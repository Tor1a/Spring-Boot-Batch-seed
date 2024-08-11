package com.example.springbatch.mattermost.tasklet;

import com.example.springbatch.mattermost.utils.DateUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;


import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.*;


@Slf4j
@Component
public class MattermostWeeklyReportingTasklet implements Tasklet {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${mattermost.webhook-url}")
    private String webhookUrl;

    public MattermostWeeklyReportingTasklet(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public RepeatStatus execute(org.springframework.batch.core.StepContribution contribution, org.springframework.batch.core.scope.context.ChunkContext chunkContext) throws Exception {
        Date date = new Date();

        String messageText = DateUtils.getCurrentWeekOfMonth(date) +" 주간보고 쓰레드 @here";

        Map<String, String> message = new HashMap<>();
        message.put("text", messageText);
        String jsonMessage = objectMapper.writeValueAsString(message);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> request = new HttpEntity<>(jsonMessage, headers);

        try {
            String response = restTemplate.postForObject(webhookUrl, request, String.class);
            log.info("Message sent successfully: {}", response);
        } catch (Exception e) {
            log.error("Error sending message: ", e);
        }
        log.info("Run MattermostWeeklyReportingTasklet");

        return RepeatStatus.FINISHED;
    }
}
