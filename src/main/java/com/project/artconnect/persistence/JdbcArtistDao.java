package com.project.artconnect.persistence;

import com.project.artconnect.util.ConnectionManager;
import com.project.artconnect.dao.ArtistDao;
import com.project.artconnect.model.Artist;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
/**
 * JDBC implementation for ArtistDao.
 * TODO: Students must implement this using JDBC and SQL.
 */
public class JdbcArtistDao implements ArtistDao {

    private final DataSource dataSource;

    public JdbcArtistDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // TODO: Implement SELECT * FROM artist
    @Override
    public List<Artist> findAll() {
        String sql = "SELECT name, city, bio FROM artist";
        List<Artist> artists = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Artist a = new Artist();
                a.setName(rs.getString("name"));
                a.setCity(rs.getString("city"));
                a.setBio(rs.getString("bio"));
                artists.add(a);
            }
            return artists;
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all artists", e);
        }
    }


        // TODO: Implement INSERT INTO artist(...) VALUES(...)
    @Override
    public void save(Artist artist) {
        String sql = "INSERT INTO artist (name, city, bio) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, artist.getName());
            ps.setString(2, artist.getCity());
            ps.setString(3, artist.getBio());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving artist: " + artist.getName(), e);
        }
    }

    // TODO: Implement UPDATE artist SET ... WHERE name = ?
    @Override
    public void update(Artist artist) {
        String sql = "UPDATE artist SET city = ?, bio = ? WHERE name = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, artist.getCity());
            ps.setString(2, artist.getBio());
            ps.setString(3, artist.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating artist: " + artist.getName(), e);
        }
    }
        // TODO: Implement UPDATE artist SET ... WHERE name = ?


    // TODO: Implement DELETE FROM artist WHERE name = ?
    @Override
    public void delete(String artistName) {
        String sql = "DELETE FROM artist WHERE name = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, artistName);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting artist: " + artistName, e);
        }
    }

    // TODO: Implement SELECT * FROM artist WHERE city = ?
    @Override
    public List<Artist> findByCity(String city) {
        String sql = "SELECT name, city, bio FROM artist WHERE city = ?";
        List<Artist> artists = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, city);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Artist a = new Artist();
                    a.setName(rs.getString("name"));
                    a.setCity(rs.getString("city"));
                    a.setBio(rs.getString("bio"));
                    artists.add(a);
                }
            }
            return artists;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding artists by city: " + city, e);
        }
    }
}
