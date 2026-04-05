package hrm.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface HRMService extends Remote {
    
    String registerEmployee(String firstName, String lastName, String icNumber) throws RemoteException;
    String generateYearlyReport(String employeeId) throws RemoteException;

    Employee getEmployeeDetails(String employeeId) throws RemoteException;
    boolean applyForLeave(String employeeId, int days) throws RemoteException;
    int checkLeaveBalance(String employeeId) throws RemoteException;
    
    boolean updatePersonalDetails(String employeeId, String newDetails) throws RemoteException;
    List<String> getLeaveHistory(String employeeId) throws RemoteException;
    String checkLeaveStatus(String employeeId) throws RemoteException;
}