package lee.joohan.taskschedulerexample.configs;

import lee.joohan.taskschedulerexample.tasklets.MyTaskOne;
import lee.joohan.taskschedulerexample.tasklets.MyTaskTwo;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class BatchConfig {
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step stepOne() {
        return stepBuilderFactory.get("stepOne")
                .tasklet(new MyTaskOne())
                .build();
    }

    @Bean
    public Step stepTwo() {
        return stepBuilderFactory.get("stepTwo")
                .tasklet(new MyTaskTwo())
                .build();
    }

    @Bean(name="demoJobOne")
    public Job DemoJobOne() {
        return jobBuilderFactory.get("demoJobOne")
                .start(stepOne())
                .next(stepTwo())
                .build();
    }

    @Bean(name="demoJobTwo")
    public Job demoJobTwo() {
        return jobBuilderFactory.get("demoJobTwo")
                .start(stepOne())
                .build();
    }
}
