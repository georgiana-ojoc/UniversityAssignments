package com.georgiana.ojoc.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "artists", schema = "music_albums")
@NamedQueries({@NamedQuery(name = "Artist.findByName", query = "SELECT a FROM Artist a WHERE UPPER(a.name) = UPPER(:name)"),
        @NamedQuery(name = "Artist.findByCountry", query = "SELECT a FROM Artist a WHERE UPPER(a.country) = UPPER(:country)")})
public class Artist implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "country", nullable = false, length = 100)
    private String country;

    public Artist() {}

    public Artist(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Artist artist = (Artist) object;
        if (id != artist.id) {
            return false;
        }
        if (!Objects.equals(name, artist.name)) {
            return false;
        }
        return Objects.equals(country, artist.country);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "\n{ " + "id : " + id + ",\nname : " + name + ",\ncountry : " + country + " }\n";
    }
}
