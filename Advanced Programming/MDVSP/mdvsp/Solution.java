package mdvsp;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Georgiana Ojoc
 */
public class Solution {
    private Vehicle[] vehicles;
    private Client[] clients;
    private boolean[] visitedClients;
    private List<Tour> tours;

    public Solution(Problem problem) {
        vehicles = problem.getVehicles();
        clients = problem.getClients();
        Arrays.sort(clients, Comparator.comparingInt(Client::getOrder));
        visitedClients = new boolean[clients.length];
        tours = new LinkedList<>();
    }

    public List<Tour> getTours() { return tours; }

    /**
     * The clients are assigned in ascending order of their visiting times to the first available vehicles.
     */
    public void greedy() {
        for (Vehicle vehicle : vehicles) {
            Tour tour = new Tour(vehicle);
            int maximumOrder = 0;
            int clientsNumber = 0;
            for (int i = 0; i < clients.length; ++i) {
                if (!visitedClients[i]) {
                    if (clients[i].getOrder() > maximumOrder) {
                        tour.addClients(clients[i]);
                        visitedClients[i] = true;
                        maximumOrder = clients[i].getOrder();
                        ++clientsNumber;
                    }
                }
            }
            if (clientsNumber > 0) {
                tours.add(tour);
            }
        }
    }

    public void print() {
        for (Tour tour : tours) {
            System.out.println(tour);
        }
    }
}
