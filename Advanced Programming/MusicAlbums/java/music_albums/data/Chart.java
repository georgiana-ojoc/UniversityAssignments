package music_albums.data;

import java.util.Objects;

public class Chart {
    private int id;
    private int chartID;
    private int albumID;
    private int rank;

    public Chart(int id, int chartID, int albumID, int rank) {
        this.id = id;
        this.chartID = chartID;
        this.albumID = albumID;
        this.rank = rank;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getChartID() {
        return chartID;
    }

    public void setChartID(int chartID) {
        this.chartID = chartID;
    }

    public int getAlbumID() {
        return albumID;
    }

    public void setAlbumID(int albumID) {
        this.albumID = albumID;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Chart)) {
            return false;
        }
        Chart chart = (Chart) object;
        return id == chart.id &&
                getChartID() == chart.getChartID() &&
                getAlbumID() == chart.getAlbumID() &&
                getRank() == chart.getRank();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getChartID(), getAlbumID(), getRank());
    }

    @Override
    public String toString() {
        return "\n{ " + "id : " + id + ",\nchartID : " + chartID +
                ",\nalbumID : " + albumID + ",\nrank : " + rank + " }\n";
    }
}
