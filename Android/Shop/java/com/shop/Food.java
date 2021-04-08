package com.shop;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Food {
    @PrimaryKey
    private Integer identifier;

    private String name;

    private Integer image;

    private Double price;

    private String priceWithCurrency;

    public Food(String name, Integer image, Double price) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.priceWithCurrency = price + " $";
    }

    public Integer getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Integer identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public Double getPrice() { return price; }

    public String getPriceWithCurrency() {
        return priceWithCurrency;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setPriceWithCurrency(String price) {
        this.priceWithCurrency = price;
    }

    @Override
    public String toString() {
        return "Food{" +
                "name='" + name + '\'' +
                ", image=" + image +
                ", price=" + price +
                ", priceWithCurrency='" + priceWithCurrency + '\'' +
                '}';
    }
}