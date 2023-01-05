package com.example.exerciseseven.controller;

import com.example.exerciseseven.ExerciseSevenApplication;
import com.example.exerciseseven.dto.MeterDto;
import com.example.exerciseseven.service.MeterService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import org.apache.catalina.User;
import org.apache.catalina.realm.UserDatabaseRealm;
import org.apache.catalina.users.MemoryUserDatabase;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = ExerciseSevenApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MeterControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MeterController meterController;
    @MockBean
    private MeterService meterService;


    @Test
    void testGetReading() throws Exception {
        MeterDto meterDto = new MeterDto();
        meterDto.setCurrentReading(10.0d);
        meterDto.setDateTime(LocalDateTime.of(1, 1, 1, 1, 1));
        meterDto.setMeterGroup("Name");
        meterDto.setMeterId(1L);
        meterDto.setType("Type");        this.mockMvc.perform(
                        get("/api/v1.0/example")
                                .content(String.valueOf(meterDto))
                                .contentType("application/json"))
                .andExpect(status().is4xxClientError());
    }


    @Test
    void testReport() throws Exception {
        this.mockMvc.perform(
                        get("/api/v1.0/report"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}

