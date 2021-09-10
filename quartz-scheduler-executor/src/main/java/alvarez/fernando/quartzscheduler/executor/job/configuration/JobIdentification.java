package alvarez.fernando.quartzscheduler.executor.job.configuration;

import org.quartz.Job;

public interface JobIdentification {
	
	Class<? extends Job> getJobClass();
	
	String getJobGroup();
	
	String getJobDescription();
	
	default String getTriggerGroup() {
		return this.getJobGroup() + ".trigger";
	}
	
}