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
import java.util.Random;

@Slf4j
@AllArgsConstructor
@DisallowConcurrentExecution
public class FakeOrderPaymentConfirmationJob extends QuartzJobBean {
	
	private static final float MINIMUM_CHANCE_TO_EXPIRE = 0.65f;
	
	private final Random random = new Random();
	
	private final FakeOrderService fakeOrderService;
	
	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) {
		final List<FakeOrder> recentlyCreatedOrders = fakeOrderService.listRecentlyCreatedOrders();
		
		if (CollectionUtils.isEmpty(recentlyCreatedOrders)) {
			log.info("Nothing to process");
			return;
		}
		
		if (recentlyCreatedOrders.size() == 1) {
			log.info("1 new FakeOrder to process");
		} else {
			log.info("{} new FakeOrders to process", recentlyCreatedOrders.size());
		}
		
		final float chance = this.random.nextFloat();
		
		if (chance >= MINIMUM_CHANCE_TO_EXPIRE) {
			final float ordersPercentageToExpire = this.random.nextFloat();
			final int quantityToExpire = (int) (recentlyCreatedOrders.size() * ordersPercentageToExpire);
			
			fakeOrderService.updateStatus(recentlyCreatedOrders.subList(0, quantityToExpire), FakeOrder.Status.EXPIRED);
			fakeOrderService.updateStatus(recentlyCreatedOrders.subList(quantityToExpire, recentlyCreatedOrders.size()), FakeOrder.Status.PAID);
			
		} else {
			fakeOrderService.updateStatus(recentlyCreatedOrders, FakeOrder.Status.PAID);
		}
	}
	
}