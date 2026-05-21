package com.project.artconnect.persistence;

import com.project.artconnect.dao.CommunityMemberDao;
import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.model.Discipline;
import com.project.artconnect.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation for CommunityMemberDao.
 */
public class JdbcCommunityMemberDao implements CommunityMemberDao {

    @Override
    public Optional<CommunityMember> findById(Long id) {

        String sql = "SELECT * FROM communitymember WHERE id_member = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    CommunityMember member = new CommunityMember();

                    member.setName(rs.getString("Name"));
                    member.setEmail(rs.getString("Email"));
                    member.setBirthYear(rs.getInt("BirthYear"));
                    member.setPhone(rs.getString("phone"));
                    member.setCity(rs.getString("city"));
                    member.setMembershipType(rs.getString("Membership_type"));

                    return Optional.of(member);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching member with id: " + id, e);
        }

        return Optional.empty();
    }

    @Override
    public List<CommunityMember> findAll() {

        String sql = "SELECT * FROM communitymember";
        List<CommunityMember> members = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                CommunityMember member = new CommunityMember();

                member.setName(rs.getString("Name"));
                member.setEmail(rs.getString("Email"));
                member.setBirthYear(rs.getInt("BirthYear"));
                member.setPhone(rs.getString("phone"));
                member.setCity(rs.getString("city"));
                member.setMembershipType(rs.getString("Membership_type"));

                members.add(member);
            }

            return members;

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all community members", e);
        }
    }

    @Override
    public void save(CommunityMember member) {

        String sql = "INSERT INTO communitymember " +
                "(Name, Email, BirthYear, phone, city, favDiscipline, Membership_type) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());

            if (member.getBirthYear() != null) {
                ps.setInt(3, member.getBirthYear());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }

            ps.setString(4, member.getPhone());
            ps.setString(5, member.getCity());
            ps.setString(6, member.getFavoriteDisciplines().get(0).getName());
            ps.setString(7, member.getMembershipType());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error saving member: " + member.getName(), e);
        }
    }

    @Override
    public void update(CommunityMember member) {

        String sql = "UPDATE communitymember SET " +
                "Email = ?, " +
                "BirthYear = ?, " +
                "phone = ?, " +
                "city = ?, " +
                "favDiscipline = ?, " +
                "Membership_type = ? " +
                "WHERE Name = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, member.getEmail());

            if (member.getBirthYear() != null) {
                ps.setInt(2, member.getBirthYear());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }

            ps.setString(3, member.getPhone());
            ps.setString(4, member.getCity());
            ps.setString(5, member.getFavoriteDisciplines().get(0).getName());
            ps.setString(6, member.getMembershipType());
            ps.setString(7, member.getName());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error updating member: " + member.getName(), e);
        }
    }

    @Override
    public void delete(String name) {

        String sql = "DELETE FROM communitymember WHERE Name = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting member: " + name, e);
        }
    }
}