package hrm.server;

import hrm.shared.Employee;
import hrm.shared.HRMService;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HRMServerImpl extends UnicastRemoteObject implements HRMService {
    
    private Map<String, Employee> employeeDatabase;
    private int employeeCounter = 1000;
    private final String DATA_FILE = "employee_data.dat";

    public HRMServerImpl() throws RemoteException {
        super();
        loadDataFromFile();
    }
    
    private void saveDataToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(employeeDatabase);
            System.out.println("Server Log: Data saved to file successfully.");
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadDataFromFile() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                employeeDatabase = (Map<String, Employee>) ois.readObject();
                employeeCounter = 1000 + employeeDatabase.size(); 
                System.out.println("Server Log: Data loaded from file.");
            } catch (Exception e) {
                System.err.println("Error loading data: " + e.getMessage());
            }
        } else {
            employeeDatabase = new HashMap<>();
            System.out.println("Server Log: No existing data found. Starting fresh.");
        }
    }

    @Override
    public String registerEmployee(String firstName, String lastName, String icNumber) throws RemoteException {
        String newId = "EMP" + (++employeeCounter);
        Employee newEmp = new Employee(newId, firstName, lastName, icNumber);
        employeeDatabase.put(newId, newEmp);
        System.out.println("Server Log: Registered new employee " + newId);
        saveDataToFile();
        return newId;
    }

    @Override
    public Employee getEmployeeDetails(String employeeId) throws RemoteException {
        return employeeDatabase.get(employeeId);
    }

    @Override
    public boolean applyForLeave(String employeeId, int days) throws RemoteException {
        Employee emp = employeeDatabase.get(employeeId);
        if (emp != null && emp.getLeaveBalance() >= days) {
            emp.setLeaveBalance(emp.getLeaveBalance() - days);
            emp.addLeaveHistory("Applied for " + days + " days. Status: APPROVED");
            saveDataToFile(); // Save after modifying data
            return true;
        }
        if (emp != null) {
             emp.addLeaveHistory("Applied for " + days + " days. Status: REJECTED (Insufficient Balance)");
             saveDataToFile();
        }
        return false;
    }

    @Override
    public int checkLeaveBalance(String employeeId) throws RemoteException {
        Employee emp = employeeDatabase.get(employeeId);
        if (emp != null) {
            return emp.getLeaveBalance();
        }
        throw new RemoteException("Employee not found.");
    }

    @Override
    public String generateYearlyReport(String employeeId) throws RemoteException {
        Employee emp = employeeDatabase.get(employeeId);
        if (emp != null) {
            return "--- Yearly Report for " + emp.getFirstName() + " ---\n" +
                   "Remaining Leave Balance: " + emp.getLeaveBalance() + " days.";
        }
        return "Employee not found.";
    }
    
    @Override
    public boolean updatePersonalDetails(String employeeId, String newDetails) throws RemoteException {
        Employee emp = employeeDatabase.get(employeeId);
        if (emp != null) {
            emp.setPersonalDetails(newDetails);
            saveDataToFile();
            return true;
        }
        return false;
    }
    
    @Override
    public List<String> getLeaveHistory(String employeeId) throws RemoteException {
        Employee emp = employeeDatabase.get(employeeId);
        if (emp != null) {
            return emp.getLeaveHistory();
        }
        throw new RemoteException("Employee not found.");
    }
    
    @Override
    public String checkLeaveStatus(String employeeId) throws RemoteException {
        List<String> history = getLeaveHistory(employeeId);
        if (history.isEmpty()) return "No leave applications found.";
        return "Latest Application Status: " + history.get(history.size() - 1);
    }
}