package alvarez.fernando.quartzscheduler.executor.job.configuration;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Configuration
public class JobConfig implements ApplicationListener<ApplicationReadyEvent> {
	
	private final Scheduler scheduler;
	
	private final Map<JobDetail, Set<? extends Trigger>> jobs;
	
	public JobConfig(Scheduler scheduler, Collection<JobScheduler> jobschedulers) {
		this.scheduler = scheduler;
		this.jobs = new LinkedHashMap<>(jobschedulers.size());
		
		for (JobScheduler jobscheduler : jobschedulers) {
			this.jobs.put(jobscheduler.getJobDetail(), jobscheduler.getTriggers());
		}
	}
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
		try {
			scheduler.scheduleJobs(this.jobs, true);
		} catch (SchedulerException e) {
			throw new BeanInstantiationException(this.getClass(), "Error while scheduling jobs", e);
		}
		log.info("{} Jobs scheduled", this.jobs.size());
	}
	
}