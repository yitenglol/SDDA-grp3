/**
 * This java file contains different classes and methods for a Build-To-Order (BTO) Management System.
 * It includes the user management, project management, and the handling of enquiries with other functions to complement the whole system.
 */


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;



/**
 * Abstract class representing the user in the System
 */
abstract class User {
    private String name;
    private String nric;
    private int age;
    private String maritalStatus;
    private String password;
    private String filter;

    /**
     * Instantiates a new User with specific details
     *
     * @param name          The name of the user
     * @param nric          The NRIC of the user
     * @param age           The age of the user
     * @param maritalStatus The maritalStatus of the user
     * @param password      The password of the user
     * @param filter        The filter of the user
     */
    public User(String name, String nric, int age, String maritalStatus, String password, String filter) {
        this.name = name;
        this.nric = nric;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.password = password;
        this.filter = filter;
    }

    /**
     * Gets csv filename associated with the type of user.
     *
     * @return the csv filename
     */
    public abstract String getCSVFilename();

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() { return name; }

    /**
     * Gets NRIC.
     *
     * @return the nric
     */
    public String getNric() { return nric; }

    /**
     * Gets age.
     *
     * @return the age
     */
    public int getAge() { return age; }

    /**
     * Gets marital status.
     *
     * @return the marital status
     */
    public String getMaritalStatus() { return maritalStatus; }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() { return password; }

    /**
     * Gets filter.
     *
     * @return the filter
     */
    public String getFilter() { return filter; }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) { this.password = password; }

    /**
     * Sets filter.
     *
     * @param filter the filter
     */
    public void setFilter(String filter) { this.filter = filter; }
}

/**
 * Class representing applicant type user in the system
 */
class Applicant extends User {
    /**
     * Instantiates a new applicant with specific details
     *
     * @param name          The name of the applicant
     * @param nric          The NRIC of the applicant
     * @param age           The age of the applicant
     * @param maritalStatus The marital status of the applicant
     * @param password      The password of the applicant
     * @param filter        The filter of the applicant
     */
    public Applicant(String name, String nric, int age, String maritalStatus, String password, String filter) {
        super(name, nric, age, maritalStatus, password, filter);
    }

    /**
     * Gets csv filename.
     *
     * @return the csv filename
     */
    @Override
    public String getCSVFilename() {
        return "ApplicantList.csv";
    }
}

/**
 * Class representing officer type user in the system
 */
class Officer extends User {
    /**
     * Instantiates a new Officer.
     *
     * @param name          The name of the officer
     * @param nric          The NRIC of the officer
     * @param age           The age of the officer
     * @param maritalStatus The marital status of the officer
     * @param password      The password of the officer
     * @param filter        The filter of the officer
     */
    public Officer(String name, String nric, int age, String maritalStatus, String password, String filter) {
        super(name, nric, age, maritalStatus, password, filter);
    }

    /**
     * Gets csv filename.
     *
     * @return the csv filename
     */
    @Override
    public String getCSVFilename() {
        return "OfficerList.csv";
    }
}

/**
 * Class representing Manager type user in the system
 */
class Manager extends User {
    /**
     * Instantiates a new Manager.
     *
     * @param name          The name of the Manager
     * @param nric          The NRIC of the Manager
     * @param age           The age of the Manager
     * @param maritalStatus The marital status of the Manager
     * @param password      The password of the Manager
     * @param filter        The filter of the Manager
     */
    public Manager(String name, String nric, int age, String maritalStatus, String password, String filter) {
        super(name, nric, age, maritalStatus, password, filter);
    }

    /**
     * Gets csv filename.
     *
     * @return the csv filename
     */
    @Override
    public String getCSVFilename() {
        return "ManagerList.csv";
    }
}

/**
 * Class representing project in the system
 */
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
    private List<String> officerPending;
    private List<String> officerUnsuccessful;
    private List<String> type1OwnerPending;
    private List<String> type1OwnerUnsuccessful;
    private List<String> type1OwnerSuccessful;
    private List<String> type1OwnerBooked;
    private List<String> type1WithdrawalPending;
    private List<String> type2OwnerPending;
    private List<String> type2OwnerUnsuccessful;
    private List<String> type2OwnerSuccessful;
    private List<String> type2OwnerBooked;
    private List<String> type2WithdrawalPending;

    /**
     * Instantiates a new Project.
     *
     * @param projectName            The name of the project.t
     * @param neighborhood           The neighborhood where project is located.
     * @param type1                  The type of the first unit.
     * @param numUnitsType1          The number of type 1 units.
     * @param priceType1             The price for type 1 unit.
     * @param type2                  The type of the second unit.
     * @param numUnitsType2          The number of type 2 units.
     * @param priceType2             The price for type 1 units.
     * @param openingDate            The opening date of the project.
     * @param closingDate            The closing date of the project.
     * @param managerName            The manager name.
     * @param officerSlots           The number of officer assigned to the project.
     * @param officers               The officers.
     * @param visibility             Visibility status of the project.
     * @param officerPending         The list of officers pending assignment to the project.
     * @param officerUnsuccessful    The list of officers who were unsuccessful in their application for the project.
     * @param type1OwnerPending      The list of owners pending approval for type 1 units.
     * @param type1OwnerUnsuccessful The list of owners who were unsuccessful in their application for type 1 units.
     * @param type1OwnerSuccessful   The list of owners who were successful in their application for type 1 units.
     * @param type1OwnerBooked       The list of owners who have applied type 1 units.
     * @param type1WithdrawalPending The list of owners who have requested withdrawal from type 1 units.
     * @param type2OwnerPending      The list of owners pending approval for type 2 units.
     * @param type2OwnerUnsuccessful The list of owners who were unsuccessful in their application for type 2 units.
     * @param type2OwnerSuccessful   The list of owners who were successful in their application for type 2 units.
     * @param type2OwnerBooked       The list of owners who have applied type 2 units.
     * @param type2WithdrawalPending The list of owners who have requested withdrawal from type 2 units.
     */
    public Project(String projectName, String neighborhood, String type1, int numUnitsType1, int priceType1,
                   String type2, int numUnitsType2, int priceType2, String openingDate, String closingDate,
                   String managerName, int officerSlots, List<String> officers, boolean visibility,
                   List<String> officerPending, List<String> officerUnsuccessful,
                   List<String> type1OwnerPending, List<String> type1OwnerUnsuccessful,
                   List<String> type1OwnerSuccessful, List<String> type1OwnerBooked,
                   List<String> type1WithdrawalPending, List<String> type2OwnerPending, List<String> type2OwnerUnsuccessful,
                   List<String> type2OwnerSuccessful, List<String> type2OwnerBooked,
                   List<String> type2WithdrawalPending) {
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
        this.officerPending = officerPending != null ? new ArrayList<>(officerPending) : new ArrayList<>();
        this.officerUnsuccessful = officerUnsuccessful != null ? new ArrayList<>(officerUnsuccessful) : new ArrayList<>();
        this.type1OwnerPending = type1OwnerPending != null ? new ArrayList<>(type1OwnerPending) : new ArrayList<>();
        this.type1OwnerUnsuccessful = type1OwnerUnsuccessful != null ? new ArrayList<>(type1OwnerUnsuccessful) : new ArrayList<>();
        this.type1OwnerSuccessful = type1OwnerSuccessful != null ? new ArrayList<>(type1OwnerSuccessful) : new ArrayList<>();
        this.type1OwnerBooked = type1OwnerBooked != null ? new ArrayList<>(type1OwnerBooked) : new ArrayList<>();
		this.type1WithdrawalPending = type1WithdrawalPending != null ? new ArrayList<>(type1WithdrawalPending) : new ArrayList<>();
        this.type2OwnerPending = type2OwnerPending != null ? new ArrayList<>(type2OwnerPending) : new ArrayList<>();
        this.type2OwnerUnsuccessful = type2OwnerUnsuccessful != null ? new ArrayList<>(type2OwnerUnsuccessful) : new ArrayList<>();
        this.type2OwnerSuccessful = type2OwnerSuccessful != null ? new ArrayList<>(type2OwnerSuccessful) : new ArrayList<>();
        this.type2OwnerBooked = type2OwnerBooked != null ? new ArrayList<>(type2OwnerBooked) : new ArrayList<>();
        this.type2WithdrawalPending = type2WithdrawalPending != null ? new ArrayList<>(type2WithdrawalPending) : new ArrayList<>();
    }

    /**
     * Gets project name.
     *
     * @return the project name
     */
    public String getProjectName() { return projectName; }

    /**
     * Gets neighborhood.
     *
     * @return the neighborhood
     */
    public String getNeighborhood() { return neighborhood; }

    /**
     * Gets type 1.
     *
     * @return the type 1
     */
    public String getType1() { return type1; }

    /**
     * Gets num units type 1.
     *
     * @return the num units type 1
     */
    public int getNumUnitsType1() { return numUnitsType1; }

    /**
     * Gets price type 1.
     *
     * @return the price type 1
     */
    public int getPriceType1() { return priceType1; }

    /**
     * Gets type 2.
     *
     * @return the type 2
     */
    public String getType2() { return type2; }

    /**
     * Gets num units type 2.
     *
     * @return the num units type 2
     */
    public int getNumUnitsType2() { return numUnitsType2; }

    /**
     * Gets price type 2.
     *
     * @return the price type 2
     */
    public int getPriceType2() { return priceType2; }

    /**
     * Gets opening date.
     *
     * @return the opening date
     */
    public String getOpeningDate() { return openingDate; }

    /**
     * Gets closing date.
     *
     * @return the closing date
     */
    public String getClosingDate() { return closingDate; }

    /**
     * Gets manager name.
     *
     * @return the manager name
     */
    public String getManagerName() { return managerName; }

    /**
     * Gets officer slots.
     *
     * @return the officer slots
     */
    public int getOfficerSlots() { return officerSlots; }

    /**
     * Gets officers.
     *
     * @return the officers
     */
    public List<String> getOfficers() { return new ArrayList<>(officers); }

    /**
     * Gets visibility.
     *
     * @return the visibility
     */
    public boolean getVisibility() { return visibility; }

    /**
     * Sets visibility.
     *
     * @param visibility the visibility
     */
    public void setVisibility(boolean visibility) { this.visibility = visibility; }

    /**
     * Gets officer pending.
     *
     * @return the officer pending
     */
    public List<String> getOfficerPending() { return new ArrayList<>(officerPending); }

    /**
     * Gets officer unsuccessful.
     *
     * @return the officer unsuccessful
     */
    public List<String> getOfficerUnsuccessful() { return new ArrayList<>(officerUnsuccessful); }

    /**
     * Gets type 1 owner pending.
     *
     * @return the type 1 owner pending
     */
    public List<String> getType1OwnerPending() { return new ArrayList<>(type1OwnerPending); }

    /**
     * Gets type 1 owner unsuccessful.
     *
     * @return the type 1 owner unsuccessful
     */
    public List<String> getType1OwnerUnsuccessful() { return new ArrayList<>(type1OwnerUnsuccessful); }

    /**
     * Gets type 1 owner successful.
     *
     * @return the type 1 owner successful
     */
    public List<String> getType1OwnerSuccessful() { return new ArrayList<>(type1OwnerSuccessful); }

    /**
     * Gets type 1 owner booked.
     *
     * @return the type 1 owner booked
     */
    public List<String> getType1OwnerBooked() { return new ArrayList<>(type1OwnerBooked); }

    /**
     * Gets type 1 withdrawal pending.
     *
     * @return the type 1 withdrawal pending
     */
    public List<String> getType1WithdrawalPending() { return new ArrayList<>(type1WithdrawalPending); }

    /**
     * Gets type 2 owner pending.
     *
     * @return the type 2 owner pending
     */
    public List<String> getType2OwnerPending() { return new ArrayList<>(type2OwnerPending); }

    /**
     * Gets type 2 owner unsuccessful.
     *
     * @return the type 2 owner unsuccessful
     */
    public List<String> getType2OwnerUnsuccessful() { return new ArrayList<>(type2OwnerUnsuccessful); }

    /**
     * Gets type 2 owner successful.
     *
     * @return the type 2 owner successful
     */
    public List<String> getType2OwnerSuccessful() { return new ArrayList<>(type2OwnerSuccessful); }

    /**
     * Gets type 2 owner booked.
     *
     * @return the type 2 owner booked
     */
    public List<String> getType2OwnerBooked() { return new ArrayList<>(type2OwnerBooked); }

    /**
     * Gets type 2 withdrawal pending.
     *
     * @return the type 2 withdrawal pending
     */
    public List<String> getType2WithdrawalPending() { return new ArrayList<>(type2WithdrawalPending); }

    /**
     * Sets neighborhood.
     *
     * @param neighborhood the neighborhood
     */
    public void setNeighborhood(String neighborhood) { this.neighborhood = neighborhood; }

    /**
     * Sets num units type 1.
     *
     * @param numUnitsType1 the num units type 1
     */
    public void setNumUnitsType1(int numUnitsType1) { this.numUnitsType1 = numUnitsType1; }

    /**
     * Sets price type 1.
     *
     * @param priceType1 the price type 1
     */
    public void setPriceType1(int priceType1) { this.priceType1 = priceType1; }

    /**
     * Sets num units type 2.
     *
     * @param numUnitsType2 the num units type 2
     */
    public void setNumUnitsType2(int numUnitsType2) { this.numUnitsType2 = numUnitsType2; }

    /**
     * Sets price type 2.
     *
     * @param priceType2 the price type 2
     */
    public void setPriceType2(int priceType2) { this.priceType2 = priceType2; }

    /**
     * Sets opening date.
     *
     * @param openingDate the opening date
     */
    public void setOpeningDate(String openingDate) { this.openingDate = openingDate; }

    /**
     * Sets closing date.
     *
     * @param closingDate the closing date
     */
    public void setClosingDate(String closingDate) { this.closingDate = closingDate; }

    /**
     * Sets officer slots.
     *
     * @param officerSlots the officer slots
     */
    public void setOfficerSlots(int officerSlots) { this.officerSlots = officerSlots; }

    /**
     * Sets officer pending.
     *
     * @param officerPending the officer pending
     */
    public void setOfficerPending(List<String> officerPending) { this.officerPending = new ArrayList<>(officerPending); }

    /**
     * Sets officer unsuccessful.
     *
     * @param officerUnsuccessful the officer unsuccessful
     */
    public void setOfficerUnsuccessful(List<String> officerUnsuccessful) { this.officerUnsuccessful = new ArrayList<>(officerUnsuccessful); }

    /**
     * Sets type 1 owner pending.
     *
     * @param type1OwnerPending the type 1 owner pending
     */
    public void setType1OwnerPending(List<String> type1OwnerPending) { this.type1OwnerPending = new ArrayList<>(type1OwnerPending); }

    /**
     * Sets type 1 owner unsuccessful.
     *
     * @param type1OwnerUnsuccessful the type 1 owner unsuccessful
     */
    public void setType1OwnerUnsuccessful(List<String> type1OwnerUnsuccessful) { this.type1OwnerUnsuccessful = new ArrayList<>(type1OwnerUnsuccessful); }

    /**
     * Sets type 1 owner successful.
     *
     * @param type1OwnerSuccessful the type 1 owner successful
     */
    public void setType1OwnerSuccessful(List<String> type1OwnerSuccessful) { this.type1OwnerSuccessful = new ArrayList<>(type1OwnerSuccessful); }

    /**
     * Sets type 1 owner booked.
     *
     * @param type1OwnerBooked the type 1 owner booked
     */
    public void setType1OwnerBooked(List<String> type1OwnerBooked) { this.type1OwnerBooked = new ArrayList<>(type1OwnerBooked); }

    /**
     * Sets type 1 withdrawal pending.
     *
     * @param type1WithdrawalPending the type 1 withdrawal pending
     */
    public void setType1WithdrawalPending(List<String> type1WithdrawalPending) { this.type1WithdrawalPending = new ArrayList<>(type1WithdrawalPending); }

    /**
     * Sets type 2 owner pending.
     *
     * @param type2OwnerPending the type 2 owner pending
     */
    public void setType2OwnerPending(List<String> type2OwnerPending) { this.type2OwnerPending = new ArrayList<>(type2OwnerPending); }

    /**
     * Sets type 2 owner unsuccessful.
     *
     * @param type2OwnerUnsuccessful the type 2 owner unsuccessful
     */
    public void setType2OwnerUnsuccessful(List<String> type2OwnerUnsuccessful) { this.type2OwnerUnsuccessful = new ArrayList<>(type2OwnerUnsuccessful); }

    /**
     * Sets type 2 owner successful.
     *
     * @param type2OwnerSuccessful the type 2 owner successful
     */
    public void setType2OwnerSuccessful(List<String> type2OwnerSuccessful) { this.type2OwnerSuccessful = new ArrayList<>(type2OwnerSuccessful); }

    /**
     * Sets type 2 owner booked.
     *
     * @param type2OwnerBooked the type 2 owner booked
     */
    public void setType2OwnerBooked(List<String> type2OwnerBooked) { this.type2OwnerBooked = new ArrayList<>(type2OwnerBooked); }

    /**
     * Sets type 2 withdrawal pending.
     *
     * @param type2WithdrawalPending the type 2 withdrawal pending
     */
    public void setType2WithdrawalPending(List<String> type2WithdrawalPending) { this.type2WithdrawalPending = new ArrayList<>(type2WithdrawalPending); }
}

/**
 * Class representing FileHandler in the system
 */
class FileHandler {
    /**
     * Read users from csv list.
     *
     * @param <T>       the type parameter
     * @param filename  the filename of the csv
     * @param userClass the user class
     * @return the list
     */
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
                String filter = "None";
                if (parts.length >= 6) {
                    filter = parts[5].trim();
                }
                T user = userClass.getDeclaredConstructor(String.class, String.class, int.class, String.class, String.class, String.class)
                        .newInstance(name, nric, age, maritalStatus, password, filter);
                users.add(user);
            }
        } catch (IOException | ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Write users to csv boolean.
     *
     * @param filename the filename
     * @param users    the users
     * @return the boolean
     */
    public static boolean writeUsersToCSV(String filename, List<? extends User> users) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println("Name,NRIC,Age,Marital Status,Password,Filter");
            for (User user : users) {
                pw.printf("%s,%s,%d,%s,%s,%s%n",
                        user.getName(),
                        user.getNric(),
                        user.getAge(),
                        user.getMaritalStatus(),
                        user.getPassword(),
                        user.getFilter());
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Read projects from csv list.
     *
     * @param filename the filename of the csv
     * @return the list
     */
    public static List<Project> readProjectsFromCSV(String filename) {
        List<Project> projects = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) {
            return projects;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
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
                if (fields.size() < 14) {
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
                List<String> officers = parseCommaSeparatedList(fields, 12);

                boolean visibility = false;
                if (fields.size() > 13 && !fields.get(13).isEmpty()) {
                    visibility = Boolean.parseBoolean(fields.get(13));
                }

                List<String> officerPending = parseCommaSeparatedList(fields, 14);
                List<String> officerUnsuccessful = parseCommaSeparatedList(fields, 15);
                List<String> type1OwnerPending = parseCommaSeparatedList(fields, 16);
                List<String> type1OwnerUnsuccessful = parseCommaSeparatedList(fields, 17);
                List<String> type1OwnerSuccessful = parseCommaSeparatedList(fields, 18);
                List<String> type1OwnerBooked = parseCommaSeparatedList(fields, 19);
				List<String> type1WithdrawalPending = parseCommaSeparatedList(fields, 20);
                List<String> type2OwnerPending = parseCommaSeparatedList(fields, 21);
                List<String> type2OwnerUnsuccessful = parseCommaSeparatedList(fields, 22);
                List<String> type2OwnerSuccessful = parseCommaSeparatedList(fields, 23);
                List<String> type2OwnerBooked = parseCommaSeparatedList(fields, 24);
				List<String> type2WithdrawalPending = parseCommaSeparatedList(fields, 25);

                Project project = new Project(projectName, neighborhood, type1, numUnitsType1, priceType1, type2, numUnitsType2, priceType2, openingDate, closingDate, managerName, officerSlots, officers, visibility, officerPending, officerUnsuccessful, type1OwnerPending, type1OwnerUnsuccessful, type1OwnerSuccessful, type1OwnerBooked, type1WithdrawalPending, type2OwnerPending, type2OwnerUnsuccessful, type2OwnerSuccessful, type2OwnerBooked, type2WithdrawalPending);
                projects.add(project);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return projects;
    }

/**
 * Parses the CSV list from a specified index from a list of strings
 * Retreive the string at the given index from the given fields list
 *
 * @param fields the list of strings to parse
 * @param index the index used to parse the CSV list
 * @return list the list of trimmed strings obtained from spliting the index from the given
 * 	   field or an empty list if the field is null,empty or invalid
 */
    private static List<String> parseCommaSeparatedList(List<String> fields, int index) {
        List<String> list = new ArrayList<>();
        if (index < fields.size()) {
            String value = fields.get(index);
            if (!value.isEmpty()) {
                String[] parts = value.split(",");
                for (String part : parts) {
                    list.add(part.trim());
                }
            }
        }
        return list;
    }

    /**
     * Write projects to csv boolean.
     *
     * @param filename the filename
     * @param projects the projects
     * @return the boolean
     */
    public static boolean writeProjectsToCSV(String filename, List<Project> projects) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println("Project Name,Neighborhood,Type 1,Number of units for Type 1,Selling price for Type 1,Type 2,Number of units for Type 2,Selling price for Type 2,Application opening date,Application closing date,Manager,Officer Slot,Officer,Visibility,OfficerPending,OfficerUnsuccessful,Type1OwnerPending,Type1OwnerUnsuccessful,Type1OwnerSuccessful,Type1OwnerBooked,Type1WithdrawalPending,Type2OwnerPending,Type2OwnerUnsuccessful,Type2OwnerSuccessful,Type2OwnerBooked,Type2WithdrawalPending");
            for (Project project : projects) {
                String officersStr = getCSVString(project.getOfficers());
                String officerPendingStr = getCSVString(project.getOfficerPending());
                String officerUnsuccessfulStr = getCSVString(project.getOfficerUnsuccessful());
                String type1OwnerPendingStr = getCSVString(project.getType1OwnerPending());
                String type1OwnerUnsuccessfulStr = getCSVString(project.getType1OwnerUnsuccessful());
                String type1OwnerSuccessfulStr = getCSVString(project.getType1OwnerSuccessful());
                String type1OwnerBookedStr = getCSVString(project.getType1OwnerBooked());
                String type1WithdrawalPendingStr = getCSVString(project.getType1WithdrawalPending());
                String type2OwnerPendingStr = getCSVString(project.getType2OwnerPending());
                String type2OwnerUnsuccessfulStr = getCSVString(project.getType2OwnerUnsuccessful());
                String type2OwnerSuccessfulStr = getCSVString(project.getType2OwnerSuccessful());
                String type2OwnerBookedStr = getCSVString(project.getType2OwnerBooked());
				String type2WithdrawalPendingStr = getCSVString(project.getType2WithdrawalPending());

                pw.printf("%s,%s,%s,%d,%d,%s,%d,%d,%s,%s,%s,%d,%s,%b,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s%n",
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
                        project.getVisibility(),
                        officerPendingStr,
                        officerUnsuccessfulStr,
                        type1OwnerPendingStr,
                        type1OwnerUnsuccessfulStr,
                        type1OwnerSuccessfulStr,
                        type1OwnerBookedStr,
						type1WithdrawalPendingStr,
                        type2OwnerPendingStr,
                        type2OwnerUnsuccessfulStr,
                        type2OwnerSuccessfulStr,
                        type2OwnerBookedStr,
                        type2WithdrawalPendingStr);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

/**
* Converts a list of strings into a single CSV string.
*
* @param list
* @return a CSV-formated string in double quotes if it contains commas
*/
    private static String getCSVString(List<String> list) {
        String str = String.join(",", list);
        if (str.contains(",")) {
            str = "\"" + str + "\"";
        }
        return str;
    }

    /**
     * Read enquiries list.
     *
     * @return the list
     */
// Read all enquiries Testing
    public static List<Enquiry> readEnquiries() {
        List<Enquiry> enquiries = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("EnquiryList.csv"))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (parts.length < 4) continue;
                String name = parts[0].trim();
                String project = parts[1].trim();
                String enquiryStr = parts[2].trim();
                String reply = parts[3].trim();
                enquiries.add(new Enquiry(name, project, enquiryStr, reply));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return enquiries;
    }

    /**
     * Write enquiries boolean.
     *
     * @param enquiries the enquiries
     * @return the boolean
     */
// Write updated enquiries
    public static boolean writeEnquiries(List<Enquiry> enquiries) {
        try (PrintWriter pw = new PrintWriter("EnquiryList.csv")) {
            pw.println("Name,Project,String,Reply");
            for (Enquiry e : enquiries) {
                pw.printf("%s,%s,%s,%s%n",
                        e.getName(), e.getProject(), e.getEnquiry(), e.getReply());
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}



/**
 * Class representing NRICValidator in the system
 */
class NRICValidator {
    /**
     * Is valid boolean.
     *
     * @param nric the nric
     * @return the boolean
     */
    public static boolean isValid(String nric) {
        return nric != null && nric.matches("^[STst]\\d{7}[A-Za-z]$");
    }
}

/**
 * Class representing LoginManager in the system
 */
class LoginManager {
    /**
     * Find user by nric user.
     *
     * @param nric the nric
     * @return the user
     */
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

/**
 * Class representing changing password in the system
 */
class PasswordChanger {
    /**
     * Change password boolean.
     *
     * @param user the user
     * @return the boolean
     */
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

/**
 * Class representing the enquiry made in the system
 */
class Enquiry {
    private String name;
    private String project;
    private String enquiry;
    private String reply;

    /**
     * Instantiates a new Enquiry.
     *
     * @param name    Who is making the enquiry
     * @param project The name of the project that the enquiry is made for
     * @param enquiry What is the enquiry in question
     * @param reply   Reply to the enquiry
     */
    public Enquiry(String name, String project, String enquiry, String reply) {
        this.name = name;
        this.project = project;
        this.enquiry = enquiry;
        this.reply = reply;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() { return name; }

    /**
     * Gets project.
     *
     * @return the project
     */
    public String getProject() { return project; }

    /**
     * Gets enquiry.
     *
     * @return the enquiry
     */
    public String getEnquiry() { return enquiry; }

    /**
     * Gets reply.
     *
     * @return the reply
     */
    public String getReply() { return reply; }

    /**
     * Sets reply.
     *
     * @param reply the reply
     */
// Testing
    public void setReply(String reply) {
        this.reply = reply;
    }
}

/**
 * Main system class in the system that contains user interaction and business logic
 */
public class SDDA_grp3 {
	
	    private static boolean isNameExists(String name) {
        List<Applicant> applicants = FileHandler.readUsersFromCSV("ApplicantList.csv", Applicant.class);
        List<Officer> officers = FileHandler.readUsersFromCSV("OfficerList.csv", Officer.class);
        List<Manager> managers = FileHandler.readUsersFromCSV("ManagerList.csv", Manager.class);

        return applicants.stream().anyMatch(a -> a.getName().equalsIgnoreCase(name)) ||
               officers.stream().anyMatch(o -> o.getName().equalsIgnoreCase(name)) ||
               managers.stream().anyMatch(m -> m.getName().equalsIgnoreCase(name));
    }

    private static boolean isNricExists(String nric) {
        List<Applicant> applicants = FileHandler.readUsersFromCSV("ApplicantList.csv", Applicant.class);
        List<Officer> officers = FileHandler.readUsersFromCSV("OfficerList.csv", Officer.class);
        List<Manager> managers = FileHandler.readUsersFromCSV("ManagerList.csv", Manager.class);

        return applicants.stream().anyMatch(a -> a.getNric().equalsIgnoreCase(nric)) ||
               officers.stream().anyMatch(o -> o.getNric().equalsIgnoreCase(nric)) ||
               managers.stream().anyMatch(m -> m.getNric().equalsIgnoreCase(nric));
    }

    private static void createUserAccount(Scanner scanner) {
        System.out.print("Select User Type (m for manager, a for applicant, o for officer): ");
        String userType = scanner.nextLine().trim().toLowerCase();
        if (!userType.equals("m") && !userType.equals("a") && !userType.equals("o")) {
            System.out.println("Invalid user type.");
            return;
        }

        String name;
        boolean nameExists;
        do {
            System.out.print("Enter Name: ");
            name = scanner.nextLine().trim();
            nameExists = isNameExists(name);
            if (nameExists) {
                System.out.println("Error! " + name + " already registered");
            }
        } while (nameExists);

        String nric;
        boolean nricValid;
        do {
            System.out.print("Enter NRIC: ");
            nric = scanner.nextLine().trim();
            nricValid = NRICValidator.isValid(nric) && !isNricExists(nric);
            if (!NRICValidator.isValid(nric)) {
                System.out.println("Invalid NRIC format.");
            } else if (isNricExists(nric)) {
                System.out.println("Error! NRIC already registered");
            }
        } while (!nricValid);

        int age;
        do {
            System.out.print("Enter Age: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Enter a number.");
                scanner.next();
            }
            age = scanner.nextInt();
            scanner.nextLine();
            if (age < 0) {
                System.out.println("Error! Age cannot be negative");
            }
        } while (age < 0);

        String maritalStatus;
        do {
            System.out.print("Enter Marital Status (m for married, s for single): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("m")) {
                maritalStatus = "Married";
                break;
            } else if (input.equals("s")) {
                maritalStatus = "Single";
                break;
            } else {
                System.out.println("Invalid option.");
            }
        } while (true);

        String password;
        do {
            System.out.print("Enter password: ");
            password = scanner.nextLine().trim();
            if (password.isEmpty()) {
                System.out.println("Password cannot be empty.");
            }
        } while (password.isEmpty());
        String encodedPassword = Base64.getEncoder().encodeToString(password.getBytes());

        User newUser;
        switch (userType) {
            case "m":
                newUser = new Manager(name, nric, age, maritalStatus, encodedPassword, "None");
                break;
            case "o":
                newUser = new Officer(name, nric, age, maritalStatus, encodedPassword, "None");
                break;
            case "a":
                newUser = new Applicant(name, nric, age, maritalStatus, encodedPassword, "None");
                break;
            default:
                return;
        }

        String filename = newUser.getCSVFilename();
        List<? extends User> users = FileHandler.readUsersFromCSV(filename, newUser.getClass());
        List<Object> updatedUsers = new ArrayList<>(users);
        updatedUsers.add(newUser);
        boolean success = FileHandler.writeUsersToCSV(filename, updatedUsers.stream()
                .map(u -> (User) u)
                .collect(Collectors.toList()));

        if (success) {
            System.out.println("Success! Account Created");
        } else {
            System.out.println("Error creating account.");
        }
    }
	
	private static void changeFilter(User user, Scanner scanner) {
        System.out.printf("Current filter: %s%n", user.getFilter());
        System.out.print("Select new filter (empty for None, 2 for 2-Room, 3 for 3-Room, or enter Neighbourhood): ");
        String newFilter = scanner.nextLine().trim();
        
        if (newFilter.isEmpty()) {
            newFilter = "None";
        } else if (newFilter.equals("2")) {
            newFilter = "2-Room";
        } else if (newFilter.equals("3")) {
            newFilter = "3-Room";
        }

        user.setFilter(newFilter);
        String filename = user.getCSVFilename();
        List<? extends User> users = FileHandler.readUsersFromCSV(filename, user.getClass());
        boolean success = FileHandler.writeUsersToCSV(filename, users);
        
        if (success) {
            System.out.printf("Success, filter set to %s! Welcome %s.%n", newFilter, user.getName());
        } else {
            System.out.println("Failed to update filter.");
        }
    }
	
	private static void viewAllProjects(Manager manager, Scanner scanner) {
        List<Project> allProjects = FileHandler.readProjectsFromCSV("ProjectList.csv");
        List<EligibleEntry> entries = new ArrayList<>();
        String filter = manager.getFilter();

        for (Project project : allProjects) {
            String type1 = project.getType1();
            if (applyFilter(project, "Type1", filter)) {
                String displayStr = String.format("%s\t\t%s\t\t%d", project.getProjectName(), type1, project.getPriceType1());
                entries.add(new EligibleEntry(project, "Type1", displayStr));
            }
            String type2 = project.getType2();
            if (applyFilter(project, "Type2", filter)) {
                String displayStr = String.format("%s\t\t%s\t\t%d", project.getProjectName(), type2, project.getPriceType2());
                entries.add(new EligibleEntry(project, "Type2", displayStr));
            }
        }

        System.out.println("=============================================================");
        System.out.printf("Current filter: %s%n", manager.getFilter());
        System.out.println("Project Name\t\tType\t\tPrice");
        for (EligibleEntry entry : entries) {
            System.out.println(entry.displayString);
        }
        System.out.println("=============================================================");
    }

    private static boolean applyFilter(Project project, String typeDesignation, String filter) {
        if (filter.equals("None")) return true;
        if (filter.equals("2-Room")) return typeDesignation.equals("Type1");
        if (filter.equals("3-Room")) return typeDesignation.equals("Type2");
        return project.getNeighborhood().equalsIgnoreCase(filter);
    }

	
	private static class BookableEntry {
        /**
         * Project containing bookable flat.
         */
        Project project;
        /**
         * The flat type.
         */
        String type;
        String name;
        /**
         * Instantiates a new Bookable entry.
         *
         * @param project Project containing bookable flat
         * @param type    Flat type
         * @param name    Name of flat applicant
         */
        BookableEntry(Project project, String type, String name) {
			this.project = project;
			this.type = type;
			this.name = name;
		}
	}
	
	private static void handleBookOwner(Officer officer, Scanner scanner) {
		List<Project> allProjects = FileHandler.readProjectsFromCSV("ProjectList.csv");
		List<Project> assignedProjects = allProjects.stream()
				.filter(p -> p.getOfficers().contains(officer.getName()))
				.collect(Collectors.toList());

		List<BookableEntry> bookableEntries = new ArrayList<>();
		for (Project project : assignedProjects) {
			List<String> type1Success = project.getType1OwnerSuccessful();
			for (String name : type1Success) {
				bookableEntries.add(new BookableEntry(project, "Type1", name));
			}
			List<String> type2Success = project.getType2OwnerSuccessful();
			for (String name : type2Success) {
				bookableEntries.add(new BookableEntry(project, "Type2", name));
			}
		}

		if (bookableEntries.isEmpty()) {
			System.out.println("No bookable owners found.");
			return;
		}

		System.out.println("=============================================================");
		System.out.println("Index\tProject Name\tType\tOwner");
		int index = 1;
		for (BookableEntry entry : bookableEntries) {
			String type = entry.type.equals("Type1") ? entry.project.getType1() : entry.project.getType2();
			System.out.printf("%d\t\t%s\t\t%s\t\t%s%n", index++, entry.project.getProjectName(), type, entry.name);
		}
		System.out.println("=============================================================");

		System.out.print("Enter index of Owner to Book (enter c to cancel): ");
		String input = scanner.nextLine().trim();
		if (input.equalsIgnoreCase("c")) {
			return;
		}

		try {
			int selectedIndex = Integer.parseInt(input) - 1;
			if (selectedIndex < 0 || selectedIndex >= bookableEntries.size()) {
				System.out.println("Invalid index.");
				return;
			}

			BookableEntry selectedEntry = bookableEntries.get(selectedIndex);
			Project project = selectedEntry.project;
			String type = selectedEntry.type;
			String name = selectedEntry.name;

			int unitCount;
			if (type.equals("Type1")) {
				unitCount = project.getNumUnitsType1();
				if (unitCount <= 0) {
					System.out.println("No available units for booking.");
					return;
				}
				project.setNumUnitsType1(unitCount - 1);

				List<String> type1Successful = new ArrayList<>(project.getType1OwnerSuccessful());
				type1Successful.remove(name);
				project.setType1OwnerSuccessful(type1Successful);

				List<String> type1Booked = new ArrayList<>(project.getType1OwnerBooked());
				type1Booked.add(name);
				project.setType1OwnerBooked(type1Booked);
			} else {
				unitCount = project.getNumUnitsType2();
				if (unitCount <= 0) {
					System.out.println("No available units for booking.");
					return;
				}
				project.setNumUnitsType2(unitCount - 1);

				List<String> type2Successful = new ArrayList<>(project.getType2OwnerSuccessful());
				type2Successful.remove(name);
				project.setType2OwnerSuccessful(type2Successful);

				List<String> type2Booked = new ArrayList<>(project.getType2OwnerBooked());
				type2Booked.add(name);
				project.setType2OwnerBooked(type2Booked);
			}

			boolean success = FileHandler.writeProjectsToCSV("ProjectList.csv", allProjects);
			if (success) {
				generateReceipt(selectedEntry);
				System.out.printf("Success, generated receipt! Welcome %s.%n", officer.getName());
			} else {
				System.out.println("Failed to update project.");
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid input.");
		}
	}

	private static void generateReceipt(BookableEntry entry) {
		List<Applicant> applicants = FileHandler.readUsersFromCSV("ApplicantList.csv", Applicant.class);
		Applicant applicant = applicants.stream()
				.filter(a -> a.getName().equals(entry.name))
				.findFirst()
				.orElse(null);

		if (applicant == null) {
			System.out.println("Applicant not found.");
			return;
		}

		String projectName = entry.project.getProjectName();
		String neighborhood = entry.project.getNeighborhood();
		String flatType = entry.type.equals("Type1") ? entry.project.getType1() : entry.project.getType2();
		int price = entry.type.equals("Type1") ? entry.project.getPriceType1() : entry.project.getPriceType2();

		LocalDate currentDate = LocalDate.now();
		String dateStr = currentDate.format(DateTimeFormatter.BASIC_ISO_DATE);
		String fileName = String.format("receipt-%s-%s.txt", dateStr, applicant.getNric());

		try (PrintWriter pw = new PrintWriter(fileName)) {
			pw.printf("Applicant’s Name: %s%n", applicant.getName());
			pw.printf("NRIC: %s%n", applicant.getNric());
			pw.printf("Age: %d%n", applicant.getAge());
			pw.printf("Marital status: %s%n", applicant.getMaritalStatus());
			pw.printf("Flat type booked: %s%n", flatType);
			pw.printf("Project name: %s%n", projectName);
			pw.printf("Neighborhood: %s%n", neighborhood);
			pw.printf("Flat type price: %d%n", price);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
    private static class EligibleEntry {
        /**
         * The eligible project
         */
        Project project;
        /**
         * The flat type designation
         */
        String typeDesignation;
        /**
         * Display string for the project
         */
        String displayString;

        /**
         * Instantiates a new Eligible entry.
         *
         * @param project         Eligible Project
         * @param typeDesignation Flat type
         * @param displayString   Formatted display information
         */
        EligibleEntry(Project project, String typeDesignation, String displayString) {
            this.project = project;
            this.typeDesignation = typeDesignation;
            this.displayString = displayString;
        }
    }

    private static class PendingEntry {
        /**
         * Project with plending application
         */
        Project project;
        /**
         * Flat type
         */
        String type;
        /**
         * Applicant name
         */
        String ownerName;

        /**
         * Instantiates a new Pending entry.
         *
         * @param project   Project with pending application
         * @param type      Flat type
         * @param ownerName Applicants name
         */
        PendingEntry(Project project, String type, String ownerName) {
            this.project = project;
            this.type = type;
            this.ownerName = ownerName;
        }
    }
	
	
	private static class WithdrawalEntry {
        /**
         * Project for withdrawel
         */
        Project project;
        /**
         * Flat type
         */
        String type;
        /**
         * Applicant's name
         */
        String name;

        /**
         * Instantiates a new Withdrawal entry.
         *
         * @param project Project for withdrrawel
         * @param type    Flat type
         * @param name    Applicant's name
         */
        WithdrawalEntry(Project project, String type, String name) {
			this.project = project;
			this.type = type;
			this.name = name;
    }
}

	private static void approveWithdrawals(Manager manager, Scanner scanner) {
		List<Project> allProjects = FileHandler.readProjectsFromCSV("ProjectList.csv");
		List<Project> managedProjects = allProjects.stream()
				.filter(p -> p.getManagerName().equalsIgnoreCase(manager.getName()))
				.collect(Collectors.toList());

		List<WithdrawalEntry> withdrawalEntries = new ArrayList<>();
		for (Project project : managedProjects) {
			project.getType1WithdrawalPending().forEach(name -> 
				withdrawalEntries.add(new WithdrawalEntry(project, "Type1", name)));
			project.getType2WithdrawalPending().forEach(name -> 
				withdrawalEntries.add(new WithdrawalEntry(project, "Type2", name)));
		}

		if (withdrawalEntries.isEmpty()) {
			System.out.println("No pending withdrawals to approve.");
			return;
		}

		System.out.println("=============================================================");
		System.out.println("Index\tProject Name\tType\tOwner");
		int index = 1;
		for (WithdrawalEntry entry : withdrawalEntries) {
			String type = entry.type.equals("Type1") ? entry.project.getType1() : entry.project.getType2();
			System.out.printf("%d\t%s\t%s\t%s%n", index++, entry.project.getProjectName(), type, entry.name);
		}
		System.out.println("=============================================================");

		System.out.print("Enter index of Owner to Withdraw (enter c to cancel): ");
		String input = scanner.nextLine().trim();
		if (input.equalsIgnoreCase("c")) return;

		try {
			int selectedIndex = Integer.parseInt(input) - 1;
			if (selectedIndex < 0 || selectedIndex >= withdrawalEntries.size()) {
				System.out.println("Invalid index.");
				return;
			}

			WithdrawalEntry entry = withdrawalEntries.get(selectedIndex);
			Project project = entry.project;
			String name = entry.name;

			if (entry.type.equals("Type1")) {

				List<String> newWithdrawalPending = new ArrayList<>(project.getType1WithdrawalPending());
				newWithdrawalPending.remove(name);
				project.setType1WithdrawalPending(newWithdrawalPending);

				List<String> newUnsuccessful = new ArrayList<>(project.getType1OwnerUnsuccessful());
				newUnsuccessful.add(name);
				project.setType1OwnerUnsuccessful(newUnsuccessful);
			} else {

				List<String> newWithdrawalPending = new ArrayList<>(project.getType2WithdrawalPending());
				newWithdrawalPending.remove(name);
				project.setType2WithdrawalPending(newWithdrawalPending);

				List<String> newUnsuccessful = new ArrayList<>(project.getType2OwnerUnsuccessful());
				newUnsuccessful.add(name);
				project.setType2OwnerUnsuccessful(newUnsuccessful);
			}

			boolean success = FileHandler.writeProjectsToCSV("ProjectList.csv", allProjects);
			if (success) {
				System.out.printf("Success, withdrew %s from %s! Welcome %s.%n",
						name, project.getProjectName(), manager.getName());
			} else {
				System.out.println("Failed to save changes.");
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid input.");
		}
	}

    private static void approveOwners(Manager manager, Scanner scanner) {
        List<Project> allProjects = FileHandler.readProjectsFromCSV("ProjectList.csv");
        List<Project> managedProjects = allProjects.stream()
                .filter(p -> p.getManagerName().equalsIgnoreCase(manager.getName()))
                .collect(Collectors.toList());

        List<PendingEntry> pendingEntries = new ArrayList<>();
        for (Project project : managedProjects) {
            project.getType1OwnerPending().forEach(owner ->
                    pendingEntries.add(new PendingEntry(project, "Type1", owner)));
            project.getType2OwnerPending().forEach(owner ->
                    pendingEntries.add(new PendingEntry(project, "Type2", owner)));
        }

        if (pendingEntries.isEmpty()) {
            System.out.println("No pending applications to approve.");
            return;
        }

        System.out.println("=============================================================");
        System.out.println("Pending Applications");
        System.out.println("Index\tProject Name\t\tType\tOwner");
        int index = 1;
        for (PendingEntry entry : pendingEntries) {
            String type = entry.type.equals("Type1") ? entry.project.getType1() : entry.project.getType2();
            System.out.printf("%d\t\t%s\t\t%s\t\t%s%n", index++, entry.project.getProjectName(), type, entry.ownerName);
        }
        System.out.println("=============================================================");

        while (true) {
            System.out.print("Enter number to approve (q to exit): ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("q")) break;

            try {
                int selectedIndex = Integer.parseInt(input) - 1;
                if (selectedIndex < 0 || selectedIndex >= pendingEntries.size()) {
                    System.out.println("Invalid index.");
                    continue;
                }

                PendingEntry entry = pendingEntries.get(selectedIndex);
                Project project = entry.project;
                String owner = entry.ownerName;

                if (entry.type.equals("Type1")) {
                    List<String> newPending = new ArrayList<>(project.getType1OwnerPending());
                    newPending.remove(owner);
                    project.setType1OwnerPending(newPending);

                    List<String> newSuccessful = new ArrayList<>(project.getType1OwnerSuccessful());
                    newSuccessful.add(owner);
                    project.setType1OwnerSuccessful(newSuccessful);
                } else {
                    List<String> newPending = new ArrayList<>(project.getType2OwnerPending());
                    newPending.remove(owner);
                    project.setType2OwnerPending(newPending);

                    List<String> newSuccessful = new ArrayList<>(project.getType2OwnerSuccessful());
                    newSuccessful.add(owner);
                    project.setType2OwnerSuccessful(newSuccessful);
                }

                if (FileHandler.writeProjectsToCSV("ProjectList.csv", allProjects)) {
                    System.out.printf("Success! %s approved for %s type %s.%n", owner,
                            project.getProjectName(), entry.type.equals("Type1") ? project.getType1() : project.getType2());

                    pendingEntries.clear();
                    managedProjects.forEach(p -> {
                        p.getType1OwnerPending().forEach(o -> pendingEntries.add(new PendingEntry(p, "Type1", o)));
                        p.getType2OwnerPending().forEach(o -> pendingEntries.add(new PendingEntry(p, "Type2", o)));
                    });

                    if (pendingEntries.isEmpty()) {
                        System.out.println("No more pending applications.");
                        break;
                    }

                    System.out.println("=============================================================");
                    System.out.println("Pending Applications");
                    System.out.println("Index\tProject Name\t\tType\tOwner");
                    index = 1;
                    for (PendingEntry e : pendingEntries) {
                        String type = e.type.equals("Type1") ? e.project.getType1() : e.project.getType2();
                        System.out.printf("%d\t\t%s\t\t%s\t\t%s%n", index++, e.project.getProjectName(), type, e.ownerName);
                    }
                    System.out.println("=============================================================");
                } else {
                    System.out.println("Failed to save changes.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number or 'q'.");
            }
        }
    }

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

    private static boolean isUserInOfficerOrPending(User user, List<Project> projects) {
        for (Project project : projects) {
            if (project.getOfficers().contains(user.getName()) || project.getOfficerPending().contains(user.getName())) {
                return true;
            }
        }
        return false;
    }

    private static boolean isUserInApplicationLists(User user, List<Project> projects) {
        for (Project project : projects) {
            List<String> type1Lists = new ArrayList<>();
            type1Lists.addAll(project.getType1OwnerPending());
            type1Lists.addAll(project.getType1OwnerUnsuccessful());
            type1Lists.addAll(project.getType1OwnerSuccessful());
            type1Lists.addAll(project.getType1OwnerBooked());
            List<String> type2Lists = new ArrayList<>();
            type2Lists.addAll(project.getType2OwnerPending());
            type2Lists.addAll(project.getType2OwnerUnsuccessful());
            type2Lists.addAll(project.getType2OwnerSuccessful());
            type2Lists.addAll(project.getType2OwnerBooked());
            if (type1Lists.contains(user.getName()) || type2Lists.contains(user.getName())) {
                return true;
            }
        }
        return false;
    }

    private static String findPendingApplicationMessage(User user, List<Project> projects) {
        for (Project p : projects) {
            if (p.getType1OwnerPending().contains(user.getName())) {
                return String.format("You cannot apply for any projects, you are PENDING for %s type %s", p.getProjectName(), p.getType1());
            }
            if (p.getType1OwnerSuccessful().contains(user.getName())) {
                return String.format("You cannot apply for any projects, you are SUCCESSFUL for %s type %s", p.getProjectName(), p.getType1());
            }
            if (p.getType1OwnerBooked().contains(user.getName())) {
                return String.format("You cannot apply for any projects, you are BOOKED for %s type %s", p.getProjectName(), p.getType1());
            }
			if (p.getType1WithdrawalPending().contains(user.getName())) {
                return String.format("You cannot apply or withdraw any projects, you are PENDING WITHDRAWAL for %s", p.getProjectName(), p.getType1());
            }
            if (p.getType2OwnerPending().contains(user.getName())) {
                return String.format("You cannot apply for any projects, you are PENDING for %s type %s", p.getProjectName(), p.getType2());
            }
            if (p.getType2OwnerSuccessful().contains(user.getName())) {
                return String.format("You cannot apply for any projects, you are SUCCESSFUL for %s type %s", p.getProjectName(), p.getType2());
            }
            if (p.getType2OwnerBooked().contains(user.getName())) {
                return String.format("You cannot apply for any projects, you are BOOKED for %s type %s", p.getProjectName(), p.getType2());
            }
			if (p.getType2WithdrawalPending().contains(user.getName())) {
                return String.format("You cannot apply or withdraw any projects, you are PENDING WITHDRAWAL for %s", p.getProjectName(), p.getType2());
            }
        }
        return "You cannot apply for any projects due to an existing application.";
    }

    private static List<Enquiry> readEnquiries() {
        List<Enquiry> enquiries = new ArrayList<>();
        File file = new File("EnquiryList.csv");
        if (!file.exists()) {
            return enquiries;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (parts.length < 4) continue;
                String name = parts[0].trim();
                String project = parts[1].trim();
                String enquiryStr = parts[2].trim();
                String reply = parts[3].trim();
                enquiries.add(new Enquiry(name, project, enquiryStr, reply));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return enquiries;
    }

    private static void writeEnquiry(String name, String project, String enquiry) {
        List<Enquiry> existingEnquiries = readEnquiries();
        existingEnquiries.add(new Enquiry(name, project, enquiry, ""));
        try (PrintWriter pw = new PrintWriter("EnquiryList.csv")) {
            pw.println("Name,Project,String,Reply");
            for (Enquiry e : existingEnquiries) {
                pw.printf("%s,%s,%s,%s%n", e.getName(), e.getProject(), e.getEnquiry(), e.getReply());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void handleWithdrawal(User user) {
        List<Project> allProjects = FileHandler.readProjectsFromCSV("ProjectList.csv");
        boolean anyChanges = false;

        for (Project project : allProjects) {
			boolean modifiedType1 = false;
			boolean modifiedType2 = false;
			modifiedType1 |= removeFromList(project.getType1OwnerPending(), user.getName(), project::setType1OwnerPending);
			modifiedType2 |= removeFromList(project.getType2OwnerPending(), user.getName(), project::setType2OwnerPending);
			modifiedType1 |= removeFromList(project.getType1OwnerUnsuccessful(), user.getName(), project::setType1OwnerUnsuccessful);
			modifiedType2 |= removeFromList(project.getType2OwnerUnsuccessful(), user.getName(), project::setType2OwnerUnsuccessful);
			modifiedType1 |= removeFromList(project.getType1OwnerSuccessful(), user.getName(), project::setType1OwnerSuccessful);
			modifiedType2 |= removeFromList(project.getType2OwnerSuccessful(), user.getName(), project::setType2OwnerSuccessful);
			modifiedType1 |= removeFromList(project.getType1OwnerBooked(), user.getName(), project::setType1OwnerBooked);
			modifiedType2 |= removeFromList(project.getType2OwnerBooked(), user.getName(), project::setType2OwnerBooked);			

			if (modifiedType1) {
				project.getType1WithdrawalPending().add(user.getName());
				anyChanges = true;
			}
			if (modifiedType2) {
				project.getType2WithdrawalPending().add(user.getName());
				anyChanges = true;
			}
        }

        if (anyChanges) {
            boolean success = FileHandler.writeProjectsToCSV("ProjectList.csv", allProjects);
            if (success) {
                System.out.printf("Withdrawal Request Sent! Welcome %s, %d, %s %s.%n",
                        user.getName(), user.getAge(), user.getMaritalStatus(),
                        user instanceof Officer ? "Officer" : "Applicant");
            } else {
                System.out.println("Error saving withdrawal.");
            }
        } else {
            System.out.println("No applications found to withdraw.");
        }
    }

	
    private static boolean removeFromList(List<String> list, String name, Consumer<List<String>> setter) {
        if (list.contains(name)) {
            List<String> newList = new ArrayList<>(list);
            newList.remove(name);
            setter.accept(newList);
            return true;
        }
        return false;
    }

    private static void handleEnquiry(User user, List<EligibleEntry> eligibleEntries, Scanner scanner) {
        System.out.print("Select project to enquire (enter index number): ");
        String indexInput = scanner.nextLine().trim();
        try {
            int index = Integer.parseInt(indexInput) - 1;
            if (index < 0 || index >= eligibleEntries.size()) {
                System.out.println("Invalid index.");
                return;
            }
            EligibleEntry entry = eligibleEntries.get(index);
            String projectName = entry.project.getProjectName();
            System.out.print("Enter enquiry for " + projectName + ": ");
            String enquiry = scanner.nextLine().trim();

            List<Enquiry> existingEnquiries = readEnquiries();
            boolean duplicate = existingEnquiries.stream()
                    .anyMatch(e -> e.getName().equals(user.getName())
                            && e.getProject().equals(projectName)
                            && e.getEnquiry().equals(enquiry));
            if (duplicate) {
                System.out.println("Error! Duplicate enquiry.");
            } else {
                writeEnquiry(user.getName(), projectName, enquiry);
                System.out.printf("Success Project Enquired! Welcome %s, %d, %s %s.%n",
                        user.getName(), user.getAge(), user.getMaritalStatus(),
                        user instanceof Officer ? "Officer" : "Applicant");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private static void viewEligibleProjects(User user, Scanner scanner) {
        List<Project> allProjects = FileHandler.readProjectsFromCSV("ProjectList.csv");
        List<Project> visibleProjects = allProjects.stream()
                .filter(Project::getVisibility)
                .collect(Collectors.toList());
        List<EligibleEntry> eligibleEntries = new ArrayList<>();
        String filter = user.getFilter();

        for (Project project : visibleProjects) {
            String type1 = project.getType1();
            if (isEligibleForRoomType(user, type1) && applyFilter(project, "Type1", filter)) {
                String displayStr = String.format("%s\t\t%s\t\t%d", project.getProjectName(), type1, project.getPriceType1());
                eligibleEntries.add(new EligibleEntry(project, "Type1", displayStr));
            }
            String type2 = project.getType2();
            if (isEligibleForRoomType(user, type2) && applyFilter(project, "Type2", filter)) {
                String displayStr = String.format("%s\t\t%s\t\t%d", project.getProjectName(), type2, project.getPriceType2());
                eligibleEntries.add(new EligibleEntry(project, "Type2", displayStr));
            }
        }

        System.out.printf("Current filter: %s%n", user.getFilter());

        boolean canApply = true;
        String pendingMessage = null;

        if (user instanceof Officer) {
            if (isUserInOfficerOrPending(user, allProjects)) {
                canApply = false;
                pendingMessage = "You cannot apply because you are assigned as an officer in a project.";
            }
        }

        if (canApply) {
            if (isUserInApplicationLists(user, allProjects)) {
                canApply = false;
                pendingMessage = findPendingApplicationMessage(user, allProjects);
            }
        }

        boolean canWithdraw = isUserInApplicationLists(user, allProjects) && !isUserInWithdrawalPending(user, allProjects);
        boolean canEnquire = true;

        if (eligibleEntries.isEmpty()) {
            System.out.printf("No Eligible Projects Welcome %s, %d, %s %s.%n",
                    user.getName(), user.getAge(), user.getMaritalStatus(),
                    user instanceof Officer ? "Officer" : "Applicant");
        } else {
            System.out.println("=============================================================");
            System.out.println("Project Name\t\tType\t\tPrice");
            for (EligibleEntry entry : eligibleEntries) {
                System.out.println(entry.displayString);
            }
            System.out.println("=============================================================");
            if (!canApply) {
                System.out.println(pendingMessage);
            }

            StringJoiner options = new StringJoiner(", ");
            if (canApply) {
                options.add("a to apply");
            }
            if (canWithdraw) {
                options.add("w to withdraw");
            }
            options.add("e to enquiry");
            options.add("c to cancel");
            System.out.print("Choice (" + options + "): ");
            String choice = scanner.nextLine().trim().toLowerCase();

            switch (choice) {
                case "a":
                    System.out.print("Select project to apply (enter index number): ");
                    String input = scanner.nextLine().trim();
                    try {
                        int selectedIndex = Integer.parseInt(input) - 1;
                        if (selectedIndex < 0 || selectedIndex >= eligibleEntries.size()) {
                            System.out.println("Invalid index.");
                            return;
                        }
                        EligibleEntry selectedEntry = eligibleEntries.get(selectedIndex);
                        Project selectedProject = selectedEntry.project;
                        String typeDesignation = selectedEntry.typeDesignation;

                        if (typeDesignation.equals("Type1")) {
                            List<String> newPending = new ArrayList<>(selectedProject.getType1OwnerPending());
                            newPending.add(user.getName());
                            selectedProject.setType1OwnerPending(newPending);
                        } else {
                            List<String> newPending = new ArrayList<>(selectedProject.getType2OwnerPending());
                            newPending.add(user.getName());
                            selectedProject.setType2OwnerPending(newPending);
                        }

                        boolean success = FileHandler.writeProjectsToCSV("ProjectList.csv", allProjects);
                        if (success) {
                            System.out.printf("Success Project Applied! Welcome %s, %d, %s %s.%n",
                                    user.getName(), user.getAge(), user.getMaritalStatus(),
                                    user instanceof Officer ? "Officer" : "Applicant");
                        } else {
                            System.out.println("Error applying to project.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input.");
                    }
                    break;
                case "w":
                    handleWithdrawal(user);
                    break;
                case "e":
                    handleEnquiry(user, eligibleEntries, scanner);
                    break;
                case "c":
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static boolean isUserInWithdrawalPending(User user, List<Project> projects) {
        for (Project p : projects) {
            if (p.getType1WithdrawalPending().contains(user.getName())) {
                return true;
            }
			if (p.getType2WithdrawalPending().contains(user.getName())) {
                return true;
            }
        }
        return false;
    }

    private static void createProject(Manager manager, Scanner scanner) {
        List<Project> projects = FileHandler.readProjectsFromCSV("ProjectList.csv");
        String projectName;
        boolean projectExists;

        do {
            System.out.print("Project Name: ");
            projectName = scanner.nextLine().trim();
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

        List<String> AssignedOfficers = new ArrayList<>();
        List<Officer> allOfficers = FileHandler.readUsersFromCSV("OfficerList.csv", Officer.class);
        List<String> validOfficerNames = allOfficers.stream()
                .map(Officer::getName)
                .collect(Collectors.toList());

        for (int i = 0; i < officerSlots; i++) {
            while (true) {
                System.out.printf("Officer #%d Name (Case Sensitive): ", i + 1);
                String officerName = scanner.nextLine().trim();

                if (validOfficerNames.contains(officerName)) {
                    AssignedOfficers.add(officerName);
                    break;
                } else {
                    System.out.println("Not a valid Officer Name, please check spelling and try again!");
                }
            }
        }

        Project newProject = new Project(projectName, neighborhood, "2-Room", numUnitsType1, priceType1, "3-Room", numUnitsType2, priceType2, openingDate, closingDate, manager.getName(), officerSlots, AssignedOfficers, false, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
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
            int selectedIndex = Integer.parseInt(input) - 1;
            if (selectedIndex < 0 || selectedIndex >= projects.size()) {
                System.out.println("Invalid index.");
                return;
            }
            Project project = projects.get(selectedIndex);
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

    private static void editProject(Manager manager, Scanner scanner) {
        List<Project> projects = FileHandler.readProjectsFromCSV("ProjectList.csv");
        if (projects.isEmpty()) {
            System.out.println("No projects to edit.");
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

        System.out.print("Enter number to edit: ");
        String input = scanner.nextLine().trim();
        try {
            int selectedIndex = Integer.parseInt(input) - 1;
            if (selectedIndex < 0 || selectedIndex >= projects.size()) {
                System.out.println("Invalid index.");
                return;
            }
            Project project = projects.get(selectedIndex);

            System.out.print("Neighbourhood (" + project.getNeighborhood() + ", empty to keep): ");
            String neighborhoodInput = scanner.nextLine().trim();
            if (!neighborhoodInput.isEmpty()) {
                project.setNeighborhood(neighborhoodInput);
            }

            int numUnitsType1 = project.getNumUnitsType1();
            boolean validUnitsType1 = false;
            do {
                System.out.print("Number of 2-room units (" + numUnitsType1 + ", empty to keep): ");
                String unitsInput = scanner.nextLine().trim();
                if (unitsInput.isEmpty()) {
                    validUnitsType1 = true;
                    break;
                }
                try {
                    int newUnits = Integer.parseInt(unitsInput);
                    if (newUnits < 0) {
                        System.out.println("Error! Number of units cannot be negative");
                        continue;
                    }
                    project.setNumUnitsType1(newUnits);
                    validUnitsType1 = true;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            } while (!validUnitsType1);

            int priceType1 = project.getPriceType1();
            boolean validPriceType1 = false;
            do {
                System.out.print("Selling price of 2-room units (" + priceType1 + ", empty to keep): ");
                String priceInput = scanner.nextLine().trim();
                if (priceInput.isEmpty()) {
                    validPriceType1 = true;
                    break;
                }
                try {
                    int newPrice = Integer.parseInt(priceInput);
                    if (newPrice <= 0) {
                        System.out.println("Error! Selling price must be positive");
                        continue;
                    }
                    project.setPriceType1(newPrice);
                    validPriceType1 = true;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            } while (!validPriceType1);

            int numUnitsType2 = project.getNumUnitsType2();
            boolean validUnitsType2 = false;
            do {
                System.out.print("Number of 3-room units (" + numUnitsType2 + ", empty to keep): ");
                String unitsInput = scanner.nextLine().trim();
                if (unitsInput.isEmpty()) {
                    validUnitsType2 = true;
                    break;
                }
                try {
                    int newUnits = Integer.parseInt(unitsInput);
                    if (newUnits < 0) {
                        System.out.println("Error! Number of units cannot be negative");
                        continue;
                    }
                    project.setNumUnitsType2(newUnits);
                    validUnitsType2 = true;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            } while (!validUnitsType2);

            int priceType2 = project.getPriceType2();
            boolean validPriceType2 = false;
            do {
                System.out.print("Selling price of 3-room units (" + priceType2 + ", empty to keep): ");
                String priceInput = scanner.nextLine().trim();
                if (priceInput.isEmpty()) {
                    validPriceType2 = true;
                    break;
                }
                try {
                    int newPrice = Integer.parseInt(priceInput);
                    if (newPrice <= 0) {
                        System.out.println("Error! Selling price must be positive");
                        continue;
                    }
                    project.setPriceType2(newPrice);
                    validPriceType2 = true;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            } while (!validPriceType2);

            String newOpeningDate = project.getOpeningDate();
            System.out.print("Application opening date (" + newOpeningDate + ", empty to keep): ");
            String openingInput = scanner.nextLine().trim();
            if (!openingInput.isEmpty()) {
                newOpeningDate = openingInput;
            }

            String newClosingDate = project.getClosingDate();
            boolean validDates = false;
            do {
                System.out.print("Application closing date (" + newClosingDate + ", empty to keep): ");
                String closingInput = scanner.nextLine().trim();
                if (!closingInput.isEmpty()) {
                    newClosingDate = closingInput;
                }

                if (newClosingDate.compareTo(newOpeningDate) < 0) {
                    System.out.println("Error! Closing date can't be earlier than opening date!");
                } else {
                    validDates = true;
                }
            } while (!validDates);

            project.setOpeningDate(newOpeningDate);
            project.setClosingDate(newClosingDate);

            int officerSlots = project.getOfficerSlots();
            int currentOfficersCount = project.getOfficers().size();
            boolean validSlots = false;
            do {
                System.out.print("HDB Officer Slots (" + officerSlots + ", empty to keep): ");
                String slotsInput = scanner.nextLine().trim();
                if (slotsInput.isEmpty()) {
                    validSlots = true;
                    break;
                }
                try {
                    int newSlots = Integer.parseInt(slotsInput);
                    if (newSlots < 0 || newSlots > 10) {
                        System.out.println("Error! Officer slots cannot be negative or more than 10");
                        continue;
                    }
                    if (newSlots < currentOfficersCount) {
                        System.out.println("Error! Officer slots is less than current amount of officers");
                        continue;
                    }
                    project.setOfficerSlots(newSlots);
                    validSlots = true;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            } while (!validSlots);

            boolean success = FileHandler.writeProjectsToCSV("ProjectList.csv", projects);
            if (success) {
                System.out.println("Success! Welcome " + manager.getName() + ", " + manager.getAge() + ", " + manager.getMaritalStatus() + " Manager.");
            } else {
                System.out.println("Error saving project changes.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }
       public static void main(String[] args) {
        File enquiryFile = new File("EnquiryList.csv");
        if (!enquiryFile.exists()) {
            try (PrintWriter pw = new PrintWriter(enquiryFile)) {
                pw.println("Name,Project,String,Reply");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Welcome to SDDA-grp3 system.");
            System.out.print("Login (enter q to quit, c for create): ");
            String nricInput = scanner.nextLine().trim();
            if (nricInput.equalsIgnoreCase("q")) {
                System.out.println("Goodbye!");
                break;
            } else if (nricInput.equalsIgnoreCase("c")) {
                createUserAccount(scanner);
                continue;
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
            String encodedInput = Base64.getEncoder().encodeToString(passwordInput.getBytes());
            if (!user.getPassword().equals(encodedInput)) {
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
                    System.out.println("5) Edit Project");
                    System.out.println("6) Approve Owners");
                    System.out.println("7) Reply to Enquiries (Any Project)"); //Testing
                    System.out.println("8) View Enquiries"); // Testing
					System.out.println("9) Approve Withdrawals");
					System.out.println("10) Change Filter");
					System.out.println("11) View all Projects");
                } else if (user instanceof Officer) {
                    System.out.println("3) View Eligible Projects"); //Testing
                    System.out.println("4) Reply to Enquiries (Assigned Projects)"); //Testing
                    System.out.println("5) View Enquiries"); //Testing
					System.out.println("6) Book Owner");
					System.out.println("7) Change Filter");
                } else if (user instanceof Applicant) {
                    System.out.println("3) View Eligible Projects"); // Testing
                    System.out.println("4) View Enquiries"); //Testing
					System.out.println("5) Change Filter");
                }
                System.out.print("Choice: ");
                String choice = scanner.nextLine().trim();
                switch (choice) {
                    case "1":
                        logout = true;
                        break;
					case "2":
                        String newPassword;
                        do {
                            System.out.print("Enter new password: ");
                            newPassword = scanner.nextLine().trim();
                            if (newPassword.isEmpty()) {
                                System.out.println("Password cannot be empty! Please Enter a new password: ");
                            }
                        } while (newPassword.isEmpty());
                        String encodedNewPassword = Base64.getEncoder().encodeToString(newPassword.getBytes());
                        user.setPassword(encodedNewPassword);
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
                        } else if (user instanceof Officer) { //Testing
                            replyToEnquiriesForOfficer((Officer) user);
                        } else if (user instanceof Applicant) { // Testing view for applicants
                            viewEnquiries(user);
                        } else {
                            System.out.println("Invalid Choice!");
                        }
                        break;
                    case "5":
                        if (user instanceof Manager) {
                            editProject((Manager) user, scanner);
                        } else if (user instanceof Officer) { //Testing view for officer
                            viewEnquiries(user);
						} else if (user instanceof Applicant) {
							changeFilter(user, scanner);
                        } else {
                            System.out.println("Invalid choice.");
                        }
                        break;
                    case "6":
                        if (user instanceof Manager) {
                            approveOwners((Manager) user, scanner);
                        } else if (user instanceof Officer) {
							handleBookOwner((Officer) user, scanner);
						} else {
							System.out.println("Invalid choice.");
						}
                        break;
                    case "7": //Testing reply
                        if (user instanceof Manager) {
                            replyToEnquiriesForManager((Manager) user);
                        } else if (user instanceof Officer) {
							changeFilter(user, scanner);
						}
                        break;
                    case "8": //Testing view
                        if (user instanceof Manager) {
                            viewEnquiries(user);;
                        }
					case "9":
						if (user instanceof Manager) {
							approveWithdrawals((Manager) user, scanner);
                        }
						break;
					case "10":
						if (user instanceof Manager) {
							changeFilter((Manager) user, scanner);
						}
						break;
					case "11":
						if (user instanceof Manager) {
							viewAllProjects((Manager) user, scanner);
						}
						break;
                    default:
                        System.out.println("Invalid choice.");
                }
            }
        }
        scanner.close();
    }

    // Testing (Manager Reply eveything)
    private static void replyToEnquiriesForManager(Manager manager) {
        List<Enquiry> allEnquiries = FileHandler.readEnquiries();
        List<Enquiry> pendingEnquiries = allEnquiries.stream()
                .filter(e -> e.getReply().isEmpty())
                .collect(Collectors.toList());

        handleEnquiryReply(manager, pendingEnquiries, allEnquiries);
    }

    // Officer (Officer reply assigned)
    private static void replyToEnquiriesForOfficer(Officer officer) {
        List<Project> allProjects = FileHandler.readProjectsFromCSV("ProjectList.csv");
        List<String> assignedProjects = allProjects.stream()
                .filter(p -> p.getOfficers().contains(officer.getName()))
                .map(Project::getProjectName)
                .collect(Collectors.toList());

        List<Enquiry> allEnquiries = FileHandler.readEnquiries();
        List<Enquiry> pendingEnquiries = allEnquiries.stream()
                .filter(e -> e.getReply().isEmpty() && assignedProjects.contains(e.getProject()))
                .collect(Collectors.toList());

        handleEnquiryReply(officer, pendingEnquiries, allEnquiries);
    }

    private static void handleEnquiryReply(User user, List<Enquiry> pendingEnquiries, List<Enquiry> allEnquiries) {
        if (pendingEnquiries.isEmpty()) {
            System.out.println("No pending enquiries for your projects.");
            return;
        }

        System.out.println("=============================================================");
        System.out.println("Pending Enquiries");
        System.out.println("Index\tProject\t\tEnquiry");
        for (int i = 0; i < pendingEnquiries.size(); i++) {
            Enquiry e = pendingEnquiries.get(i);
            System.out.printf("%d\t%s\t\t%s%n", i + 1, e.getProject(), e.getEnquiry());
        }
        System.out.println("=============================================================");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter enquiry index to reply (q to exit): ");
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("q")) return;

        try {
            int index = Integer.parseInt(input) - 1;
            if (index < 0 || index >= pendingEnquiries.size()) {
                System.out.println("Invalid index.");
                return;
            }

            Enquiry selectedEnquiry = pendingEnquiries.get(index);
            System.out.print("Enter your reply: ");
            String reply = scanner.nextLine().trim();

            selectedEnquiry.setReply(reply);
            boolean success = FileHandler.writeEnquiries(allEnquiries);

            if (success) {
                System.out.printf("Reply saved! Welcome %s.%n", user.getName());
            } else {
                System.out.println("Failed to save reply.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private static void viewEnquiries(User user) {
        List<Enquiry> allEnquiries = FileHandler.readEnquiries();
        List<Enquiry> filteredEnquiries = new ArrayList<>();

        // Testing, user see own, officer see assigned, manager see all
        if (user instanceof Applicant) {
            filteredEnquiries = allEnquiries.stream()
                    .filter(e -> e.getName().equalsIgnoreCase(user.getName()))
                    .collect(Collectors.toList());
        } else if (user instanceof Officer) {
            List<Project> projects = FileHandler.readProjectsFromCSV("ProjectList.csv");
            List<String> assignedProjects = projects.stream()
                    .filter(p -> p.getOfficers().contains(user.getName()))
                    .map(Project::getProjectName)
                    .collect(Collectors.toList());

            filteredEnquiries = allEnquiries.stream()
                    .filter(e -> assignedProjects.contains(e.getProject()))
                    .collect(Collectors.toList());
        } else if (user instanceof Manager) {
            filteredEnquiries = allEnquiries;
        }

        if (filteredEnquiries.isEmpty()) {
            System.out.println("No enquiries found.");
            return;
        }

        System.out.println("=============================================================");
        System.out.println("Enquiries");
        System.out.println("Project\t\tEnquiry\t\tReply");
        for (Enquiry e : filteredEnquiries) {
            System.out.printf("%s\t\t%s\t\t%s%n",
                    e.getProject(),
                    e.getEnquiry(),
                    e.getReply().isEmpty() ? "[Pending]" : e.getReply());
        }
        System.out.println("=============================================================");
    }
}
