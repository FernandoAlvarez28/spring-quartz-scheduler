package alvarez.fernando.quartzscheduler.executor;

import alvarez.fernando.quartzscheduler.core.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = Constants.BASE_PACKAGE_WILDCARD)
@EnableJpaRepositories(basePackages = Constants.BASE_PACKAGE_WILDCARD)
@EntityScan(basePackages = Constants.BASE_PACKAGE_WILDCARD)
public class QuartzSchedulerExecutorApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuartzSchedulerExecutorApplication.class, args);
	}

}