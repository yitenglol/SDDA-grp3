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
	
	private static void viewEligibleProjects(User user) {
        List<String[]> eligibleEntries = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("ProjectList.csv"))) {
            String line;
            boolean headerSkipped = false;
            while ((line = br.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }
                line = line.trim();
                if (line.isEmpty()) continue;

                int lastComma = line.lastIndexOf(',');
                if (lastComma == -1) continue;
                String visibilityStr = line.substring(lastComma + 1).trim();
                if (!visibilityStr.equalsIgnoreCase("true")) {
                    continue;
                }

                String[] parts = line.split(",", -1);
                if (parts.length < 14) {
                    continue;
                }

                String projectName = parts[0].trim();
                String type1 = parts[2].trim();
                String priceStr1 = parts[4].trim();
                String type2 = parts[5].trim();
                String priceStr2 = parts[7].trim();

                try {
                    int price1 = Integer.parseInt(priceStr1);
                    int price2 = Integer.parseInt(priceStr2);

                    if (isEligibleForType(user, type1)) {
                        eligibleEntries.add(new String[]{projectName, type1, String.valueOf(price1)});
                    }
                    if (isEligibleForType(user, type2)) {
                        eligibleEntries.add(new String[]{projectName, type2, String.valueOf(price2)});
                    }
                } catch (NumberFormatException e) {
                    continue;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading ProjectList.csv");
            return;
        }

        if (eligibleEntries.isEmpty()) {
            System.out.printf("No Eligible Projects Welcome %s, %d, %s %s.%n",
                    user.getName(), user.getAge(), user.getMaritalStatus(), user.getRole());
        } else {
            System.out.println("=============================================================");
            System.out.println("Project Name\t\tType\t\tPrice");
            for (String[] entry : eligibleEntries) {
                System.out.printf("%s\t\t%s\t\t%s%n", entry[0], entry[1], entry[2]);
            }
            System.out.println("=============================================================");
            System.out.printf("Welcome %s, %d, %s %s.%n",
                    user.getName(), user.getAge(), user.getMaritalStatus(), user.getRole());
        }
    }

    private static boolean isEligibleForType(User user, String type) {
        String maritalStatus = user.getMaritalStatus();
        int age = user.getAge();

        if (type.equals("2-Room")) {
            return (maritalStatus.equalsIgnoreCase("Married") && age >= 21) ||
                   (maritalStatus.equalsIgnoreCase("Single") && age >= 35);
        } else if (type.equals("3-Room")) {
            return maritalStatus.equalsIgnoreCase("Married") && age >= 21;
        }
        return false;
    }


	private static void editProject(String managerName, Scanner scanner) {
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
		System.out.println("=============================================================");
		System.out.println("Index\tProject Name\t\tVisibility");
		for (int i = 0; i < dataLines.size(); i++) {
			String[] parts = dataLines.get(i).split(",", -1);
			String projectName = parts[0];
			String visibility = parts.length > 0 ? parts[parts.length - 1].trim() : "false";
			System.out.printf("%d\t\t%s\t\t%s%n", i + 1, projectName, visibility);
		}
		System.out.println("=============================================================");

		System.out.print("Enter number to edit: ");
		int selectedIndex;
		try {
			selectedIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
			if (selectedIndex < 0 || selectedIndex >= dataLines.size()) {
				System.out.println("Invalid index.");
				return;
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid input.");
			return;
		}

		String selectedLine = dataLines.get(selectedIndex);
		String[] parts = selectedLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
		String projectName = parts[0];
		String currentNeighborhood = parts[1];
		int currentNumType1 = Integer.parseInt(parts[3]);
		int currentPriceType1 = Integer.parseInt(parts[4]);
		int currentNumType2 = Integer.parseInt(parts[6]);
		int currentPriceType2 = Integer.parseInt(parts[7]);
		String currentOpeningDate = parts[8];
		String currentClosingDate = parts[9];
		int currentOfficerSlots = Integer.parseInt(parts[11]);
		String currentOfficers = parts.length > 12 ? parts[12].replaceAll("^\"|\"$", "") : "";
		String officersField = currentOfficers.contains(",") ? "\"" + currentOfficers + "\"" : currentOfficers;
		String visibility = parts[parts.length - 1];
		int currentOfficerCount = currentOfficers.isEmpty() ? 0 : currentOfficers.split(",").length;

		System.out.printf("Neighbourhood (%s, empty to keep): ", currentNeighborhood);
		String newNeighborhood = scanner.nextLine().trim();
		if (newNeighborhood.isEmpty()) newNeighborhood = currentNeighborhood;

		currentNumType1 = getUpdatedValue(scanner, "Number of 2-room units", currentNumType1, true);

		currentPriceType1 = getUpdatedValue(scanner, "Selling price of 2-room units", currentPriceType1, false);

		currentNumType2 = getUpdatedValue(scanner, "Number of 3-room units", currentNumType2, true);

		currentPriceType2 = getUpdatedValue(scanner, "Selling price of 3-room units", currentPriceType2, false);

		LocalDate newOpeningDate = getDateInput(scanner, "Application opening date", currentOpeningDate);

		LocalDate newClosingDate = getClosingDateInput(scanner, "Application closing date", currentClosingDate, newOpeningDate);

		currentOfficerSlots = getOfficerSlotsInput(scanner, currentOfficerSlots, currentOfficerCount);

		String updatedLine = String.format("%s,%s,2-Room,%d,%d,3-Room,%d,%d,%s,%s,%s,%d,%s,%s",
        projectName, newNeighborhood, currentNumType1, currentPriceType1, currentNumType2, currentPriceType2,
        newOpeningDate, newClosingDate, parts[10], currentOfficerSlots, officersField, visibility);

		projectLines.set(selectedIndex + 1, updatedLine);

		try (PrintWriter pw = new PrintWriter(new FileWriter("ProjectList.csv"))) {
			projectLines.forEach(pw::println);
			System.out.printf("Success! Welcome %s.%n", managerName);
		} catch (IOException e) {
			System.err.println("Error writing to ProjectList.csv");
		}
	}

	private static int getUpdatedValue(Scanner scanner, String prompt, int currentValue, boolean allowZero) {
		while (true) {
			System.out.printf("%s (%d, empty to keep): ", prompt, currentValue);
			String input = scanner.nextLine().trim();
			if (input.isEmpty()) return currentValue;

			try {
				int value = Integer.parseInt(input);
				if (allowZero && value < 0) {
					System.out.println("Error! Number cannot be negative.");
					continue;
				}
				if (!allowZero && value <= 0) {
					System.out.println("Error! Value must be positive.");
					continue;
				}
				return value;
			} catch (NumberFormatException e) {
				System.out.println("Error! Please enter a valid number.");
			}
		}
	}

	private static LocalDate getDateInput(Scanner scanner, String prompt, String currentDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
		while (true) {
			System.out.printf("%s (%s, empty to keep): ", prompt, currentDate);
			String input = scanner.nextLine().trim();
			if (input.isEmpty()) return LocalDate.parse(currentDate, formatter);

			try {
				return LocalDate.parse(input, formatter);
			} catch (DateTimeParseException e) {
				System.out.println("Error! Invalid date format. Use YYYY-MM-DD.");
			}
		}
	}

	private static LocalDate getClosingDateInput(Scanner scanner, String prompt, String currentDate, LocalDate openingDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
		while (true) {
			System.out.printf("%s (%s, empty to keep): ", prompt, currentDate);
			String input = scanner.nextLine().trim();
			if (input.isEmpty()) return LocalDate.parse(currentDate, formatter);

			try {
				LocalDate date = LocalDate.parse(input, formatter);
				if (date.isBefore(openingDate)) {
					System.out.println("Error! Closing date can't be earlier than opening date!");
					continue;
				}
				return date;
			} catch (DateTimeParseException e) {
				System.out.println("Error! Invalid date format. Use YYYY-MM-DD.");
			}
		}
	}

	private static int getOfficerSlotsInput(Scanner scanner, int currentSlots, int currentOfficers) {
		while (true) {
			System.out.printf("HDB Officer Slots (%d, empty to keep): ", currentSlots);
			String input = scanner.nextLine().trim();
			if (input.isEmpty()) return currentSlots;

			try {
				int slots = Integer.parseInt(input);
				if (slots < currentOfficers) {
					System.out.println("Error! Slots cannot be less than current officers assigned.");
					continue;
				}
				if (slots < 0 || slots > 10) {
					System.out.println("Error! Slots must be between 0 and 10.");
					continue;
				}
				return slots;
			} catch (NumberFormatException e) {
				System.out.println("Error! Please enter a valid number.");
			}
		}
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
					System.out.println("5) Edit Project");
				} else if (currentUser.getRole().equals("Applicant") || currentUser.getRole().equals("Officer")) {
					System.out.println("3) View Eligible Projects");
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
						if (currentUser.getRole().equals("Manager")) {
							createProject(currentUser.getName(), scanner);
						} else if (currentUser.getRole().equals("Applicant") || currentUser.getRole().equals("Officer")) {
							viewEligibleProjects(currentUser);
						} else {
							System.out.println("Invalid choice.");
						}
						break;
					case "4":
						if (currentUser.getRole().equals("Manager")) {
							toggleVisibility(currentUser.getName(), scanner);
						} else {
							System.out.println("Invalid choice.");
						}
						break;
					case "5":
						if (!currentUser.getRole().equals("Manager")) {
							System.out.println("Invalid choice.");
							break;
						}
						editProject(currentUser.getName(), scanner);
						break;
					default:
						System.out.println("Invalid choice.");
				}
			}
        }
        scanner.close();
    }
}