package lee.joohan.taskschedulerexample.batch.writer;

import java.util.List;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

/**
 * Created by Joohan Lee on 2019/12/27
 */

@Component
@StepScope
public class GroupCustomerCareerWriter implements ItemWriter<Object[]> {


  @Override
  public void write(List<? extends Object[]> items) {
    for (Object[] item : items) {
        String job = (String) item[0];
        long count = (Long) item[1];

      String output = String.format("First: [%s], Second [%s]", job, count);

      System.out.println(output);
    }
  }
}