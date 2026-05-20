package com.project.artconnect.service.impl;

import com.project.artconnect.dao.ArtistDao;
import com.project.artconnect.dao.ArtworkDao;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Artwork;
import com.project.artconnect.persistence.JdbcArtworkDao;
import com.project.artconnect.service.ArtistService;
import com.project.artconnect.service.ArtworkService;


import java.util.List;
import java.util.Optional;

public class JdbcArtworkService implements ArtworkService {

    private final ArtworkDao artworkDao;

    public JdbcArtworkService(ArtworkDao artworkDao) {
        this.artworkDao = new JdbcArtworkDao();
    }

    @Override
    public List<Artwork> getAllArtworks() {
        return artworkDao.findAll();
    }

    @Override
    public List<Artwork> getArtworksByArtist(Artist artist) {
        return artworkDao.findByArtistName(artist.getName());
    }

    @Override
    public void createArtwork(Artwork artwork) {
        artworkDao.save(artwork);
    }

    @Override
    public void updateArtwork(Artwork artwork) {
        artworkDao.update(artwork);
    }

    @Override
    public void deleteArtwork(String title) {
        artworkDao.delete(title);
    }

    @Override
    public void initData(ArtistService artistService) {

    }
}
