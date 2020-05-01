package com.georgiana.ojoc.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "albums", schema = "music_albums")
@NamedQueries({@NamedQuery(name = "Album.findByName", query = "SELECT a FROM Album a WHERE UPPER(a.name) = UPPER(:name)"),
        @NamedQuery(name = "Album.findByArtist", query = "SELECT a FROM Album a WHERE a.artistID = :artistID"),
        @NamedQuery(name = "Album.findByReleaseYear", query = "SELECT a FROM Album a WHERE a.releaseYear = :releaseYear"),
        @NamedQuery(name = "Album.findByGenre", query = "SELECT a FROM Album a WHERE UPPER(a.genre) = UPPER(:genre)"),
        @NamedQuery(name = "Album.findAll", query = "SELECT a FROM Album a")})
public class Album implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "id_artist", nullable = false)
    private int artistID;

    @Column(name = "release_year")
    private int releaseYear;

    @Column(name = "genre")
    private String genre;

    public Album() {}

    public Album(String name, int artistID, int releaseYear, String genre) {
        this.name = name;
        this.artistID = artistID;
        this.releaseYear = releaseYear;
        this.genre = genre;
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

    public int getArtistID() {
        return artistID;
    }

    public void setArtistID(int artistID) {
        this.artistID = artistID;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Album album = (Album) object;
        if (id != album.id) {
            return false;
        }
        if (!Objects.equals(name, album.name)) {
            return false;
        }
        if (releaseYear != album.releaseYear) {
            return false;
        }
        return Objects.equals(genre, album.genre);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + releaseYear;
        result = 31 * result + (genre != null ? genre.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "\n{ " + "id : " + id + ",\nname : " + name +
                ",\nartistID : " + artistID + ",\nreleaseYear : " + releaseYear +
                ",\ngenre : " + genre + " }\n";
    }
}
