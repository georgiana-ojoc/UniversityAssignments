package mdvsp;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Georgiana Ojoc
 */
public class Depot {
    private String name;
    private Vehicle[] vehicles;

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Vehicle[] getVehicles() { return vehicles; }

    /**
     If a vehicle already had a depot, it will be deleted from that depot's list of vehicles.
     */
    public boolean setVehicles(Vehicle ... vehicles) {
        for (int i = 0; i < vehicles.length; ++i) {
            for (int j = i + 1; j < vehicles.length; ++j) {
                if (vehicles[i].equals(vehicles[j])) {
                    System.out.println(vehicles[i] + " appears twice. No vehicle is added.");
                    return false;
                }
            }
        }
        this.vehicles = vehicles;
        for (Vehicle vehicle : vehicles) {
            Depot oldDepot = vehicle.getDepot();
            if (oldDepot != null) {
                oldDepot.deleteVehicle(vehicle);
            }
            vehicle.setDepot(this);
        }
        return true;
    }

    public void deleteVehicle(Vehicle vehicle) {
        Vehicle[] newVehicles = new Vehicle[vehicles.length - 1];
        int i = 0;
        for (; i < vehicles.length; ++i) {
            if (vehicle.equals(vehicles[i])) {
                break;
            }
        }
        System.arraycopy(vehicles, 0, newVehicles, 0, i);
        System.arraycopy(vehicles, i + 1, newVehicles, i, vehicles.length - i - 1);
        vehicles = newVehicles;
    }

    @Override
    public String toString() {
        if (vehicles != null) {
            StringBuilder result = new StringBuilder(name + ": ");
            for (Vehicle vehicle : vehicles) {
                result.append(vehicle).append(", ");
            }
            result.delete(result.length() - 2, result.length() - 1);
            return result.toString();
        }
        return name;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) { return true; }
        if (object == null || getClass() != object.getClass()) { return false; }
        Depot depot = (Depot) object;
        return (Objects.equals(name, depot.name)) && (Arrays.equals(vehicles, depot.vehicles));
    }
}
