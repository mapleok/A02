// src/main/java/com/example/farmsim/model/dto/CropYieldHistoryDTO.java
package com.example.farmsim.model.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class CropYieldHistoryDTO {
    private Long id;
    private String cropId;
    private double yield;
    private LocalDateTime timestamp;
}