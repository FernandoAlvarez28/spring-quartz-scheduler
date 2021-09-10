package alvarez.fernando.quartzscheduler.executor.job.configuration;

import lombok.AllArgsConstructor;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import java.util.Set;

/**
 * Interface to provide a {@link JobDetail} and its {@link Trigger Triggers} ready to be scheduled.
 */
@AllArgsConstructor
public abstract class JobScheduler {
	
	protected final JobIdentification identification;

	protected abstract JobDetail getJobDetail();
	
	protected abstract Set<Trigger> getTriggers();
	
}