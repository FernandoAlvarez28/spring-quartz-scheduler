package alvarez.fernando.quartzscheduler.executor.job.configuration;

import alvarez.fernando.quartzscheduler.executor.job.FakeOrderDeliveryTrackingJob;
import alvarez.fernando.quartzscheduler.executor.job.FakeOrderDispatchJob;
import alvarez.fernando.quartzscheduler.executor.job.FakeOrderPaymentConfirmationJob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.quartz.Job;

@Getter
@AllArgsConstructor
public enum Jobs implements JobIdentification {
	
	FAKE_ORDER_PAYMENT_CONFIRMATION_JOB(FakeOrderPaymentConfirmationJob.class, "fake-order.payment-confirmation", "Confirm Orders payment"),
	FAKE_ORDER_DISPATCH_JOB(FakeOrderDispatchJob.class, "fake-order.dispatch", "Dispatch/Send paid Orders"),
	FAKE_ORDER_DELIVERY_TRACKING_JOB(FakeOrderDeliveryTrackingJob.class, "fake-order.delivery-tracking", "Track Orders delivery"),
	;
	
	private final Class<? extends Job> jobClass;
	
	private final String jobGroup;
	
	private final String jobDescription;
	
}