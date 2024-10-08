package com.example.springbatch.band.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class QuartzBandJob implements Job {

    private final JobLauncher jobLauncher;
    private final org.springframework.batch.core.Job batchJob;

    public QuartzBandJob(JobLauncher jobLauncher, @Qualifier("bandJob") org.springframework.batch.core.Job batchJob) {
        this.jobLauncher = jobLauncher;
        this.batchJob = batchJob;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addLong("uniqueness", System.nanoTime());
        JobParameters jobParameter = jobParametersBuilder.toJobParameters();

        try {
            jobLauncher.run(batchJob, jobParameter);
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }
}
