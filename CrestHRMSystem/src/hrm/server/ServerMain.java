package hrm.server;

import hrm.shared.HRMService;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain {
    public static void main(String[] args) {
        try {
            HRMService hrmServer = new HRMServerImpl();
            
            Registry registry = LocateRegistry.createRegistry(1099);
            
            registry.rebind("CrestHRMService", hrmServer);
            
            System.out.println("=====================================");
            System.out.println("Crest Solutions HRM Server is RUNNING");
            System.out.println("Waiting for client requests...");
            System.out.println("=====================================");
            
        } catch (Exception e) {
            System.err.println("Server Exception: " + e.toString());
            e.printStackTrace();
        }
    }
}