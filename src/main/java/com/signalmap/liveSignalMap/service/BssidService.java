package com.signalmap.liveSignalMap.service;

import com.signalmap.liveSignalMap.entity.BssidMapping;

import java.util.List;

public interface BssidService {

    BssidMapping create(BssidMapping mapping);

    BssidMapping findByBssid(String bssid);

    List<BssidMapping> findAll();

    void delete(Long id);
}