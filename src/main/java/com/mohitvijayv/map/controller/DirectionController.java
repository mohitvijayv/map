package com.mohitvijayv.map.controller;

import java.io.IOException;

import com.mohitvijayv.map.service.DirectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/directions")
@ResponseStatus(HttpStatus.OK)
@RestController
public class DirectionController {

    private DirectionService directionService;

    @Autowired
    public DirectionController(final DirectionService directionService) {
        this.directionService = directionService;
    }

    @GetMapping("equidistant")
    public ResponseEntity<String> getEquidistantPoints(@RequestParam String origin, @RequestParam String destination) throws IOException {
        return new ResponseEntity<>(directionService.getPoints(origin, destination), HttpStatus.OK);
    }
}
