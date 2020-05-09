package gomoku.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RMIService extends Remote {
    List<String> getCommands() throws RemoteException;
}
