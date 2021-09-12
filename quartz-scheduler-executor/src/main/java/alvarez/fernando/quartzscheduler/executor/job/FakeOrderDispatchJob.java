package alvarez.fernando.quartzscheduler.executor.job;

import alvarez.fernando.quartzscheduler.core.fakeorder.FakeOrder;
import alvarez.fernando.quartzscheduler.core.fakeorder.FakeOrderService;
import lombok.AllArgsConstructor;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.CollectionUtils;

import java.util.List;

@AllArgsConstructor
public class FakeOrderDispatchJob extends QuartzJobBean {
	
	private final FakeOrderService fakeOrderService;
	
	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) {
		final List<FakeOrder> paidOrders = fakeOrderService.listPaidOrders();
		
		if (CollectionUtils.isEmpty(paidOrders)) {
			return;
		}
		
		fakeOrderService.updateStatus(paidOrders, FakeOrder.Status.SENT);
	}
	
}