package com.example.exerciseseven.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class ReportDto {

    private Long meterId;
    private String meterType;
    private Double firstReading;
    private Double lastReading;


    @Override
    public String toString() {
        return "ReportDto{" +
                "meterId=" + meterId +
                ", meterType='" + meterType + '\'' +
                ", firstReading=" + firstReading +
                ", lastReading=" + lastReading +
                '}';
    }
}
