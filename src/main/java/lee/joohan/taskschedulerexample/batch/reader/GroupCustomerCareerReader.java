package lee.joohan.taskschedulerexample.batch.reader;

import lee.joohan.taskschedulerexample.batch.BatchSize;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;

/**
 * Created by Joohan Lee on 2019/12/27
 */

@Component
@StepScope
public class GroupCustomerCareerReader extends JpaPagingItemReader {
  public GroupCustomerCareerReader(JpaTransactionManager jpaTransactionManager) {
    String query = new StringBuilder()
        .append("SELECT job, count(*) FROM customers ")
        .append("GROUP BY job")
        .toString();

    this.setName("GroupCustomerCareerReader");
    this.setEntityManagerFactory(jpaTransactionManager.getEntityManagerFactory());
    this.setPageSize(BatchSize.CUSTOMER_CAREER_GROUP);
    this.setQueryString(query);
  }
}
