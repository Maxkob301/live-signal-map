
package com.signalmap.liveSignalMap.controller;

import com.signalmap.liveSignalMap.dto.BssidMappingRequest;
import com.signalmap.liveSignalMap.dto.BssidMappingResponse;
import com.signalmap.liveSignalMap.service.BssidService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bssid")
@RequiredArgsConstructor
public class BssidController {

    private final BssidService bssidService;

    @GetMapping
    public List<BssidMappingResponse> getAll() {
        return bssidService.findAll();
    }

    @GetMapping("/{bssid}")
    public BssidMappingResponse getByBssid(@PathVariable String bssid) {
        return bssidService.findByBssid(bssid);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BssidMappingResponse create(
            @Valid @RequestBody BssidMappingRequest request
    ) {
        return bssidService.create(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bssidService.delete(id);
    }
}
