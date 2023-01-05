package com.example.exerciseseven.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Getter
@Setter
public class MeterDto {
    @NotNull
    private Long meterId;
    @NotNull
    private String type;
    @NotNull
    private String meterGroup;
    @JsonProperty("timestamp")
    @NotNull
    private LocalDateTime dateTime;
    @NotNull
    @PositiveOrZero
    private Double currentReading;
}
