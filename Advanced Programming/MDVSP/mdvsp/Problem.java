package mdvsp;

/**
 * @author Georgiana Ojoc
 */
public class Problem {
    private Depot[] depots;
    private Client[] clients;

    public Depot[] getDepots() {
        return depots;
    }

    public boolean setDepots(Depot ... depots) {
        for (int i = 0; i < depots.length; ++i) {
            for (int j = i + 1; j < depots.length; ++j) {
                if (depots[i].equals(depots[j])) {
                    System.out.println(depots[i] + " appears twice. No depot is added.");
                    return false;
                }
            }
        }
        this.depots = depots;
        return true;
    }

    public Client[] getClients() {
        return clients;
    }

    public boolean setClients(Client ... clients) {
        for (int i = 0; i < clients.length; ++i) {
            for (int j = i + 1; j < clients.length; ++j) {
                if (clients[i].equals(clients[j])) {
                    System.out.println(clients[i] + " appears twice. No client is added.");
                    return false;
                }
            }
        }
        this.clients = clients;
        return true;
    }

    public Vehicle[] getVehicles() {
        int length = 0;
        for (Depot depot : depots) {
            length += depot.getVehicles().length;
        }
        Vehicle[] problemVehicles = new Vehicle[length];
        int i = 0;
        for (Depot depot : depots) {
            Vehicle[] depotVehicles = depot.getVehicles();
            for (Vehicle vehicle : depotVehicles) {
                problemVehicles[i++] = vehicle;
            }
        }
        return problemVehicles;
    }

    @Override
    public String toString() {
        if ((depots == null) && (clients == null)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        if (depots != null) {
            for (Depot depot : depots) {
                result.append(depot).append('\n');
            }
        }
        if (clients != null) {
            for (Client client : clients) {
                result.append(client).append('\n');
            }
        }
        return result.toString();
    }
}
