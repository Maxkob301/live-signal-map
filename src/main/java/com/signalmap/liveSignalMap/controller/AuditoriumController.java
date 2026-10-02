package com.signalmap.liveSignalMap.controller;

import com.signalmap.liveSignalMap.entity.Auditorium;
import com.signalmap.liveSignalMap.service.AuditoriumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auditoriums")
@RequiredArgsConstructor
public class AuditoriumController {

    private final AuditoriumService auditoriumService;

    @GetMapping
    public List<Auditorium> getAll() {
        return auditoriumService.findAll();
    }

    @GetMapping("/{id}")
    public Auditorium getById(@PathVariable Long id) {
        return auditoriumService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Auditorium create(@RequestBody Auditorium auditorium) {
        return auditoriumService.create(auditorium);
    }

    @PutMapping("/{id}")
    public Auditorium update(@PathVariable Long id, @RequestBody Auditorium auditorium) {
        return auditoriumService.update(id, auditorium);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        auditoriumService.delete(id);
    }
}