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
			for (Map.Entry<JobDetail, Set<? extends Trigger>> entry : this.jobs.entrySet()) {
				final JobDetail jobDetail = entry.getKey();
				final Set<? extends Trigger> triggers = entry.getValue();
				
				if (!scheduler.checkExists(jobDetail.getKey())) {
					//Only needs to schedule the first time. Any other instance will execute the jobs by reading them from database
					scheduler.scheduleJob(jobDetail, triggers, true);
					this.printScheduledJob(jobDetail, triggers);
				} else {
					this.printConfiguredJob(jobDetail, triggers);
				}
			}
		} catch (SchedulerException e) {
			throw new BeanInstantiationException(this.getClass(), "Error while scheduling jobs", e);
		}
		
		log.info("{} Jobs configured", this.jobs.size());
		
	}
	
	private void printScheduledJob(JobDetail jobDetail, Set<? extends Trigger> triggers) {
		printJob(jobDetail, triggers, "scheduled");
	}
	
	private void printConfiguredJob(JobDetail jobDetail, Set<? extends Trigger> triggers) {
		printJob(jobDetail, triggers, "configured");
	}
	
	private void printJob(JobDetail jobDetail, Collection<? extends Trigger> triggers, String action) {
		final List<String> triggerAsString = new ArrayList<>(triggers.size());
		for (Trigger trigger : triggers) {
			if (trigger instanceof CronTrigger) {
				triggerAsString.add(((CronTrigger) trigger).getCronExpression());
			} else {
				triggerAsString.add(trigger.toString());
			}
		}
		
		log.info("Job {} {} for \"{}\": \"{}\"", jobDetail.getJobClass(), action, StringUtils.collectionToCommaDelimitedString(triggerAsString), jobDetail.getDescription());
	}
	
}