package com.project.artconnect.service.impl;

import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Artwork;
import com.project.artconnect.service.ArtistService;
import com.project.artconnect.service.ArtworkService;
import java.util.*;

public class InMemoryArtworkService implements ArtworkService {
    private final Map<String, Artwork> artworks = new LinkedHashMap<>();

    public InMemoryArtworkService() {
        // Data initialized after ArtistService is ready
    }

    public void initData(ArtistService artistService) {
       }

    private void addArtwork(String title, int year, String type, double price, Artist artist) {
        if (artist == null)
            return;
        Artwork a = new Artwork(title, year, type, price, artist);
        a.setMedium("Traditional " + type);
        a.setDimensions("Varies");
        a.setDescription("A legendary masterpiece by " + artist.getName());
        artworks.put(title, a);
        artist.addArtwork(a);
    }

    @Override
    public List<Artwork> getAllArtworks() {
        return new ArrayList<>(artworks.values());
    }

    @Override
    public List<Artwork> getArtworksByArtist(Artist artist) {
        if (artist == null)
            return Collections.emptyList();
        return artist.getArtworks();
    }

    @Override
    public void createArtwork(Artwork artwork) {
        artworks.put(artwork.getTitle(), artwork);
    }

    @Override
    public void updateArtwork(Artwork artwork) {
        artworks.put(artwork.getTitle(), artwork);
    }

    @Override
    public void deleteArtwork(String title) {
        artworks.remove(title);
    }
}
