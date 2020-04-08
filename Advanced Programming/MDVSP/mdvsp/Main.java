package mdvsp;

/**
 * @author Georgiana Ojoc
 */
public class Main {
    public static void main(String[] args) {
        Car car = new Car();
        car.setName("Car");
        Truck truck = new Truck();
        truck.setName("Truck");
        Drone drone = new Drone();
        drone.setName("Drone");

        Depot depot1 = new Depot();
        depot1.setName("Depot1");
        if (!depot1.setVehicles(car, truck)) {
            return;
        }
        Depot depot2 = new Depot();
        depot2.setName("Depot2");
        if (!depot2.setVehicles(drone)) {
            return;
        }

        Client client1 = new Client("Client1", 1);
        Client client2 = new Client("Client2", 1);
        Client client3 = new Client("Client3", 2);
        Client client4 = new Client("Client4", 2);
        Client client5 = new Client("Client5", 3);

        System.out.println("Instance:");
        Problem problem = new Problem();
        if (!problem.setDepots(depot1, depot2)) {
            return;
        }
        if (!problem.setClients(client1, client2, client3, client4, client5)) {
            return;
        }
        System.out.println(problem);

        Solution solution = new Solution(problem);
        solution.greedy();
        System.out.println("Clients allocated to vehicles:");
        solution.print();
        System.out.println();

        CostSolution costSolution = new CostSolution(problem);
        costSolution.dijkstra();
        costSolution.printCostMatrix();
        System.out.println();
        System.out.println("Cheapest and shortest tours:");
        costSolution.print();
    }
}
