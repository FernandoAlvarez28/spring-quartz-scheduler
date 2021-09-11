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
public class FakeOrderPaymentConfirmationJob extends QuartzJobBean {
	
	private static final float CHANCE_TO_EXPIRE = 0.35f;
	
	private final Random random = new Random();
	
	private final FakeOrderService fakeOrderService;
	
	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) {
		final List<FakeOrder> recentlyCreatedOrders = fakeOrderService.listRecentlyCreatedOrders();
		
		if (CollectionUtils.isEmpty(recentlyCreatedOrders)) {
			return;
		}
		
		final float chance = this.random.nextFloat();
		
		if (chance >= (1 - CHANCE_TO_EXPIRE)) {
			final float ordersPercentageToExpire = this.random.nextFloat();
			final int quantityToExpire = (int) (recentlyCreatedOrders.size() * ordersPercentageToExpire);
			
			fakeOrderService.updateStatus(recentlyCreatedOrders.subList(0, quantityToExpire), FakeOrder.Status.EXPIRED);
			fakeOrderService.updateStatus(recentlyCreatedOrders.subList(quantityToExpire, recentlyCreatedOrders.size()), FakeOrder.Status.PAID);
			
		} else {
			fakeOrderService.updateStatus(recentlyCreatedOrders, FakeOrder.Status.PAID);
		}
	}
	
}