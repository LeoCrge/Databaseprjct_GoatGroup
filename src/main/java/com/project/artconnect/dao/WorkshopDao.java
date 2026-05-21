package com.project.artconnect.dao;

import com.project.artconnect.model.Workshop;

import java.util.List;

public interface WorkshopDao {

    List<Workshop> findAll();

    void save(Workshop workshop);

    void update(Workshop workshop);

    void delete(String title);
}