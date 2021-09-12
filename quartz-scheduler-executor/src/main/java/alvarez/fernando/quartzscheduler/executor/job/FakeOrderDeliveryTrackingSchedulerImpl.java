package alvarez.fernando.quartzscheduler.executor.job;

import alvarez.fernando.quartzscheduler.executor.job.configuration.JobScheduler;
import alvarez.fernando.quartzscheduler.executor.job.configuration.Jobs;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

@Component
public class FakeOrderDeliveryTrackingSchedulerImpl extends JobScheduler {
	
	private final JobDetail jobDetail;
	
	private final Trigger trigger;
	
	public FakeOrderDeliveryTrackingSchedulerImpl() {
		super(Jobs.FAKE_ORDER_DELIVERY_TRACKING_JOB);
		
		this.jobDetail = JobBuilder.newJob(super.identification.getJobClass())
				//Must use a unique ID for the Jobs (e.g.: don't use "UUID.randomUUID()")
				//or else a new job is created in the database at every Executor startup
				.withIdentity(super.identification.toString(), super.identification.getJobGroup())
				.withDescription(super.identification.getJobDescription())
				.requestRecovery(true)
				.storeDurably()
				.build();
		
		this.trigger = TriggerBuilder.newTrigger()
				.forJob(this.jobDetail)
				//The trigger's ID must be unique too, but only among the other triggers (so you can use the same Job ID)
				.withIdentity(this.jobDetail.getKey().getName(), super.identification.getTriggerGroup())
				.withDescription("Repeat every 3 minutes")
				.withSchedule(CronScheduleBuilder.cronSchedule("0 0/3 * * * ?").withMisfireHandlingInstructionFireAndProceed())
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