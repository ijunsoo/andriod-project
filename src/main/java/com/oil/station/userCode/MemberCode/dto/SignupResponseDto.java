package com.oil.station.userCode.MemberCode.dto;

import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Builder
@AllArgsConstructor
public class SignupResponseDto {
    @Id
    private Long id;
    private LocalDateTime createdAt;
}
