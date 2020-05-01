package com.georgiana.ojoc;

import static org.junit.Assert.assertEquals;

import com.georgiana.ojoc.entity.Album;
import com.georgiana.ojoc.util.HopcroftKarp;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AlbumManagerTest
{
    @Test
    public void answerOneShouldBeTrue() {
        List<Album> albums = new ArrayList<>();
        albums.add(new Album("1", 1, 2020, "pop"));
        albums.add(new Album("2", 1, 2020, "rock"));
        albums.add(new Album("3", 1, 2020, "country"));
        assertEquals(1, new HopcroftKarp(albums).getMatchingCardinal());
    }

    @Test
    public void answerTwoShouldBeTrue() {
        List<Album> albums = new ArrayList<>();
        albums.add(new Album("1", 1, 2020, "pop"));
        albums.add(new Album("2", 2, 2020, "rock"));
        albums.add(new Album("3", 1, 2020, "country"));
        assertEquals(2, new HopcroftKarp(albums).getMatchingCardinal());
    }

    @Test
    public void answerThreeShouldBeTrue() {
        List<Album> albums = new ArrayList<>();
        albums.add(new Album("1", 1, 2020, "pop"));
        albums.add(new Album("2", 2, 2020, "rock"));
        albums.add(new Album("3", 3, 2020, "country"));
        assertEquals(3, new HopcroftKarp(albums).getMatchingCardinal());
    }
}
