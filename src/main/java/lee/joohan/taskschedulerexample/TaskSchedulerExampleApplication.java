package lee.joohan.taskschedulerexample;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class TaskSchedulerExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskSchedulerExampleApplication.class, args);
    }

}
