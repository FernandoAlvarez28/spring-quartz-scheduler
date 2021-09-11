package alvarez.fernando.quartzscheduler.executor.job.configuration;

import alvarez.fernando.quartzscheduler.executor.job.FakeOrderPaymentConfirmationJob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.quartz.Job;

@Getter
@AllArgsConstructor
public enum Jobs implements JobIdentification {
	
	FAKE_ORDER_PAYMENT_CONFIRMATION_JOB(FakeOrderPaymentConfirmationJob.class, "fake-order.payment-confirmation", "Confirm Orders payment"),
	;
	
	private final Class<? extends Job> jobClass;
	
	private final String jobGroup;
	
	private final String jobDescription;
	
}