package com.oil.station;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gas-stations")
public class GasStationController {

    private final GasStationService gasStationService;

    @Autowired
    public GasStationController(GasStationService gasStationService) {
        this.gasStationService = gasStationService;
    }

    @PostMapping("/cheapest")
    public ResponseEntity<ResponseEntity<JsonNode>> getCheapestGasStation(@RequestBody GasStationRequest request) {
        ResponseEntity<JsonNode> cheapestStation = gasStationService.findNearbyGasStations(request.getX(), request.getY(), request.getRadius());
        return ResponseEntity.ok(cheapestStation);
    }
}
