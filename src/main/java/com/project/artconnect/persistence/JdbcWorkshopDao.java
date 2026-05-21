package com.project.artconnect.persistence;

import com.project.artconnect.dao.WorkshopDao;
import com.project.artconnect.model.Workshop;
import com.project.artconnect.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class JdbcWorkshopDao implements WorkshopDao {

    @Override
    public List<Workshop> findAll() {

        String sql = """
                SELECT title, dates, duration_minutes,
                       max_participant, price, location,
                       description, level
                FROM workshop
                """;

        List<Workshop> workshops = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Workshop w = new Workshop();

                w.setTitle(rs.getString("title"));

                Timestamp timestamp = rs.getTimestamp("dates");

                if (timestamp != null) {
                    w.setDate(timestamp.toLocalDateTime());
                }

                w.setDurationMinutes(rs.getInt("duration_minutes"));
                w.setMaxParticipants(rs.getInt("max_participant"));
                w.setPrice(rs.getDouble("price"));
                w.setLocation(rs.getString("location"));
                w.setDescription(rs.getString("description"));
                w.setLevel(rs.getString("level"));

                workshops.add(w);
            }

            return workshops;

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all workshops", e);
        }
    }

    @Override
    public void save(Workshop workshop) {

        String sql = """
                INSERT INTO workshop
                (title, dates, duration_minutes,
                 max_participant, price, location,
                 description, level)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, workshop.getTitle());

            ps.setTimestamp(
                    2,
                    Timestamp.valueOf(workshop.getDate())
            );

            ps.setInt(3, workshop.getDurationMinutes());
            ps.setInt(4, workshop.getMaxParticipants());
            ps.setDouble(5, workshop.getPrice());
            ps.setString(6, workshop.getLocation());
            ps.setString(7, workshop.getDescription());
            ps.setString(8, workshop.getLevel());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error saving workshop: " + workshop.getTitle(),
                    e
            );
        }
    }

    @Override
    public void update(Workshop workshop) {

        String sql = """
                UPDATE workshop
                SET dates = ?,
                    duration_minutes = ?,
                    max_participant = ?,
                    price = ?,
                    location = ?,
                    description = ?,
                    level = ?
                WHERE title = ?
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(
                    1,
                    Timestamp.valueOf(workshop.getDate())
            );

            ps.setInt(2, workshop.getDurationMinutes());
            ps.setInt(3, workshop.getMaxParticipants());
            ps.setDouble(4, workshop.getPrice());
            ps.setString(5, workshop.getLocation());
            ps.setString(6, workshop.getDescription());
            ps.setString(7, workshop.getLevel());

            ps.setString(8, workshop.getTitle());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error updating workshop: " + workshop.getTitle(),
                    e
            );
        }
    }

    @Override
    public void delete(String title) {

        String sql = "DELETE FROM workshop WHERE title = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, title);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error deleting workshop: " + title,
                    e
            );
        }
    }
}