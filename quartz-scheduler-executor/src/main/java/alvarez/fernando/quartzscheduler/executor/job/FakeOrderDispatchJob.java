package alvarez.fernando.quartzscheduler.executor.job;

import alvarez.fernando.quartzscheduler.core.fakeorder.FakeOrder;
import alvarez.fernando.quartzscheduler.core.fakeorder.FakeOrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@AllArgsConstructor
@DisallowConcurrentExecution
public class FakeOrderDispatchJob extends QuartzJobBean {
	
	private final FakeOrderService fakeOrderService;
	
	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) {
		final List<FakeOrder> paidOrders = fakeOrderService.listPaidOrders();
		
		if (CollectionUtils.isEmpty(paidOrders)) {
			log.info("Nothing to process");
			return;
		}
		
		if (paidOrders.size() == 1) {
			log.info("1 paid FakeOrder to process");
		} else {
			log.info("{} paid FakeOrders to process", paidOrders.size());
		}
		
		fakeOrderService.updateStatus(paidOrders, FakeOrder.Status.SENT);
	}
	
}