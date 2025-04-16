import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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

class Project {
    private String projectName;
    private String neighborhood;
    private String type1;
    private int numUnitsType1;
    private int priceType1;
    private String type2;
    private int numUnitsType2;
    private int priceType2;
    private String openingDate;
    private String closingDate;
    private String managerName;
    private int officerSlots;
    private List<String> officers;
    private boolean visibility;

    public Project(String projectName, String neighborhood, String type1, int numUnitsType1, int priceType1,
                   String type2, int numUnitsType2, int priceType2, String openingDate, String closingDate,
                   String managerName, int officerSlots, List<String> officers, boolean visibility) {
        this.projectName = projectName;
        this.neighborhood = neighborhood;
        this.type1 = type1;
        this.numUnitsType1 = numUnitsType1;
        this.priceType1 = priceType1;
        this.type2 = type2;
        this.numUnitsType2 = numUnitsType2;
        this.priceType2 = priceType2;
        this.openingDate = openingDate;
        this.closingDate = closingDate;
        this.managerName = managerName;
        this.officerSlots = officerSlots;
        this.officers = officers != null ? new ArrayList<>(officers) : new ArrayList<>();
        this.visibility = visibility;
    }

    public String getProjectName() { return projectName; }
    public String getNeighborhood() { return neighborhood; }
    public String getType1() { return type1; }
    public int getNumUnitsType1() { return numUnitsType1; }
    public int getPriceType1() { return priceType1; }
    public String getType2() { return type2; }
    public int getNumUnitsType2() { return numUnitsType2; }
    public int getPriceType2() { return priceType2; }
    public String getOpeningDate() { return openingDate; }
    public String getClosingDate() { return closingDate; }
    public String getManagerName() { return managerName; }
    public int getOfficerSlots() { return officerSlots; }
    public List<String> getOfficers() { return new ArrayList<>(officers); }
    public boolean getVisibility() { return visibility; }
    public void setVisibility(boolean visibility) { this.visibility = visibility; }
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

    public static List<Project> readProjectsFromCSV(String filename) {
        List<Project> projects = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) {
            return projects;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;
            boolean hasVisibilityColumn = false;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    String[] headers = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                    List<String> headerFields = new ArrayList<>();
                    for (String header : headers) {
                        headerFields.add(header.trim());
                    }
                    hasVisibilityColumn = headerFields.size() > 13 && headerFields.get(13).equalsIgnoreCase("Visibility");
                    continue;
                }
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                List<String> fields = new ArrayList<>();
                for (String part : parts) {
                    String field = part.trim();
                    if (field.startsWith("\"") && field.endsWith("\"")) {
                        field = field.substring(1, field.length() - 1);
                    }
                    fields.add(field);
                }
                if (fields.size() < 13) {
                    continue;
                }
                String projectName = fields.get(0);
                String neighborhood = fields.get(1);
                String type1 = fields.get(2);
                int numUnitsType1 = Integer.parseInt(fields.get(3));
                int priceType1 = Integer.parseInt(fields.get(4));
                String type2 = fields.get(5);
                int numUnitsType2 = Integer.parseInt(fields.get(6));
                int priceType2 = Integer.parseInt(fields.get(7));
                String openingDate = fields.get(8);
                String closingDate = fields.get(9);
                String managerName = fields.get(10);
                int officerSlots = Integer.parseInt(fields.get(11));
                String officersStr = fields.get(12);
                List<String> officers = new ArrayList<>();
                if (!officersStr.isEmpty()) {
                    String[] officerArray = officersStr.split(",");
                    for (String officer : officerArray) {
                        officers.add(officer.trim());
                    }
                }
                boolean visibility = false;
                if (hasVisibilityColumn && fields.size() > 13) {
                    String visibilityStr = fields.get(13);
                    visibility = Boolean.parseBoolean(visibilityStr);
                }
                Project project = new Project(projectName, neighborhood, type1, numUnitsType1, priceType1, type2, numUnitsType2, priceType2, openingDate, closingDate, managerName, officerSlots, officers, visibility);
                projects.add(project);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return projects;
    }

    public static boolean writeProjectsToCSV(String filename, List<Project> projects) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println("Project Name,Neighborhood,Type 1,Number of units for Type 1,Selling price for Type 1,Type 2,Number of units for Type 2,Selling price for Type 2,Application opening date,Application closing date,Manager,Officer Slot,Officer,Visibility");
            for (Project project : projects) {
                String officersStr = String.join(",", project.getOfficers());
                if (officersStr.contains(",")) {
                    officersStr = "\"" + officersStr + "\"";
                }
                pw.printf("%s,%s,%s,%d,%d,%s,%d,%d,%s,%s,%s,%d,%s,%b%n",
                        project.getProjectName(),
                        project.getNeighborhood(),
                        project.getType1(),
                        project.getNumUnitsType1(),
                        project.getPriceType1(),
                        project.getType2(),
                        project.getNumUnitsType2(),
                        project.getPriceType2(),
                        project.getOpeningDate(),
                        project.getClosingDate(),
                        project.getManagerName(),
                        project.getOfficerSlots(),
                        officersStr,
                        project.getVisibility());
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
    private static boolean isEligibleForRoomType(User user, String roomType) {
        String maritalStatus = user.getMaritalStatus().toLowerCase();
        int age = user.getAge();
        String rt = roomType.toLowerCase();

        if (rt.equals("2-room")) {
            return (maritalStatus.equals("single") && age >= 35) || (maritalStatus.equals("married") && age >= 21);
        } else if (rt.equals("3-room")) {
            return maritalStatus.equals("married") && age >= 21;
        }
        return false;
    }

    private static void viewEligibleProjects(User user, Scanner scanner) {
        List<Project> projects = FileHandler.readProjectsFromCSV("ProjectList.csv");
        List<Project> visibleProjects = projects.stream()
                .filter(Project::getVisibility)
                .collect(Collectors.toList());

        List<String> eligibleEntries = new ArrayList<>();
        for (Project project : visibleProjects) {
            String type1 = project.getType1();
            if (isEligibleForRoomType(user, type1)) {
                eligibleEntries.add(String.format("%s\t\t%s\t\t%d", project.getProjectName(), type1, project.getPriceType1()));
            }
            String type2 = project.getType2();
            if (isEligibleForRoomType(user, type2)) {
                eligibleEntries.add(String.format("%s\t\t%s\t\t%d", project.getProjectName(), type2, project.getPriceType2()));
            }
        }

        if (eligibleEntries.isEmpty()) {
            System.out.printf("No Eligible Projects Welcome %s, %d, %s %s.%n",
                    user.getName(),
                    user.getAge(),
                    user.getMaritalStatus(),
                    user instanceof Officer ? "Officer" : "Applicant");
        } else {
            System.out.println("=============================================================");
            System.out.println("Project Name\t\tType\t\tPrice");
            eligibleEntries.forEach(System.out::println);
            System.out.println("=============================================================");
            System.out.printf("Welcome %s, %d, %s %s.%n",
                    user.getName(),
                    user.getAge(),
                    user.getMaritalStatus(),
                    user instanceof Officer ? "Officer" : "Applicant");
        }
    }

    private static void createProject(Manager manager, Scanner scanner) {
        List<Project> projects = FileHandler.readProjectsFromCSV("ProjectList.csv");
        String projectName;
        boolean projectExists;

        do {
            System.out.print("Project Name: ");
            String inputName = scanner.nextLine().trim();
            projectName = inputName;
            String finalProjectName = projectName;
            projectExists = projects.stream().anyMatch(p -> p.getProjectName().equalsIgnoreCase(finalProjectName));
            if (projectExists) {
                System.out.println("Error! Project already exists");
            }
        } while (projectExists);

        System.out.print("Neighbourhood: ");
        String neighborhood = scanner.nextLine().trim();

        int numUnitsType1;
        do {
            System.out.print("Number of 2-room units: ");
            numUnitsType1 = Integer.parseInt(scanner.nextLine().trim());
            if (numUnitsType1 < 0) {
                System.out.println("Error! Number of units cannot be negative");
            }
        } while (numUnitsType1 < 0);

        int priceType1;
        do {
            System.out.print("Selling price of 2-room units: ");
            priceType1 = Integer.parseInt(scanner.nextLine().trim());
            if (priceType1 <= 0) {
                System.out.println("Error! Selling price must be positive");
            }
        } while (priceType1 <= 0);

        int numUnitsType2;
        do {
            System.out.print("Number of 3-room units: ");
            numUnitsType2 = Integer.parseInt(scanner.nextLine().trim());
            if (numUnitsType2 < 0) {
                System.out.println("Error! Number of units cannot be negative");
            }
        } while (numUnitsType2 < 0);

        int priceType2;
        do {
            System.out.print("Selling price of 3-room units: ");
            priceType2 = Integer.parseInt(scanner.nextLine().trim());
            if (priceType2 <= 0) {
                System.out.println("Error! Selling price must be positive");
            }
        } while (priceType2 <= 0);

        String openingDate;
        String closingDate;
        do {
            System.out.print("Application opening date: ");
            openingDate = scanner.nextLine().trim();
            System.out.print("Application closing date: ");
            closingDate = scanner.nextLine().trim();
            if (closingDate.compareTo(openingDate) < 0) {
                System.out.println("Error! Closing date can't be earlier than opening date!");
            }
        } while (closingDate.compareTo(openingDate) < 0);

        int officerSlots;
        do {
            System.out.print("HDB Officer Slots: ");
            officerSlots = Integer.parseInt(scanner.nextLine().trim());
            if (officerSlots < 0 || officerSlots > 10) {
                System.out.println("Error! Officer slots cannot be negative or more than 10");
            }
        } while (officerSlots < 0 || officerSlots > 10);

        Project newProject = new Project(projectName, neighborhood, "2-Room", numUnitsType1, priceType1, "3-Room", numUnitsType2, priceType2, openingDate, closingDate, manager.getName(), officerSlots, new ArrayList<>(), false);
        projects.add(newProject);

        boolean success = FileHandler.writeProjectsToCSV("ProjectList.csv", projects);
        if (success) {
            System.out.println("Project Created! Welcome " + manager.getName() + ", " + manager.getAge() + ", " + manager.getMaritalStatus() + " Manager.");
        } else {
            System.out.println("Error creating project.");
        }
    }

    private static void toggleVisibility(Manager manager, Scanner scanner) {
        List<Project> projects = FileHandler.readProjectsFromCSV("ProjectList.csv");
        if (projects.isEmpty()) {
            System.out.println("No projects to toggle.");
            return;
        }
        System.out.println("=============================================================");
        System.out.println("Index\tProject Name\t\tVisibility");
        int index = 1;
        for (Project p : projects) {
            System.out.printf("%d\t\t%s\t\t%s%n", index, p.getProjectName(), p.getVisibility() ? "True" : "False");
            index++;
        }
        System.out.println("=============================================================");
        System.out.print("Enter number to toggle: ");
        String input = scanner.nextLine().trim();
        try {
            int selectedIndex = Integer.parseInt(input);
            if (selectedIndex < 1 || selectedIndex > projects.size()) {
                System.out.println("Invalid index.");
                return;
            }
            Project project = projects.get(selectedIndex - 1);
            project.setVisibility(!project.getVisibility());
            boolean success = FileHandler.writeProjectsToCSV("ProjectList.csv", projects);
            if (success) {
                System.out.println("=============================================================");
                System.out.println("Index\tProject Name\t\tVisibility");
                index = 1;
                for (Project p : projects) {
                    System.out.printf("%d\t\t%s\t\t%s%n", index, p.getProjectName(), p.getVisibility() ? "True" : "False");
                    index++;
                }
                System.out.println("=============================================================");
                System.out.printf("Success, %s is now %s! Welcome %s, %d, %s Manager.%n",
                        project.getProjectName(),
                        project.getVisibility() ? "Visible" : "Hidden",
                        manager.getName(),
                        manager.getAge(),
                        manager.getMaritalStatus());
            } else {
                System.out.println("Failed to update project visibility.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }

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
                if (user instanceof Manager) {
                    System.out.println("3) Create Project");
                    System.out.println("4) Toggle Visibility");
                } else {
                    System.out.println("3) View Eligible Projects");
                }
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
                    case "3":
                        if (user instanceof Manager) {
                            createProject((Manager) user, scanner);
                        } else {
                            viewEligibleProjects(user, scanner);
                        }
                        break;
                    case "4":
                        if (user instanceof Manager) {
                            toggleVisibility((Manager) user, scanner);
                        } else {
                            System.out.println("Invalid choice.");
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