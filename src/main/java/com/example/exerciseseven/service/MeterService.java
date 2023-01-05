package com.example.exerciseseven.service;

import com.example.exerciseseven.dao.MeterDao;
import com.example.exerciseseven.dao.MeterDataDao;
import com.example.exerciseseven.dao.MeterGroupDao;
import com.example.exerciseseven.dto.MeterDto;
import com.example.exerciseseven.dto.ReportDto;
import com.example.exerciseseven.entity.Meter;
import com.example.exerciseseven.entity.MeterData;
import com.example.exerciseseven.entity.MeterGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MeterService {

    private final MeterGroupDao meterGroupDao;

    private final MeterDao meterDao;

    private final MeterDataDao meterDataDao;

    public void save(MeterDto meterDto){
        Meter meter = new Meter();
        meter.setId(meterDto.getMeterId());
        meter.setType(meterDto.getType());
        MeterGroup meterGroup = new MeterGroup();
        meterGroup.setName(meterDto.getMeterGroup());
        MeterData meterData = new MeterData();
        meterData.setMeterId(meterDto.getMeterId());
        meterData.setDateOfData(meterDto.getDateTime());
        meterData.setReading(meterDto.getCurrentReading());

        if (!meterGroupDao.existsByName(meterGroup.getName())) {
            meterGroupDao.save(meterGroup);
        }
        if (!meterDao.existsById(meter.getId())) {
            meter.setMeterGroupId(meterGroupDao.findById(meterGroup.getName()));
            meterDao.save(meter);
        }
        meterDataDao.save(meterData);

    }

    public Map<String, List<ReportDto>> report() {
        Map<String, List<ReportDto>> map = new LinkedHashMap<>();
        LocalDate date = LocalDate.of(2022, 11, 23);
        LocalDate firstDay = date.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDay = date.with(TemporalAdjusters.lastDayOfMonth());
        List<MeterGroup> meterGroups = meterGroupDao.findAll();
        for (MeterGroup meterGroup : meterGroups) {
            List<ReportDto> reportDtoList = new ArrayList<>();
            List<Meter> meters = meterDao.findAllByMeterGroupId(meterGroup.getId());
            for (Meter meter : meters) {
                ReportDto reportDto = new ReportDto();
                reportDto.setMeterId(meter.getId());
                reportDto.setMeterType(meter.getType());
                reportDto.setFirstReading(meterDataDao.findFirstByMeterIdAndDate(meter.getId(), firstDay).getReading());
                reportDto.setLastReading(meterDataDao.findLastByMeterIdAndDate(meter.getId(), lastDay).getReading());
                reportDtoList.add(reportDto);
            }
            map.put(meterGroup.getName(), reportDtoList);
        }
        return map;
    }

    public Map<String, Double> reportTotal() {
        Map<String, Double> map = new LinkedHashMap<>();
        LocalDate date = LocalDate.of(2022, 11, 23);
        LocalDate firstDay = date.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDay = date.with(TemporalAdjusters.lastDayOfMonth());
        List<MeterGroup> meterGroups = meterGroupDao.findAll();
        double all = 0.0;
        for (MeterGroup meterGroup : meterGroups) {
            double total = 0.0;
            List<Meter> meters = meterDao.findAllByMeterGroupId(meterGroup.getId());
            for (Meter meter : meters) {
                double used = 0.0;
                Double first = meterDataDao.findFirstByMeterIdAndDate(meter.getId(), firstDay).getReading();
                Double last = meterDataDao.findLastByMeterIdAndDate(meter.getId(), lastDay).getReading();
                used = last - first;
                total += used;
            }
            all += total;
            map.put(meterGroup.getName(), total);
        }
        map.put("all", all);
        return map;
    }
}
