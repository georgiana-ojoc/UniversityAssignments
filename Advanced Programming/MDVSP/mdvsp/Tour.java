package mdvsp;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Georgiana Ojoc
 */
public class Tour {
    private Vehicle vehicle;
    private Depot depot;
    private List<Client> clients;
    private int cost = 0;

    public Tour(Vehicle vehicle) {
        this.vehicle = vehicle;
        depot = vehicle.getDepot();
        clients = new LinkedList<>();
    }

    public Tour(Depot depot) {
        this.depot = depot;
        clients = new LinkedList<>();
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Depot getDepot() { return depot; }

    public int getCost() { return cost; }

    public List<Client> getClients() { return clients; }

    public void addClients(Client ... clients) {
        this.clients.addAll(Arrays.asList(clients));
    }

    public void setCost(int cost) { this.cost = cost; }

    @Override
    public String toString() {
        if ((vehicle == null) && (clients == null)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        if (vehicle != null) {
            result.append(vehicle).append(": ");
        }
        if (depot != null) {
            result.append(depot.getName());
        }
        if (clients != null) {
            if (vehicle != null) {
                for (Client client : clients) {
                    result.append(" -> ").append(client);
                }
            }
            else {
                for (Client client : clients) {
                    result.append(" -> ").append(client.getName());
                }
            }
        }
        if (vehicle != null && depot != null) {
            result.append(" -> ").append(depot.getName());
        }
        if (cost != 0) {
            result.append(" (cost = ").append(cost).append(')');
        }
        return result.toString();
    }
}
