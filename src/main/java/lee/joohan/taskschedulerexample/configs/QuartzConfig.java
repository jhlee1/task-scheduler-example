package lee.joohan.taskschedulerexample.configs;

import lee.joohan.taskschedulerexample.jobs.CustomQuartzJob;
import lombok.AllArgsConstructor;
import org.quartz.*;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Properties;

@AllArgsConstructor
@Configuration
public class QuartzConfig {
    private JobLauncher jobLauncher;
    private JobLocator jobLocator;

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();

        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);

        return jobRegistryBeanPostProcessor;
    }

    @Bean
    public JobDetail jobOneDetail() {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobName", "demoJobOne"); // Quartz Job으로 변경해보기
        jobDataMap.put("jobLauncher", jobLauncher);
        jobDataMap.put("jobLocator", jobLocator);

        return JobBuilder.newJob(CustomQuartzJob.class)
                .withIdentity("demoJobOne") //Quartz Job으로 변경해보기
                .setJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    @Bean
    public JobDetail jobTwoDetail() {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("jobName", "demoJobTwo");
        jobDataMap.put("jobLauncher", jobLauncher);
        jobDataMap.put("jobLocator", jobLocator);

        return JobBuilder.newJob(CustomQuartzJob.class)
                .withIdentity("demoJobTwo")
                .setJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    @Bean
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

    @Bean
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


    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();

        schedulerFactoryBean.setTriggers(jobOneTrigger(), jobTwoTrigger());
        schedulerFactoryBean.setQuartzProperties(quartzProperties());
        schedulerFactoryBean.setJobDetails(jobOneDetail(), jobTwoDetail());

        return schedulerFactoryBean;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();

        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();

        return propertiesFactoryBean.getObject();
    }

}
