package alvarez.fernando.quartzscheduler.core.fakeorder;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class FakeOrderService {
	
	private final FakeOrderRepository fakeOrderRepository;
	
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
			fakeOrder.updateStatus(newStatus);
		}
		
		fakeOrderRepository.saveAll(fakeOrders);
	}
	
	public List<FakeOrder> listAllOrdersByCreationDateDesc() {
		return fakeOrderRepository.listAllOrdersByCreationDateDesc();
	}
	
	public List<FakeOrder> listRecentlyCreatedOrders() {
		return fakeOrderRepository.listAllOrdersByStatusOrderingByIdAsc(FakeOrder.Status.NEW);
	}
	
}