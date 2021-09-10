package alvarez.fernando.quartzscheduler.executor.job.configuration;

import alvarez.fernando.quartzscheduler.executor.job.RecentlyCreatedFakeOrderJob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.quartz.Job;

@Getter
@AllArgsConstructor
public enum Jobs implements JobIdentification {
	
	RECENTLY_CREATED_FAKE_ORDER_JOB(RecentlyCreatedFakeOrderJob.class, "fake-order.recently-created", "Process recently created Orders"),
	;
	
	private final Class<? extends Job> jobClass;
	
	private final String jobGroup;
	
	private final String jobDescription;
	
}