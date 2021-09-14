package alvarez.fernando.quartzscheduler.core.fakeorder;

import alvarez.fernando.quartzscheduler.core.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class FakeOrderService {
	
	private final FakeOrderRepository fakeOrderRepository;
	
	private final NotificationService notificationService;
	
	public List<FakeOrder> createMany(int quantity) {
		final List<FakeOrder> fakeOrders = new ArrayList<>(quantity);
		
		for (int i = 0; i < quantity; i++) {
			fakeOrders.add(new FakeOrder());
		}
		
		fakeOrderRepository.saveAll(fakeOrders);
		
		return fakeOrders;
	}
	
	public void updateStatus(Collection<FakeOrder> fakeOrders, FakeOrder.Status newStatus) {
		if (CollectionUtils.isEmpty(fakeOrders)) {
			return;
		}
		
		for (FakeOrder fakeOrder : fakeOrders) {
			log.info("Updating FakeOrder {}'s status to {}", fakeOrder.getId(), newStatus);
			fakeOrder.updateStatus(newStatus);
		}
		
		fakeOrderRepository.saveAll(fakeOrders);
		
		try {
			for (FakeOrder fakeOrder : fakeOrders) {
				notificationService.notifyAboutChangedStatus(fakeOrder);
			}
		} catch (Exception e) {
			log.error("Error while sending notifications", e);
		}
	}
	
	public List<FakeOrder> listAllOrdersByCreationDateDesc() {
		return fakeOrderRepository.listAllOrdersByCreationDateDesc();
	}
	
	public List<FakeOrder> listRecentlyCreatedOrders() {
		return fakeOrderRepository.listAllOrdersByStatusOrderingByIdAsc(FakeOrder.Status.NEW);
	}
	
	public List<FakeOrder> listPaidOrders() {
		return fakeOrderRepository.listAllOrdersByStatusOrderingByIdAsc(FakeOrder.Status.PAID);
	}
	
	public List<FakeOrder> listSentOrders() {
		return fakeOrderRepository.listAllOrdersByStatusOrderingByIdAsc(FakeOrder.Status.SENT);
	}
	
	public Map<FakeOrder.Status, Long> countByStatus() {
		return fakeOrderRepository.countByStatus();
	}
	
}