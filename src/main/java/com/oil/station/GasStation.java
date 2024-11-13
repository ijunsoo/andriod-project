package com.oil.station;


import com.oil.station.util.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class GasStation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double latitude;
    private double longitude;
    private double price;

    // 추가적인 필드들, 예를 들어 주소나 브랜드 등 필요에 따라 포함
    private String address;
    private String brand;

    // Getters and Setters
    public GasStation(String name, double latitude, double longitude, double price, String address, String brand) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.price = price;
        this.address = address;
        this.brand = brand;
    }
}
