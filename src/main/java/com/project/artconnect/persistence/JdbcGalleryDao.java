package com.project.artconnect.persistence;

import com.project.artconnect.dao.GalleryDao;
import com.project.artconnect.model.Gallery;
import com.project.artconnect.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation for GalleryDao.
 */
public class JdbcGalleryDao implements GalleryDao {

    @Override
    public Optional<Gallery> findByName(String name) {
        String sql = "SELECT * FROM gallery WHERE name = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    Gallery gallery = new Gallery();

                    gallery.setName(rs.getString("name"));
                    gallery.setAddress(rs.getString("address"));
                    gallery.setOwnerName(rs.getString("ownername"));
                    gallery.setOpeningHours(rs.getString("opening_hours"));
                    gallery.setContactPhone(rs.getString("contact_phone"));
                    gallery.setRating(rs.getFloat("rating"));
                    gallery.setWebsite(rs.getString("website"));

                    return Optional.of(gallery);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching gallery: " + name, e);
        }

        return Optional.empty();
    }

    @Override
    public List<Gallery> findAll() {

        String sql = "SELECT * FROM gallery";
        List<Gallery> galleries = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Gallery gallery = new Gallery();

                gallery.setName(rs.getString("name"));
                gallery.setAddress(rs.getString("address"));
                gallery.setOwnerName(rs.getString("ownername"));
                gallery.setOpeningHours(rs.getString("opening_hours"));
                gallery.setContactPhone(rs.getString("contact_phone"));
                gallery.setRating(rs.getFloat("rating"));
                gallery.setWebsite(rs.getString("website"));

                galleries.add(gallery);
            }

            return galleries;

        } catch (SQLException e) {
        }
        return galleries;
    }

    @Override
    public void save(Gallery gallery) {

        String sql = "INSERT INTO gallery " +
                "(id_gallery, name, address, ownername, opening_hours, contact_phone, rating, website) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(2, gallery.getName());
            ps.setString(3, gallery.getAddress());
            ps.setString(4, gallery.getOwnerName());
            ps.setString(5, gallery.getOpeningHours());
            ps.setString(6, gallery.getContactPhone());
            ps.setDouble(7, gallery.getRating());
            ps.setString(8, gallery.getWebsite());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error saving gallery: " + gallery.getName(), e);
        }
    }

    @Override
    public void update(Gallery gallery) {

        String sql = "UPDATE gallery SET " +
                "address = ?, " +
                "ownername = ?, " +
                "opening_hours = ?, " +
                "contact_phone = ?, " +
                "rating = ?, " +
                "website = ? " +
                "WHERE name = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, gallery.getAddress());
            ps.setString(2, gallery.getOwnerName());
            ps.setString(3, gallery.getOpeningHours());
            ps.setString(4, gallery.getContactPhone());
            ps.setDouble(5, gallery.getRating());
            ps.setString(6, gallery.getWebsite());
            ps.setString(7, gallery.getName());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error updating gallery: " + gallery.getName(), e);
        }
    }

    @Override
    public void delete(String name) {

        String sql = "DELETE FROM gallery WHERE name = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting gallery: " + name, e);
        }
    }
}