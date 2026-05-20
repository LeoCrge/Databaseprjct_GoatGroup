package com.project.artconnect.service.impl;

import com.project.artconnect.dao.ArtistDao;
import com.project.artconnect.model.*;
import com.project.artconnect.service.WorkshopService;
import com.project.artconnect.service.ArtistService;
import com.project.artconnect.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryWorkshopService implements WorkshopService {
    private final Map<String, Workshop> workshops = new LinkedHashMap<>();

    public InMemoryWorkshopService() {
    }



    private void addWorkshop(String title, LocalDateTime date, Artist instructor, double price, String level,
            String location) {
        if (instructor == null)
            return;
        Workshop w = new Workshop(title, date, instructor, price);
        w.setLevel(level);
        w.setLocation(location);
        w.setDurationMinutes(180);
        w.setMaxParticipants(10);
        workshops.put(title, w);
    }

    @Override
    public List<Workshop> getAllWorkshops() {
        return new ArrayList<>(workshops.values());
    }

    @Override
    public Optional<Workshop> getWorkshopByTitle(String title) {
        return Optional.ofNullable(workshops.get(title));
    }

    @Override
    public void bookWorkshop(Workshop workshop, CommunityMember member) {
        if (workshop == null || member == null)
            return;
        Booking b = new Booking(workshop, member);
        member.addBooking(b);
    }

    @Override
    public List<Booking> getBookingsByMember(CommunityMember member) {
        if (member == null)
            return Collections.emptyList();
        return member.getBookings();
    }

    public void initData(ArtistService artistService) {
    }

    public static class JdbcArtistService implements ArtistService {
        private final ArtistDao artistDao;

        public JdbcArtistService(ArtistDao artistDao) {
            this.artistDao = artistDao;
        }

        @Override
        public List<Artist> getAllArtists() {
            return artistDao.findAll();
        }

        @Override
        public List<Artist> getArtistByCity(String city) {
            return artistDao.findByCity(city);
        }

        @Override
        public Optional<Artist> getArtistByName(String name) {
            return Optional.empty();
        }

        @Override
        public void createArtist(Artist artist) {
            artistDao.save(artist);
        }

        @Override
        public void updateArtist(Artist artist) {
            artistDao.update(artist);
        }

        @Override
        public void deleteArtist(String name) {
            artistDao.delete(name);
        }

        @Override
        public List<Discipline> getAllDisciplines() {
            List<Discipline> disciplines = new ArrayList<>();
            String sql = "SELECT * FROM Discipline";
            try (Connection conn = ConnectionManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    disciplines.add(new Discipline(rs.getString("name")));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return disciplines;
        }

        @Override
        public List<Artist> searchArtists(String query, String disciplineName, String city) {
            return artistDao.findAll().stream()
                    .filter(a -> query == null || a.getName().toLowerCase().contains(query.toLowerCase()))
                    .filter(a -> city == null || city.isEmpty()
                            || (a.getCity() != null && a.getCity().equalsIgnoreCase(city)))
                    .collect(Collectors.toList());
        }
    }
}
