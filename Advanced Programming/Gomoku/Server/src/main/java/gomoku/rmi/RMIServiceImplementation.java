package gomoku.rmi;

import java.rmi.RemoteException;
import java.util.List;

public class RMIServiceImplementation implements RMIService {
    private List<String> commands;

    public RMIServiceImplementation(List<String> commands) throws RemoteException {
        super();
        this.commands = commands;
    }

    @Override
    public List<String> getCommands() throws RemoteException {
        return commands;
    }
}
