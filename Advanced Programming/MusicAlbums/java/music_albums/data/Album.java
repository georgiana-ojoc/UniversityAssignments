package music_albums.data;

import java.util.Objects;

public class Album {
    private int id;
    private String name;
    private int artistID;
    private int releaseYear;

    public Album(int id, String name, int artistID, int releaseYear) {
        this.id = id;
        this.name = name;
        this.artistID = artistID;
        this.releaseYear = releaseYear;
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

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Album)) {
            return false;
        }
        Album album = (Album) object;
        return id == album.id &&
                getArtistID() == album.getArtistID() &&
                getReleaseYear() == album.getReleaseYear() &&
                Objects.equals(getName(), album.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getName(), getArtistID(), getReleaseYear());
    }

    @Override
    public String toString() {
        return "\n{ " + "id : " + id + ",\nname : " + name +
                ",\nartistID : " + artistID + ",\nreleaseYear : " + releaseYear + " }\n";
    }
}
