package mdvsp;

import java.util.Objects;

/**
 * @author Georgiana Ojoc
 */
public abstract class Vehicle {
    private String name;
    private Depot depot;

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Depot getDepot() { return depot; }

    public void setDepot(Depot depot) { this.depot = depot; }

    @Override
    public String toString() { return name; }

    @Override
    public boolean equals(Object object) {
        if (this == object) { return true; }
        if (object == null || getClass() != object.getClass()) { return false; }
        Vehicle vehicle = (Vehicle) object;
        return Objects.equals(name, vehicle.name) &&
                Objects.equals(depot, vehicle.depot);
    }
}
