package alvarez.fernando.quartzscheduler.executor.job;

import alvarez.fernando.quartzscheduler.core.fakeorder.FakeOrder;
import alvarez.fernando.quartzscheduler.core.fakeorder.FakeOrderService;
import lombok.AllArgsConstructor;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Random;

@AllArgsConstructor
public class FakeOrderDeliveryTrackingJob extends QuartzJobBean {
	
	private static final float MINIMUM_CHANCE_TO_DELIVER = 0.45f;
	
	private final Random random = new Random();
	
	private final FakeOrderService fakeOrderService;
	
	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) {
		final List<FakeOrder> sentOrders = fakeOrderService.listSentOrders();
		
		if (CollectionUtils.isEmpty(sentOrders)) {
			return;
		}
		
		final float chance = this.random.nextFloat();
		
		if (chance >= MINIMUM_CHANCE_TO_DELIVER) {
			final float ordersPercentageToDeliver = this.random.nextFloat();
			final int quantityToDeliver = (int) (sentOrders.size() * ordersPercentageToDeliver);
			
			fakeOrderService.updateStatus(sentOrders.subList(0, quantityToDeliver), FakeOrder.Status.DELIVERED);
			
		} else {
			fakeOrderService.updateStatus(sentOrders, FakeOrder.Status.DELIVERED);
		}
	}
	
}