package alvarez.fernando.quartzscheduler.executor.job.configuration;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.*;

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
		
		for (Map.Entry<JobDetail, Set<? extends Trigger>> entry : this.jobs.entrySet()) {
			final JobDetail jobDetail = entry.getKey();
			final Set<? extends Trigger> triggers = entry.getValue();
			
			final List<String> triggerAsString = new ArrayList<>(triggers.size());
			for (Trigger trigger : triggers) {
				if (trigger instanceof CronTrigger) {
					triggerAsString.add(((CronTrigger) trigger).getCronExpression());
				} else {
					triggerAsString.add(trigger.toString());
				}
			}
			
			log.info("Job {} scheduled for \"{}\": \"{}\"", jobDetail.getJobClass(), StringUtils.collectionToCommaDelimitedString(triggerAsString), jobDetail.getDescription());
		}
		
	}
	
}