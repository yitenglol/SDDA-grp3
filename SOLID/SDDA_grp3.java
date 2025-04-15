import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

abstract class User {
    private String name;
    private String nric;
    private int age;
    private String maritalStatus;
    private String password;

    public User(String name, String nric, int age, String maritalStatus, String password) {
        this.name = name;
        this.nric = nric;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.password = password;
    }

    public abstract String getCSVFilename();

    public String getName() { return name; }
    public String getNric() { return nric; }
    public int getAge() { return age; }
    public String getMaritalStatus() { return maritalStatus; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

class Applicant extends User {
    public Applicant(String name, String nric, int age, String maritalStatus, String password) {
        super(name, nric, age, maritalStatus, password);
    }

    @Override
    public String getCSVFilename() {
        return "ApplicantList.csv";
    }
}

class Officer extends User {
    public Officer(String name, String nric, int age, String maritalStatus, String password) {
        super(name, nric, age, maritalStatus, password);
    }

    @Override
    public String getCSVFilename() {
        return "OfficerList.csv";
    }
}

class Manager extends User {
    public Manager(String name, String nric, int age, String maritalStatus, String password) {
        super(name, nric, age, maritalStatus, password);
    }

    @Override
    public String getCSVFilename() {
        return "ManagerList.csv";
    }
}

class FileHandler {
    public static <T extends User> List<T> readUsersFromCSV(String filename, Class<T> userClass) {
        List<T> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length < 5) continue;
                String name = parts[0].trim();
                String nric = parts[1].trim();
                int age = Integer.parseInt(parts[2].trim());
                String maritalStatus = parts[3].trim();
                String password = parts[4].trim();
                T user = userClass.getDeclaredConstructor(String.class, String.class, int.class, String.class, String.class)
                        .newInstance(name, nric, age, maritalStatus, password);
                users.add(user);
            }
        } catch (IOException | ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static boolean writeUsersToCSV(String filename, List<? extends User> users) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println("Name,NRIC,Age,Marital Status,Password");
            for (User user : users) {
                pw.printf("%s,%s,%d,%s,%s%n",
                        user.getName(),
                        user.getNric(),
                        user.getAge(),
                        user.getMaritalStatus(),
                        user.getPassword());
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

class NRICValidator {
    public static boolean isValid(String nric) {
        return nric != null && nric.matches("^[STst]\\d{7}[A-Za-z]$");
    }
}

class LoginManager {
    public static User findUserByNRIC(String nric) {
        List<Applicant> applicants = FileHandler.readUsersFromCSV("ApplicantList.csv", Applicant.class);
        for (Applicant a : applicants) {
            if (a.getNric().equalsIgnoreCase(nric)) return a;
        }

        List<Officer> officers = FileHandler.readUsersFromCSV("OfficerList.csv", Officer.class);
        for (Officer o : officers) {
            if (o.getNric().equalsIgnoreCase(nric)) return o;
        }

        List<Manager> managers = FileHandler.readUsersFromCSV("ManagerList.csv", Manager.class);
        for (Manager m : managers) {
            if (m.getNric().equalsIgnoreCase(nric)) return m;
        }

        return null;
    }
}

class PasswordChanger {
    public static boolean changePassword(User user) {
        String csvFilename = user.getCSVFilename();
        List<? extends User> users = FileHandler.readUsersFromCSV(csvFilename, user.getClass());
        boolean found = false;
        for (User u : users) {
            if (u.getNric().equalsIgnoreCase(user.getNric())) {
                u.setPassword(user.getPassword());
                found = true;
                break;
            }
        }
        if (!found) return false;
        return FileHandler.writeUsersToCSV(csvFilename, users);
    }
}

public class SDDA_grp3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Welcome to SDDA-grp3 system.");
            System.out.print("Login (enter q to quit): ");
            String nricInput = scanner.nextLine().trim();
            if (nricInput.equalsIgnoreCase("q")) {
                System.out.println("Goodbye!");
                break;
            }
            if (!NRICValidator.isValid(nricInput)) {
                System.out.println("Invalid format!");
                continue;
            }
            User user = LoginManager.findUserByNRIC(nricInput);
            if (user == null) {
                System.out.println("User not exist!");
                continue;
            }
            System.out.print("Password: ");
            String passwordInput = scanner.nextLine().trim();
            if (!user.getPassword().equals(passwordInput)) {
                System.out.println("Wrong password!");
                continue;
            }
            String userType = user instanceof Manager ? "Manager" :
                    user instanceof Officer ? "Officer" : "Applicant";
            System.out.printf("Success! Welcome %s, %d, %s %s.%n",
                    user.getName(), user.getAge(), user.getMaritalStatus(), userType);
            boolean logout = false;
            while (!logout) {
                System.out.println("1) Logout");
                System.out.println("2) Change Password");
                System.out.print("Choice: ");
                String choice = scanner.nextLine().trim();
                switch (choice) {
                    case "1":
                        logout = true;
                        break;
                    case "2":
                        System.out.print("Enter new password: ");
                        String newPassword = scanner.nextLine().trim();
                        user.setPassword(newPassword);
                        boolean success = PasswordChanger.changePassword(user);
                        if (success) {
                            System.out.printf("Password change success! Welcome %s, %d, %s %s.%n",
                                    user.getName(), user.getAge(), user.getMaritalStatus(), userType);
                        } else {
                            System.out.println("Password change failed.");
                        }
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            }
        }
        scanner.close();
    }
}