package com.signalmap.liveSignalMap.service;

import com.signalmap.liveSignalMap.entity.Auditorium;

import java.util.List;

public interface AuditoriumService {

    Auditorium create(Auditorium auditorium);

    Auditorium findById(Long id);

    List<Auditorium> findAll();

    Auditorium update(Long id, Auditorium auditorium);

    void delete(Long id);
}