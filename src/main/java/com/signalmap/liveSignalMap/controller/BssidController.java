package com.signalmap.liveSignalMap.controller;

import com.signalmap.liveSignalMap.entity.BssidMapping;
import com.signalmap.liveSignalMap.service.BssidService;
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
    public List<BssidMapping> getAll() {
        return bssidService.findAll();
    }

    @GetMapping("/{bssid}")
    public BssidMapping getByBssid(@PathVariable String bssid) {
        return bssidService.findByBssid(bssid);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BssidMapping create(@RequestBody BssidMapping mapping) {
        return bssidService.create(mapping);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bssidService.delete(id);
    }
}