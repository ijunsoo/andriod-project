package com.oil.station;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class GasStationResponse {

    @JsonProperty("RESULT")  // 실제 JSON 응답 키에 맞춰 조정해야 합니다
    private List<GasStation> gasStations;

    public List<GasStation> getGasStations() {
        return gasStations;
    }

    public void setGasStations(List<GasStation> gasStations) {
        this.gasStations = gasStations;
    }
}