package com.georgiana.ojoc.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "charts", schema = "music_albums")
@NamedQueries({@NamedQuery(name = "Chart.findByAlbum", query = "SELECT c FROM Chart c WHERE c.albumID = :albumID")})
public class Chart implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "id_chart", nullable = false)
    private int chartID;

    @Column(name = "id_album", nullable = false)
    private int albumID;

    @Column(name = "place", nullable = false)
    private int rank;

    public Chart() {}

    public Chart(int chartID, int albumID, int rank) {
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

    public void setChartID(int idChart) {
        this.chartID = idChart;
    }

    public int getAlbumID() { return albumID; }

    public void setAlbumID(int albumID) { this.albumID = albumID; }

    public int getRank() {
        return rank;
    }

    public void setRank(int place) {
        this.rank = place;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Chart chart = (Chart) object;
        if (id != chart.id) {
            return false;
        }
        if (chartID != chart.chartID) {
            return false;
        }
        return rank == chart.rank;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + chartID;
        result = 31 * result + albumID;
        result = 31 * result + rank;
        return result;
    }

    @Override
    public String toString() {
        return "\n{ " + "id : " + id + ",\nchartID : " + chartID +
                ",\nalbumID : " + albumID + ",\nrank : " + rank + " }\n";
    }
}
