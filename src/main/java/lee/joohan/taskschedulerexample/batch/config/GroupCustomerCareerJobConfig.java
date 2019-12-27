package lee.joohan.taskschedulerexample.batch.config;

import static lee.joohan.taskschedulerexample.batch.BatchName.GROUP_CUSTOMER_CAREER_JOB;

import lee.joohan.taskschedulerexample.batch.BatchSize;
import lee.joohan.taskschedulerexample.batch.reader.GroupCustomerCareerReader;
import lee.joohan.taskschedulerexample.batch.writer.GroupCustomerCareerWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GroupCustomerCareerJobConfig {
  private final GroupCustomerCareerReader groupCustomerCareerReader;
  private final GroupCustomerCareerWriter groupCustomerCareerWriter;
  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;

  @Bean(name = GROUP_CUSTOMER_CAREER_JOB)
  public Job groupCustomerCareer() {
    return jobBuilderFactory.get(GROUP_CUSTOMER_CAREER_JOB)
        .start(groupCustomerCareerStep())
        .build();
  }

  @Bean
  public Step groupCustomerCareerStep() {
    return stepBuilderFactory.get("groupCustomerCareerStep")
        .<Object[], Object[]>chunk(BatchSize.CUSTOMER_CAREER_GROUP)
        .reader(groupCustomerCareerReader)
        .writer(groupCustomerCareerWriter)
        .build();
  }
}
