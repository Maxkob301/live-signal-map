package com.signalmap.liveSignalMap.service.impl;

import com.signalmap.liveSignalMap.entity.BssidMapping;
import com.signalmap.liveSignalMap.exception.BssidNotFoundException;
import com.signalmap.liveSignalMap.repository.BssidMappingRepository;
import com.signalmap.liveSignalMap.service.BssidService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BssidServiceImpl implements BssidService {

    private final BssidMappingRepository bssidMappingRepository;

    @Override
    public BssidMapping create(BssidMapping mapping) {
        mapping.setCreatedAt(LocalDateTime.now());
        return bssidMappingRepository.save(mapping);
    }

    @Override
    public BssidMapping findByBssid(String bssid) {
        return bssidMappingRepository.findByBssid(bssid)
                .orElseThrow(() -> new BssidNotFoundException(bssid));
    }

    @Override
    public List<BssidMapping> findAll() {
        return bssidMappingRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        bssidMappingRepository.deleteById(id);
    }
}