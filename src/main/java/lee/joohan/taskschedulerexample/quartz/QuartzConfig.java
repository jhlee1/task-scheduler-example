package lee.joohan.taskschedulerexample.quartz;

import lee.joohan.taskschedulerexample.batch.BatchName;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Properties;

@RequiredArgsConstructor
public class QuartzConfig {
    private static final String JOB_NAME_PARAM = "jobName";
    private static final String JOB_LAUNCHER_PARAM = "jobLauncher";
    private static final String JOB_LOCATOR_PARAM = "jobLocator";

    private final JobLauncher jobLauncher;
    private final JobLocator jobLocator;

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();

        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);

        return jobRegistryBeanPostProcessor;
    }

    public Trigger jobOneTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder
            .simpleSchedule()
            .withIntervalInSeconds(10)
            .repeatForever();

        return TriggerBuilder
            .newTrigger()
            .forJob(jobOneDetail())
            .withIdentity("jobOneTrigger")
            .withSchedule(scheduleBuilder)
            .build();
    }

    public JobDetail jobOneDetail() {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobName", "demoJobOne"); // Quartz Job으로 변경해보기
        jobDataMap.put("jobLauncher", jobLauncher);
        jobDataMap.put("jobLocator", jobLocator);

        return JobBuilder.newJob(QuartzJob.class)
                .withIdentity("demoJobOne") //Quartz Job으로 변경해보기
                .setJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    public Trigger jobTwoTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder
                .simpleSchedule()
                .withIntervalInSeconds(20)
                .repeatForever();

        return TriggerBuilder
                .newTrigger()
                .forJob(jobTwoDetail())
                .withIdentity("jobTwoTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }

    public JobDetail jobTwoDetail() {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("jobName", "demoJobTwo");
        jobDataMap.put("jobLauncher", jobLauncher);
        jobDataMap.put("jobLocator", jobLocator);

        return JobBuilder.newJob(QuartzJob.class)
            .withIdentity("demoJobTwo")
            .setJobData(jobDataMap)
            .storeDurably()
            .build();
    }


    public Trigger groupCustomerCareerJobTrigger() {
        // Every day at 10am
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
            .dailyAtHourAndMinute(10, 0);

        return TriggerBuilder.newTrigger()
            .forJob(groupCustomerCareerJobDetail())
            .withIdentity("GroupCustomerCareerJobTrigger")
            .withSchedule(cronScheduleBuilder)
            .build();
    }

    public JobDetail groupCustomerCareerJobDetail() {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put(JOB_NAME_PARAM, BatchName.GROUP_CUSTOMER_CAREER_JOB);
        jobDataMap.put(JOB_LAUNCHER_PARAM, jobLauncher);
        jobDataMap.put(JOB_LOCATOR_PARAM, jobLocator);

        return JobBuilder.newJob(QuartzJob.class)
            .withIdentity(BatchName.GROUP_CUSTOMER_CAREER_JOB)
            .setJobData(jobDataMap)
            .storeDurably()
            .build();
    }


    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {


        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();

        schedulerFactoryBean.setTriggers(jobOneTrigger(), jobTwoTrigger());
        schedulerFactoryBean.setJobDetails(jobOneDetail(), jobTwoDetail());
        schedulerFactoryBean.setQuartzProperties(quartzProperties());

        return schedulerFactoryBean;
    }

    @Bean
    public Scheduler scheduler() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        scheduler.start();
        scheduler.scheduleJob(jobOneDetail(), jobOneTrigger());
        scheduler.scheduleJob(jobTwoDetail(), jobTwoTrigger());
        scheduler.scheduleJob(groupCustomerCareerJobDetail(), groupCustomerCareerJobTrigger());

        return scheduler;
    }

    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();

        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();

        return propertiesFactoryBean.getObject();
    }

}
