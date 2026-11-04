
package com.signalmap.liveSignalMap.service.impl;

import com.signalmap.liveSignalMap.dto.BssidMappingRequest;
import com.signalmap.liveSignalMap.dto.BssidMappingResponse;
import com.signalmap.liveSignalMap.entity.BssidMapping;
import com.signalmap.liveSignalMap.exception.BssidAlreadyExistsException;
import com.signalmap.liveSignalMap.exception.BssidNotFoundException;
import com.signalmap.liveSignalMap.repository.AuditoriumRepository;
import com.signalmap.liveSignalMap.repository.BssidMappingRepository;
import com.signalmap.liveSignalMap.service.BssidService;
import com.signalmap.liveSignalMap.exception.AuditoriumNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BssidServiceImpl implements BssidService {

    private final BssidMappingRepository bssidMappingRepository;

    private final AuditoriumRepository auditoriumRepository;



    @Override
    public BssidMappingResponse create(BssidMappingRequest request) {

        if (bssidMappingRepository.existsByBssid(request.getBssid())) {
            throw new BssidAlreadyExistsException(request.getBssid());
        }

        if (!auditoriumRepository.existsById(request.getAuditoriumId())) {
            throw new AuditoriumNotFoundException(
                    request.getAuditoriumId()
            );
        }

        BssidMapping mapping = new BssidMapping();
        mapping.setBssid(request.getBssid());
        mapping.setAuditoriumId(request.getAuditoriumId());

        BssidMapping saved = bssidMappingRepository.save(mapping);

        return BssidMappingResponse.from(saved);
    }



    @Override
    public BssidMappingResponse findByBssid(String bssid) {
        BssidMapping mapping = bssidMappingRepository.findByBssid(bssid)
                .orElseThrow(() -> new BssidNotFoundException(bssid));

        return BssidMappingResponse.from(mapping);
    }

    @Override
    public List<BssidMappingResponse> findAll() {
        return bssidMappingRepository.findAll()
                .stream()
                .map(BssidMappingResponse::from)
                .toList();
    }

    @Override
    public void delete(Long id) {
        bssidMappingRepository.deleteById(id);
    }


}
