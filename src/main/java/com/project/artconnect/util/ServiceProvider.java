package com.project.artconnect.util;

import com.project.artconnect.dao.ArtworkDao;
import com.project.artconnect.dao.CommunityMemberDao;
import com.project.artconnect.dao.GalleryDao;
import com.project.artconnect.dao.WorkshopDao;
import com.project.artconnect.persistence.JdbcArtworkDao;
import com.project.artconnect.persistence.JdbcCommunityMemberDao;
import com.project.artconnect.persistence.JdbcGalleryDao;
import com.project.artconnect.persistence.JdbcWorkshopDao;
import com.project.artconnect.service.*;
import com.project.artconnect.service.impl.*;

/**
 * Service Provider to manage singleton instances of services and handle their
 * initialization.
 */
public class ServiceProvider {
    private static final ArtistService artistService = new JdbcArtistService();
    static GalleryDao galleryDao = new JdbcGalleryDao();
    static ArtworkDao artworksDao = new JdbcArtworkDao();
    static WorkshopDao workshopDao = new JdbcWorkshopDao();
    static CommunityMemberDao communityMemberDao = new JdbcCommunityMemberDao();
    private static final ArtworkService artworkService = new JdbcArtworkService(artworksDao);
    private static final GalleryService galleryService = new JdbcGalleryService(galleryDao);
    private static final WorkshopService workshopService = new JdbcWorkshopService(workshopDao);
    private static final CommunityService communityService = new JdbcCommunityService(communityMemberDao);

    static {
        // Initialize services with their dependencies
        artworkService.initData(artistService);
        galleryService.initData(artworkService);
        workshopService.initData(artistService);
        communityService.initData(artworkService);
    }

    public static ArtistService getArtistService() {
        return artistService;
    }

    public static ArtworkService getArtworkService() {
        return artworkService;
    }

    public static GalleryService getGalleryService() {
        return galleryService;
    }

    public static WorkshopService getWorkshopService() {
        return workshopService;
    }

    public static CommunityService getCommunityService() {
        return communityService;
    }
}
