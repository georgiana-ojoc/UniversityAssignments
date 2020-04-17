package music_albums.data;

import java.util.Objects;

public class Artist implements Comparable<Artist> {
    private int id;
    private String name;
    private String country;
    private int rank;
    private int score;

    public Artist(int id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
        rank = 0;
        score = 0;
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

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int compareTo(Artist artist) {
        return artist.score - score;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Artist)) {
            return false;
        }
        Artist artist = (Artist) object;
        return id == artist.id &&
                Objects.equals(getName(), artist.getName()) &&
                Objects.equals(getCountry(), artist.getCountry());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getName(), getCountry());
    }

    @Override
    public String toString() {
        return "\n{ " + "id : " + id + ",\nname : " + name + ",\ncountry : " + country + " }\n";
    }
}
