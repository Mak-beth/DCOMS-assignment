package hrm.client;

import hrm.shared.Employee;
import hrm.shared.HRMService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class HRMClient {
    private static HRMService hrmServer;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            hrmServer = (HRMService) registry.lookup("CrestHRMService");
            
            System.out.println("=========================================");
            System.out.println("  Connected to Crest Solutions HRM!  ");
            System.out.println("=========================================");

            boolean running = true;
            while (running) {
                System.out.println("\n=== MAIN MENU ===");
                System.out.println("1. HR Staff Portal");
                System.out.println("2. Employee Portal");
                System.out.println("3. Shut Down Client");
                System.out.print("Select an option: ");
                
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        hrPortal();
                        break;
                    case "2":
                        employeePortal();
                        break;
                    case "3":
                        running = false;
                        System.out.println("Disconnecting from System. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        } catch (Exception e) {
            System.err.println("Client Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void hrPortal() throws Exception {
        boolean hrLoggedIn = true;
        
        while (hrLoggedIn) {
            System.out.println("\n--- HR PORTAL ---");
            System.out.println("1. Register New Employee");
            System.out.println("2. Generate Yearly Report");
            System.out.println("3. Log Out (Return to Main Menu)");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter First Name: ");
                    String fName = scanner.nextLine();
                    System.out.print("Enter Last Name: ");
                    String lName = scanner.nextLine();
                    System.out.print("Enter IC/Passport: ");
                    String ic = scanner.nextLine();
                    
                    String newId = hrmServer.registerEmployee(fName, lName, ic);
                    System.out.println("\nSUCCESS: Employee registered with ID: " + newId);
                    break;
                case "2":
                    System.out.print("Enter Employee ID: ");
                    String empId = scanner.nextLine();
                    System.out.println("\n" + hrmServer.generateYearlyReport(empId));
                    break;
                case "3":
                    hrLoggedIn = false;
                    System.out.println("Logging out of HR Portal...");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void employeePortal() throws Exception {
        System.out.print("\nEnter your Employee ID to login: ");
        String empId = scanner.nextLine();
        
        Employee emp = hrmServer.getEmployeeDetails(empId);
        if (emp == null) {
            System.out.println("ERROR: Employee not found!");
            return;
        }

        boolean empLoggedIn = true;
        
        while (empLoggedIn) {
            System.out.println("\n--- Welcome, " + emp.getFirstName() + " ---");
            System.out.println("1. Update Personal Details");
            System.out.println("2. Check Leave Balance");
            System.out.println("3. Apply for Leave");
            System.out.println("4. View Leave History & Status");
            System.out.println("5. Log Out (Return to Main Menu)");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter new personal details (Address, Phone, etc.): ");
                    String details = scanner.nextLine();
                    if (hrmServer.updatePersonalDetails(empId, details)) {
                        System.out.println("Details updated successfully!");
                    }
                    break;
                case "2":
                    System.out.println("Current Leave Balance: " + hrmServer.checkLeaveBalance(empId) + " days.");
                    break;
                case "3":
                    System.out.print("How many days of leave are you applying for? ");
                    int days = Integer.parseInt(scanner.nextLine());
                    if (hrmServer.applyForLeave(empId, days)) {
                        System.out.println("Leave approved! Deducted " + days + " days.");
                    } else {
                        System.out.println("Leave rejected! Insufficient balance.");
                    }
                    break;
                case "4":
                    System.out.println("\n--- Leave History ---");
                    List<String> history = hrmServer.getLeaveHistory(empId);
                    if (history.isEmpty()) {
                        System.out.println("No leave history found.");
                    } else {
                        for (String record : history) {
                            System.out.println("- " + record);
                        }
                    }
                    System.out.println("\n" + hrmServer.checkLeaveStatus(empId));
                    break;
                case "5":
                    empLoggedIn = false;
                    System.out.println("Logging out of Employee Portal...");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}