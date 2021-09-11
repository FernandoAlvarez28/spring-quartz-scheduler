package alvarez.fernando.quartzscheduler.executor.job;

import alvarez.fernando.quartzscheduler.executor.job.configuration.JobScheduler;
import alvarez.fernando.quartzscheduler.executor.job.configuration.Jobs;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Component
public class RecentlyCreatedFakeOrderJobSchedulerImpl extends JobScheduler {
	
	private final JobDetail jobDetail;
	
	private final Trigger trigger;
	
	public RecentlyCreatedFakeOrderJobSchedulerImpl() {
		super(Jobs.RECENTLY_CREATED_FAKE_ORDER_JOB);
		
		this.jobDetail = JobBuilder.newJob(super.identification.getJobClass())
				.withIdentity(UUID.randomUUID().toString(), super.identification.getJobGroup())
				.withDescription(super.identification.getJobDescription())
				.requestRecovery(true)
				.storeDurably()
				.build();
		
		this.trigger = TriggerBuilder.newTrigger()
				.forJob(this.jobDetail)
				.withIdentity(this.jobDetail.getKey().getName(), super.identification.getTriggerGroup())
				.withDescription("Repeat every minute")
				.withSchedule(CronScheduleBuilder.cronSchedule("0 * * * * ?").withMisfireHandlingInstructionFireAndProceed())
				.build();
	}
	
	@Override
	public JobDetail getJobDetail() {
		return this.jobDetail;
	}
	
	@Override
	public Set<Trigger> getTriggers() {
		return Collections.singleton(this.trigger);
	}
	
}