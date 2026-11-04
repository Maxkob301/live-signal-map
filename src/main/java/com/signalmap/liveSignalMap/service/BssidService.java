
package com.signalmap.liveSignalMap.service;

import com.signalmap.liveSignalMap.dto.BssidMappingRequest;
import com.signalmap.liveSignalMap.dto.BssidMappingResponse;

import java.util.List;

public interface BssidService {

    BssidMappingResponse create(BssidMappingRequest request);

    BssidMappingResponse findByBssid(String bssid);

    List<BssidMappingResponse> findAll();

    void delete(Long id);
}
