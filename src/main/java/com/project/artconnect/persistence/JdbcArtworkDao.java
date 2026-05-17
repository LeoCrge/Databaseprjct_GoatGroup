package com.project.artconnect.persistence;

import com.project.artconnect.dao.ArtworkDao;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Artwork;
import com.project.artconnect.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation for ArtworkDao.
 */
public class JdbcArtworkDao implements ArtworkDao {

    @Override
    public List<Artwork> findAll() {
        String sql = "SELECT title, type, description FROM artwork";
        List<Artwork> artworks = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Artwork a = new Artwork();
                a.setTitle(rs.getString("name"));
                a.setType(rs.getString("city"));
                a.setDescription(rs.getString("bio"));
                artworks.add(a);
            }
            return artworks;
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all artists", e);
        }

    }

    @Override
    public void save(Artwork artwork) {
        String sql = "INSERT INTO artwork (title, type, description) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, artwork.getTitle());
            ps.setString(2, artwork.getType());
            ps.setString(3, artwork.getDescription());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving artwork: " + artwork.getTitle(), e);
        }
    }

    @Override
    public void update(Artwork artwork) {
        String sql = "UPDATE artwork SET type = ?, description = ? WHERE title = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, artwork.getType());
            ps.setString(2, artwork.getDescription());
            ps.setString(3, artwork.getTitle());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating artist: " + artwork.getTitle(), e);
        }
    }

    @Override
    public void delete(String title) {
        String sql = "DELETE FROM artwork WHERE title = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting artist: " + title, e);
        }    }

    @Override
    public List<Artwork> findByArtistName(String artistName) {
        throw new UnsupportedOperationException("JDBC Implementation not yet provided.");
    }
}
