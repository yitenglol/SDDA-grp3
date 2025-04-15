import java.util.*;
import java.io.*;

abstract class User {
    protected String name;
    protected String nric;
    protected int age;
    protected String maritalStatus;
    protected String password;

    public User(String name, String nric, int age, String maritalStatus, String password) {
        this.name = name;
        this.nric = nric;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.password = password;
    }

    public String getNric() {
        return nric;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public abstract String getRole();

    public String getDisplayInfo() {
        return String.format("%s, %d, %s %s", name, age, maritalStatus, getRole());
    }
}

class Officer extends User {
    public Officer(String name, String nric, int age, String maritalStatus, String password) {
        super(name, nric, age, maritalStatus, password);
    }

    @Override
    public String getRole() {
        return "Officer";
    }
}

class Manager extends User {
    public Manager(String name, String nric, int age, String maritalStatus, String password) {
        super(name, nric, age, maritalStatus, password);
    }

    @Override
    public String getRole() {
        return "Manager";
    }
}

class Applicant extends User {
    public Applicant(String name, String nric, int age, String maritalStatus, String password) {
        super(name, nric, age, maritalStatus, password);
    }

    @Override
    public String getRole() {
        return "Applicant";
    }
}

class UserManager {
    private Map<String, User> users = new HashMap<>();

    public UserManager() {
        loadUsersFromCSV("OfficerList.csv", Officer.class);
        loadUsersFromCSV("ManagerList.csv", Manager.class);
        loadUsersFromCSV("ApplicantList.csv", Applicant.class);
    }

    private void loadUsersFromCSV(String filename, Class<? extends User> userClass) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) continue;
                String name = parts[0].trim();
                String nric = parts[1].trim();
                int age = Integer.parseInt(parts[2].trim());
                String maritalStatus = parts[3].trim();
                String password = parts[4].trim();

                User user;
                if (userClass == Officer.class) {
                    user = new Officer(name, nric, age, maritalStatus, password);
                } else if (userClass == Manager.class) {
                    user = new Manager(name, nric, age, maritalStatus, password);
                } else if (userClass == Applicant.class) {
                    user = new Applicant(name, nric, age, maritalStatus, password);
                } else {
                    throw new IllegalArgumentException("Invalid user class");
                }

                users.put(nric, user);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filename);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing age in file: " + filename);
        }
    }

    public User getUserByNric(String nric) {
        return users.get(nric);
    }
}

class AuthenticationService {
    private UserManager userManager;

    public AuthenticationService(UserManager userManager) {
        this.userManager = userManager;
    }

    public User authenticate(String nric, String password) {
        User user = userManager.getUserByNric(nric);
        if (user == null || !user.checkPassword(password)) {
            return null;
        }
        return user;
    }
}

public class SDDA_grp3 {
    public static void main(String[] args) {
        UserManager userManager = new UserManager();
        AuthenticationService authService = new AuthenticationService(userManager);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Welcome to SDDA-grp3 system.");
            System.out.print("Login (enter q to quit): ");
            String nric = scanner.nextLine().trim();
            if (nric.equalsIgnoreCase("q")) {
                System.out.println("Goodbye!");
                break;
            }

            System.out.print("Password: ");
            String password = scanner.nextLine().trim();

            User user = authService.authenticate(nric, password);
            if (user == null) {
                User exists = userManager.getUserByNric(nric);
                if (exists == null) {
                    System.out.println("User not exist!");
                } else {
                    System.out.println("Wrong password!");
                }
                System.out.println();
                continue;
            }

            System.out.println("Success! Welcome " + user.getDisplayInfo() + ".");
            handleUserMenu(user, scanner);
        }
        scanner.close();
    }

    private static void handleUserMenu(User user, Scanner scanner) {
        while (true) {
            System.out.println("1) Logout");
            System.out.println("2) Change Password");
            System.out.print("Choice: ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                return;
            } else if (choice.equals("2")) {
                System.out.print("Enter new password: ");
                String newPassword = scanner.nextLine().trim();
                user.changePassword(newPassword);
                System.out.println("Password change success! Welcome " + user.getDisplayInfo() + ".");
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }
}