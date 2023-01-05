package com.example.exerciseseven.controller;

import com.example.exerciseseven.dto.MeterDto;
import com.example.exerciseseven.service.MeterService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1.0")
@RequiredArgsConstructor
public class MeterController {
    private final MeterService meterService;

    @PostMapping(path = "/example",consumes = "application/json")
    public void getReading(@Valid @RequestBody MeterDto meterDto) throws JsonProcessingException {
        meterService.save(meterDto);
    }

    @GetMapping(path = "/report")
    public ModelAndView report(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("report");
        modelAndView.addObject("reports",meterService.report());
        modelAndView.addObject("total",meterService.reportTotal());
        return modelAndView;
    }
}
