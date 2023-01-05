package com.example.exerciseseven.service;

import com.example.exerciseseven.dao.MeterDao;
import com.example.exerciseseven.dao.MeterDataDao;
import com.example.exerciseseven.dao.MeterGroupDao;
import com.example.exerciseseven.dto.MeterDto;
import com.example.exerciseseven.dto.ReportDto;
import com.example.exerciseseven.entity.Meter;
import com.example.exerciseseven.entity.MeterData;
import com.example.exerciseseven.entity.MeterGroup;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {MeterService.class})
@ExtendWith(SpringExtension.class)
class MeterServiceTest {
    @MockBean
    private MeterDao meterDao;

    @MockBean
    private MeterDataDao meterDataDao;

    @MockBean
    private MeterGroupDao meterGroupDao;

    @Autowired
    private MeterService meterService;


    @Test
    void testSave() {
        MeterDto meterDto = new MeterDto();
        meterDto.setCurrentReading(10.0d);
        meterDto.setDateTime(LocalDateTime.of(2022, 10, 26, 10, 55, 0,0));
        meterDto.setMeterGroup("Name");
        meterDto.setMeterId(1L);
        meterService.save(meterDto);
    }


    @Test
    void testReport() {
        MeterGroup meterGroup = new MeterGroup();
        meterGroup.setId(1L);
        meterGroup.setName("Name");

        ArrayList<MeterGroup> meterGroupList = new ArrayList<>();
        meterGroupList.add(meterGroup);
        when(meterGroupDao.findAll()).thenReturn(meterGroupList);

        Meter meter = new Meter();
        meter.setId(1L);
        meter.setMeterGroupId(1L);
        meter.setType("Type");

        ArrayList<Meter> meterList = new ArrayList<>();
        meterList.add(meter);
        when(meterDao.findAllByMeterGroupId(anyLong())).thenReturn(meterList);

        MeterData meterData = new MeterData();
        meterData.setDateOfData(LocalDateTime.of(1, 1, 1, 1, 1));
        meterData.setId(1L);
        meterData.setMeterId(1L);
        meterData.setReading(10.0d);

        MeterData meterData1 = new MeterData();
        meterData1.setDateOfData(LocalDateTime.of(1, 1, 1, 1, 1));
        meterData1.setId(1L);
        meterData1.setMeterId(1L);
        meterData1.setReading(10.0d);
        when(meterDataDao.findLastByMeterIdAndDate(anyLong(), (LocalDate) any())).thenReturn(meterData1);
        when(meterDataDao.findFirstByMeterIdAndDate(anyLong(), (LocalDate) any())).thenReturn(meterData);
        Map<String, List<ReportDto>> actualReportResult = meterService.report();
        assertEquals(1, actualReportResult.size());
        List<ReportDto> getResult = actualReportResult.get("Name");
        assertEquals(1, getResult.size());
        ReportDto getResult1 = getResult.get(0);
        assertEquals(10.0d, getResult1.getFirstReading().doubleValue());
        assertEquals("Type", getResult1.getMeterType());
        assertEquals(1L, getResult1.getMeterId().longValue());
        assertEquals(10.0d, getResult1.getLastReading().doubleValue());
        verify(meterGroupDao).findAll();
        verify(meterDao).findAllByMeterGroupId(anyLong());
        verify(meterDataDao).findFirstByMeterIdAndDate(anyLong(), (LocalDate) any());
        verify(meterDataDao).findLastByMeterIdAndDate(anyLong(), (LocalDate) any());
    }

    @Test
    void testReportTotal() {
        MeterGroup meterGroup = new MeterGroup();
        meterGroup.setId(1L);
        meterGroup.setName("all");

        ArrayList<MeterGroup> meterGroupList = new ArrayList<>();
        meterGroupList.add(meterGroup);
        when(meterGroupDao.findAll()).thenReturn(meterGroupList);

        Meter meter = new Meter();
        meter.setId(1L);
        meter.setMeterGroupId(1L);
        meter.setType("all");

        ArrayList<Meter> meterList = new ArrayList<>();
        meterList.add(meter);
        when(meterDao.findAllByMeterGroupId(anyLong())).thenReturn(meterList);

        MeterData meterData = new MeterData();
        meterData.setDateOfData(LocalDateTime.of(1, 1, 1, 1, 1));
        meterData.setId(1L);
        meterData.setMeterId(1L);
        meterData.setReading(10.0d);

        MeterData meterData1 = new MeterData();
        meterData1.setDateOfData(LocalDateTime.of(1, 1, 1, 1, 1));
        meterData1.setId(1L);
        meterData1.setMeterId(1L);
        meterData1.setReading(10.0d);
        when(meterDataDao.findLastByMeterIdAndDate(anyLong(), (LocalDate) any())).thenReturn(meterData1);
        when(meterDataDao.findFirstByMeterIdAndDate(anyLong(), (LocalDate) any())).thenReturn(meterData);
        Map<String, Double> actualReportTotalResult = meterService.reportTotal();
        assertEquals(1, actualReportTotalResult.size());
        Double expectedGetResult = 0.0d;
        assertEquals(expectedGetResult, actualReportTotalResult.get("all"));
        verify(meterGroupDao).findAll();
        verify(meterDao).findAllByMeterGroupId(anyLong());
        verify(meterDataDao).findFirstByMeterIdAndDate(anyLong(), any());
        verify(meterDataDao).findLastByMeterIdAndDate(anyLong(), any());
    }
}

