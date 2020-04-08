package mdvsp;

import java.util.Objects;

/**
 * @author Georgiana Ojoc
 */
public class Client {
    private String name;
    private int order;

    public Client(String name, int order) {
        this.name = name;
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return name + '(' + order + ')';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)  { return true; }
        if (object == null || getClass() != object.getClass()) { return false; }
        Client client = (Client) object;
        return (order == client.order) && Objects.equals(name, client.name);
    }
}
