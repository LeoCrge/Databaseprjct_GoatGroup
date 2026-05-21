package com.project.artconnect.service.impl;

<<<<<<< Updated upstream
public class JdbcGalleryService {
}
=======
import com.project.artconnect.dao.GalleryDao;
import com.project.artconnect.model.Exhibition;
import com.project.artconnect.model.Gallery;
import com.project.artconnect.service.ArtworkService;
import com.project.artconnect.service.GalleryService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class JdbcGalleryService implements GalleryService {

    private final GalleryDao galleryDao;

    // Constructor Injection
    public JdbcGalleryService(GalleryDao galleryDao) {
        this.galleryDao = galleryDao;
    }

    @Override
    public List<Gallery> getAllGalleries() {
        return galleryDao.findAll();
    }

    @Override
    public Optional<Gallery> getGalleryByName(String name) {
        return galleryDao.findAll().stream()
                .filter(g -> g.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public List<Exhibition> getExhibitionsByGallery(Gallery gallery) {

        // Until ExhibitionDao is implemented
        if (gallery == null) {
            return Collections.emptyList();
        }

        return gallery.getExhibitions();
    }

    @Override
    public void initData(ArtworkService artworkService) {

    }

    public void createGallery(Gallery gallery) {
        galleryDao.save(gallery);
    }

    public void updateGallery(Gallery gallery) {
        galleryDao.update(gallery);
    }

    public void deleteGallery(String name) {
        galleryDao.delete(name);
    }
}
>>>>>>> Stashed changes
