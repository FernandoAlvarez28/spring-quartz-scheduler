package alvarez.fernando.quartzscheduler.executor.job;

import alvarez.fernando.quartzscheduler.core.fakeorder.FakeOrder;
import alvarez.fernando.quartzscheduler.core.fakeorder.FakeOrderService;
import lombok.AllArgsConstructor;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

@AllArgsConstructor
public class FakeOrderPaymentConfirmationJob extends QuartzJobBean {
	
	private final FakeOrderService fakeOrderService;
	
	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) {
		final List<FakeOrder> recentlyCreatedOrders = fakeOrderService.listRecentlyCreatedOrders();
		
		fakeOrderService.updateStatus(recentlyCreatedOrders, FakeOrder.Status.PAID);
	}
	
}