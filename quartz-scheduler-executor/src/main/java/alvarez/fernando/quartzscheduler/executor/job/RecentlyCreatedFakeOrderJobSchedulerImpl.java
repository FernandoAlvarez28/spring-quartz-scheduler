package alvarez.fernando.quartzscheduler.executor.job;

import alvarez.fernando.quartzscheduler.executor.job.configuration.JobScheduler;
import alvarez.fernando.quartzscheduler.executor.job.configuration.Jobs;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.util.Calendar;
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
		
		final Calendar startDate = Calendar.getInstance();
		startDate.set(Calendar.SECOND, 0);
		startDate.set(Calendar.MILLISECOND, 0);
		startDate.set(Calendar.MINUTE, startDate.get(Calendar.MINUTE) + 2);
		
		this.trigger = TriggerBuilder.newTrigger()
				.forJob(this.jobDetail)
				.withIdentity(this.jobDetail.getKey().getName(), super.identification.getTriggerGroup())
				.withDescription("Repeat every minute")
				.startAt(startDate.getTime())
				.withSchedule(SimpleScheduleBuilder.repeatMinutelyForever().withMisfireHandlingInstructionFireNow())
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