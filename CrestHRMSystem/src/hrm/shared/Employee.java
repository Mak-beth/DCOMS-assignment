package hrm.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String employeeId;
    private String firstName;
    private String lastName;
    private String icNumber;
    private String personalDetails;
    private List<String> leaveHistory;
    private int leaveBalance;

    public Employee(String employeeId, String firstName, String lastName, String icNumber) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.icNumber = icNumber;
        this.personalDetails = "No details provided.";
        this.leaveBalance = 20;
        this.leaveHistory = new ArrayList<>();
    }
    
    public void addLeaveHistory(String record) {
        this.leaveHistory.add(record);
    }
    
    public List<String> getLeaveHistory() {
        return leaveHistory;
    }

    public void setPersonalDetails(String details) {
        this.personalDetails = details;
    }
    
    public String getPersonalDetails() {
        return personalDetails;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getIcNumber() {
        return icNumber;
    }

    public int getLeaveBalance() {
        return leaveBalance;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setIcNumber(String icNumber) {
        this.icNumber = icNumber;
    }

    public void setLeaveBalance(int leaveBalance) {
        this.leaveBalance = leaveBalance;
    }
    
    @Override
    public String toString() {
        return "Employee[" + employeeId + "]: " + firstName + " " + lastName;
    }
}