import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class User {
    private String name;
    private String nric;
    private int age;
    private String maritalStatus;
    private String password;
    private String role;

    public User(String name, String nric, int age, String maritalStatus, String password, String role) {
        this.name = name;
        this.nric = nric;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.password = password;
        this.role = role;
    }

    public String getName() { return name; }
    public String getNric() { return nric; }
    public int getAge() { return age; }
    public String getMaritalStatus() { return maritalStatus; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    public void setPassword(String password) { this.password = password; }

    public String toCSVString() {
        return String.format("%s,%s,%d,%s,%s", name, nric, age, maritalStatus, password);
    }
}

public class SDDASystem {
    private static List<User> allUsers = new ArrayList<>();

private static List<User> readUsersFromCSV(String filename, String role) {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean headerSkipped = false;
            while ((line = br.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length < 5) continue;
                String name = parts[0].trim();
                String nric = parts[1].trim();
                int age = Integer.parseInt(parts[2].trim());
                String maritalStatus = parts[3].trim();
                String password = parts[4].trim();
                users.add(new User(name, nric, age, maritalStatus, password, role));
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filename);
        }
        return users;
    }

    private static void updateUserPassword(User user) {
        String filename = "";
        switch (user.getRole()) {
            case "Officer":
                filename = "OfficerList.csv";
                break;
            case "Manager":
                filename = "ManagerList.csv";
                break;
            case "Applicant":
                filename = "ApplicantList.csv";
                break;
            default:
                return;
        }

        List<User> usersInRole = readUsersFromCSV(filename, user.getRole());
        for (User u : usersInRole) {
            if (u.getNric().equals(user.getNric())) {
                u.setPassword(user.getPassword());
                break;
            }
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println("Name,NRIC,Age,Marital Status,Password");
            for (User u : usersInRole) {
                pw.println(u.toCSVString());
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filename);
        }
    }

    private static void createProject(String managerName, Scanner scanner) {
        List<String> existingProjects = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("ProjectList.csv"))) {
            String line;
            boolean headerSkipped = false;
            while ((line = br.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }
                String[] parts = line.split(",", -1);
                if (parts.length > 0) {
                    existingProjects.add(parts[0].trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading ProjectList.csv");
            return;
        }

        String projectName = null;
        while (projectName == null) {
            System.out.print("Project Name: ");
            projectName = scanner.nextLine().trim();
            if (projectName.isEmpty()) {
                System.out.println("Error! Project name cannot be empty.");
                projectName = null;
            } else if (existingProjects.contains(projectName)) {
                System.out.println("Error! Project already exists");
                projectName = null;
            }
        }

        System.out.print("Neighbourhood: ");
        String neighborhood = scanner.nextLine().trim();

        int num2Room = -1;
        while (num2Room < 0) {
            System.out.print("Number of 2-room units: ");
            String input = scanner.nextLine().trim();
            try {
                num2Room = Integer.parseInt(input);
                if (num2Room < 0) {
                    System.out.println("Error! Number of units cannot be negative");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error! Please enter a valid number.");
            }
        }

        int price2Room = 0;
        while (price2Room <= 0) {
            System.out.print("Selling price of 2-room units: ");
            String input = scanner.nextLine().trim();
            try {
                price2Room = Integer.parseInt(input);
                if (price2Room <= 0) {
                    System.out.println("Error! Selling price must be positive.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error! Please enter a valid number.");
            }
        }

        int num3Room = -1;
        while (num3Room < 0) {
            System.out.print("Number of 3-room units: ");
            String input = scanner.nextLine().trim();
            try {
                num3Room = Integer.parseInt(input);
                if (num3Room < 0) {
                    System.out.println("Error! Number of units cannot be negative");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error! Please enter a valid number.");
            }
        }

        int price3Room = 0;
        while (price3Room <= 0) {
            System.out.print("Selling price of 3-room units: ");
            String input = scanner.nextLine().trim();
            try {
                price3Room = Integer.parseInt(input);
                if (price3Room <= 0) {
                    System.out.println("Error! Selling price must be positive.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error! Please enter a valid number.");
            }
        }

        LocalDate openingDate = null;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (openingDate == null) {
            System.out.print("Application opening date: ");
            String input = scanner.nextLine().trim();
            try {
                openingDate = LocalDate.parse(input, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Error! Invalid date format. Use YYYY-MM-DD.");
            }
        }

        LocalDate closingDate = null;
        while (closingDate == null) {
            System.out.print("Application closing date: ");
            String input = scanner.nextLine().trim();
            try {
                closingDate = LocalDate.parse(input, dateFormatter);
                if (closingDate.isBefore(openingDate)) {
                    System.out.println("Error! Closing date can't be earlier than opening date!");
                    closingDate = null;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Error! Invalid date format. Use YYYY-MM-DD.");
            }
        }

        int officerSlots = -1;
        while (officerSlots < 0 || officerSlots > 10) {
            System.out.print("HDB Officer Slots: ");
            String input = scanner.nextLine().trim();
            try {
                officerSlots = Integer.parseInt(input);
                if (officerSlots < 0 || officerSlots > 10) {
                    System.out.println("Error! Officer slots cannot be negative or more than 10");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error! Please enter a valid number.");
            }
        }

		String newLine = String.format("%s,%s,2-Room,%d,%d,3-Room,%d,%d,%s,%s,%s,%d,",
		projectName, neighborhood, num2Room, price2Room, num3Room, price3Room,
		openingDate.format(dateFormatter), closingDate.format(dateFormatter),
		managerName, officerSlots);

		File projectFile = new File("ProjectList.csv");
		boolean needsNewline = false;

		if (projectFile.exists() && projectFile.length() > 0) {
			try (RandomAccessFile raf = new RandomAccessFile(projectFile, "r")) {
				long fileLength = raf.length();
				if (fileLength > 0) {
					raf.seek(fileLength - 1);
					byte lastByte = raf.readByte();
					if (lastByte != '\n') {
						needsNewline = true;
					}
				}
			} catch (IOException e) {
				System.err.println("Error checking file for newline: " + e.getMessage());
			}
		}

		try (PrintWriter pw = new PrintWriter(new FileWriter("ProjectList.csv", true))) {
			if (needsNewline) {
				pw.println(); // Ensure new project starts on a new line
			}
			pw.println(newLine);
			System.out.printf("Project Created! Welcome %s.%n", managerName);
		} catch (IOException e) {
			System.err.println("Error writing to ProjectList.csv");
		}
    }
	
	    private static void ensureVisibilityColumnExists() {
        File projectFile = new File("ProjectList.csv");
        List<String> lines = new ArrayList<>();
        boolean modified = false;

        try (BufferedReader br = new BufferedReader(new FileReader(projectFile))) {
            String header = br.readLine();
            if (header == null) {
                header = "Project Name,Neighborhood,Type 1,Number of units for Type 1,Selling price for Type 1,Type 2,Number of units for Type 2,Selling price for Type 2,Application opening date,Application closing date,Manager,Officer Slot,Officer,Visibility";
                lines.add(header);
                modified = true;
            } else {
                if (!header.contains("Visibility")) {
                    header += ",Visibility";
                    modified = true;
                }
                lines.add(header);
            }

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                int lastComma = line.lastIndexOf(',');
                if (lastComma == -1 || (!line.substring(lastComma + 1).trim().equalsIgnoreCase("true") && !line.substring(lastComma + 1).trim().equalsIgnoreCase("false"))) {
                    line += ",false";
                    modified = true;
                }
                lines.add(line);
            }
        } catch (IOException e) {
            try (PrintWriter pw = new PrintWriter(projectFile)) {
                String header = "Project Name,Neighborhood,Type 1,Number of units for Type 1,Selling price for Type 1,Type 2,Number of units for Type 2,Selling price for Type 2,Application opening date,Application closing date,Manager,Officer Slot,Officer,Visibility";
                pw.println(header);
            } catch (IOException ex) {
                System.err.println("Error creating ProjectList.csv: " + ex.getMessage());
            }
            return;
        }

        if (modified) {
            try (PrintWriter pw = new PrintWriter(projectFile)) {
                for (String line : lines) {
                    pw.println(line);
                }
            } catch (IOException e) {
                System.err.println("Error writing updated ProjectList.csv: " + e.getMessage());
            }
        }
    }

    private static void toggleVisibility(String managerName, Scanner scanner) {
        List<String> projectLines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("ProjectList.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                projectLines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading ProjectList.csv");
            return;
        }

        if (projectLines.size() < 2) {
            System.out.println("No projects available.");
            return;
        }

        List<String> dataLines = projectLines.subList(1, projectLines.size());
        List<String> projectNames = new ArrayList<>();
        List<Boolean> visibilities = new ArrayList<>();

        for (String line : dataLines) {
            int lastComma = line.lastIndexOf(',');
            if (lastComma == -1) continue;

            String projectName = line.substring(0, line.indexOf(',')).trim();
            String visibilityStr = line.substring(lastComma + 1).trim();
            boolean visibility = Boolean.parseBoolean(visibilityStr);
            projectNames.add(projectName);
            visibilities.add(visibility);
        }

        System.out.println("=============================================================");
        System.out.println("Index\tProject Name\t\tVisibility");
        for (int i = 0; i < projectNames.size(); i++) {
            System.out.printf("%d\t\t%s\t\t%s%n", i + 1, projectNames.get(i), visibilities.get(i));
        }
        System.out.println("=============================================================");

        System.out.print("Enter number to toggle: ");
        int selectedIndex;
        try {
            selectedIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (selectedIndex < 0 || selectedIndex >= projectNames.size()) {
                System.out.println("Invalid index.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        String selectedLine = dataLines.get(selectedIndex);
        int lastComma = selectedLine.lastIndexOf(',');
        String newVisibility = !visibilities.get(selectedIndex) ? "true" : "false";
        String updatedLine = selectedLine.substring(0, lastComma) + "," + newVisibility;

        dataLines.set(selectedIndex, updatedLine);

        try (PrintWriter pw = new PrintWriter(new FileWriter("ProjectList.csv"))) {
            for (String line : projectLines) {
                pw.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error writing to ProjectList.csv");
            return;
        }

        System.out.printf("Success, %s is now %s! Welcome %s.%n",
                projectNames.get(selectedIndex), newVisibility, managerName);
    }

    public static void main(String[] args) {
		ensureVisibilityColumnExists();
        allUsers.addAll(readUsersFromCSV("OfficerList.csv", "Officer"));
        allUsers.addAll(readUsersFromCSV("ManagerList.csv", "Manager"));
        allUsers.addAll(readUsersFromCSV("ApplicantList.csv", "Applicant"));

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Welcome to SDDA-grp3 system.");
            System.out.print("Login (enter q to quit): ");
            String nricInput = scanner.nextLine().trim();

            if (nricInput.equalsIgnoreCase("q")) {
                System.out.println("Goodbye!");
                break;
            }

            if (!nricInput.matches("^[ST]\\d{7}[A-Z]$")) {
                System.out.println("Invalid format!");
                continue;
            }

            User currentUser = null;
            for (User user : allUsers) {
                if (user.getNric().equals(nricInput)) {
                    currentUser = user;
                    break;
                }
            }

            if (currentUser == null) {
                System.out.println("User not exist!");
                continue;
            }

            System.out.print("Password: ");
            String passwordInput = scanner.nextLine().trim();

            if (!currentUser.getPassword().equals(passwordInput)) {
                System.out.println("Wrong password!");
                continue;
            }

            System.out.printf("Success! Welcome %s, %d, %s %s.%n",
                    currentUser.getName(), currentUser.getAge(),
                    currentUser.getMaritalStatus(), currentUser.getRole());

            boolean loggedIn = true;
            while (loggedIn) {
                System.out.println("1) Logout");
                System.out.println("2) Change Password");
				if (currentUser.getRole().equals("Manager")) {
					System.out.println("3) Create Project");
					System.out.println("4) Toggle Visibility");
				}
                System.out.print("Choice: ");
                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1":
                        loggedIn = false;
                        break;
                    case "2":
                        System.out.print("Enter new password: ");
                        String newPassword = scanner.nextLine().trim();
                        currentUser.setPassword(newPassword);
                        updateUserPassword(currentUser);
                        System.out.printf("Password change success! Welcome %s, %d, %s %s.%n",
                                currentUser.getName(), currentUser.getAge(),
                                currentUser.getMaritalStatus(), currentUser.getRole());
                        break;
                    case "3":
                        if (!currentUser.getRole().equals("Manager")) {
                            System.out.println("Invalid choice.");
                            break;
                        }
                        createProject(currentUser.getName(), scanner);
                        break;
					case "4":
						if (!currentUser.getRole().equals("Manager")) {
							System.out.println("Invalid choice.");
							break;
						}
						toggleVisibility(currentUser.getName(), scanner);
						break;
                    default:
                        System.out.println("Invalid choice.");
                }
            }
        }
        scanner.close();
    }
}