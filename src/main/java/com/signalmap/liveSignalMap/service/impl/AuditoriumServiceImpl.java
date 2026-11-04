package com.signalmap.liveSignalMap.service.impl;


import com.signalmap.liveSignalMap.entity.Auditorium;
import com.signalmap.liveSignalMap.exception.AuditoriumHasDependenciesException;
import com.signalmap.liveSignalMap.exception.AuditoriumNotFoundException;
import com.signalmap.liveSignalMap.repository.AuditoriumRepository;
import com.signalmap.liveSignalMap.repository.BssidMappingRepository;
import com.signalmap.liveSignalMap.repository.MobileMeasurementRepository;
import com.signalmap.liveSignalMap.repository.WifiMeasurementRepository;
import com.signalmap.liveSignalMap.service.AuditoriumService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditoriumServiceImpl implements AuditoriumService {


    private final AuditoriumRepository auditoriumRepository;

    private final BssidMappingRepository bssidMappingRepository;

    private final WifiMeasurementRepository wifiMeasurementRepository;
    private final MobileMeasurementRepository mobileMeasurementRepository;

    @Override
    public Auditorium create(Auditorium auditorium) {
        return auditoriumRepository.save(auditorium);
    }

    @Override
    public Auditorium findById(Long id) {
        return auditoriumRepository.findById(id)
                .orElseThrow(() -> new AuditoriumNotFoundException(id));
    }

    @Override
    public List<Auditorium> findAll() {
        return auditoriumRepository.findAll();
    }

    @Override
    public Auditorium update(Long id, Auditorium auditorium) {
        Auditorium existing = findById(id);
        existing.setName(auditorium.getName());
        existing.setBuilding(auditorium.getBuilding());
        existing.setFloor(auditorium.getFloor());
        return auditoriumRepository.save(existing);
    }



    @Override
    public void delete(Long id) {

        if (!auditoriumRepository.existsById(id)) {
            throw new AuditoriumNotFoundException(id);
        }

        boolean hasBssid =
                bssidMappingRepository.existsByAuditoriumId(id);

        boolean hasWifiMeasurements =
                wifiMeasurementRepository.existsByAuditoriumId(id);

        boolean hasMobileMeasurements =
                mobileMeasurementRepository.existsByAuditoriumId(id);

        if (hasBssid || hasWifiMeasurements || hasMobileMeasurements) {
            throw new AuditoriumHasDependenciesException(id);
        }

        auditoriumRepository.deleteById(id);
    }


}
