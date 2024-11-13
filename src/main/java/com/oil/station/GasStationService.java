package com.oil.station;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GasStationService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${opinet.api.key}")
    private String apiKey;

    @Value("${opinet.api.url}")
    private String apiUrl;

    public ResponseEntity<JsonNode> findNearbyGasStations(double lat, double lon, String radius) {
        // WGS84 좌표를 KATEC 좌표로 변환
        GeoTransPoint katecPoint = wgs84ToKatec(lat, lon);
        double x = katecPoint.getX();
        double y = katecPoint.getY();

        String url = UriComponentsBuilder.fromHttpUrl(apiUrl + "aroundAll.do")
                .queryParam("code", apiKey)
                .queryParam("x", x)
                .queryParam("y", y)
                .queryParam("radius", radius)
                .queryParam("sort", 1)
                .queryParam("prodcd", "B027")
                .queryParam("out", "json")
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // HTML 응답을 String으로 수신
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            ObjectMapper mapper = new ObjectMapper();

            // 응답을 JSON 형식으로 파싱
            JsonNode rootNode = mapper.readTree(response.getBody());
            JsonNode oilArray = rootNode.path("RESULT").path("OIL");

            // 필요한 필드만 포함된 새로운 JSON 배열 생성
            ArrayNode filteredGasStations = mapper.createArrayNode();
            for (JsonNode stationNode : oilArray) {
                ObjectNode filteredStation = mapper.createObjectNode();
                filteredStation.put("name", stationNode.path("OS_NM").asText());
                filteredStation.put("brand", stationNode.path("POLL_DIV_CD").asText());
                filteredStation.put("price", stationNode.path("PRICE").asInt());

                // GIS_X_COOR와 GIS_Y_COOR 값을 KATEC -> WGS84 변환
                double gisX = stationNode.path("GIS_X_COOR").asDouble();
                double gisY = stationNode.path("GIS_Y_COOR").asDouble();
                GeoTransPoint katecPointToConvert = new GeoTransPoint(gisX, gisY);
                GeoTransPoint wgs84Point = GeoTrans.convert(GeoTrans.KATEC, GeoTrans.GEO, katecPointToConvert);

                // 변환된 위도와 경도 값 추가
                filteredStation.put("latitude", wgs84Point.getY());
                filteredStation.put("longitude", wgs84Point.getX());

                filteredGasStations.add(filteredStation);
            }

            // JSON 배열을 응답으로 반환
            return ResponseEntity.ok(filteredGasStations);

        } catch (Exception e) {
            // 예외 발생 시 에러 응답 반환
            ObjectMapper mapper = new ObjectMapper();
            JsonNode errorJson = mapper.createObjectNode()
                    .put("error", "Error processing response")
                    .put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorJson);
        }
    }

    // WGS84 좌표(위도, 경도)를 KATEC 좌표로 변환하는 메서드
    public static GeoTransPoint wgs84ToKatec(double latitude, double longitude) {
        GeoTransPoint wgs84Point = new GeoTransPoint(longitude, latitude);
        return GeoTrans.convert(GeoTrans.GEO, GeoTrans.KATEC, wgs84Point);
    }
}
