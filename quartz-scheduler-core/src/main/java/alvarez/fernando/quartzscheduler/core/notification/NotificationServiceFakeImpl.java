package alvarez.fernando.quartzscheduler.core.notification;

import alvarez.fernando.quartzscheduler.core.fakeorder.FakeOrder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * {@link NotificationService}'s implementation that actually doesn't notify anything, just pretend for demonstration purposes.
 */
@Slf4j
@Service
@AllArgsConstructor
public class NotificationServiceFakeImpl implements NotificationService {
	
	private final NotificationRepository notificationRepository;
	
	@Override
	public void notifyAboutChangedStatus(FakeOrder fakeOrder) {
		log.info("Notifying FakeOrder {} about the current status: {}", fakeOrder.getId(), fakeOrder.getStatus());
		notificationRepository.save(new Notification(fakeOrder));
		
		try {
			//Just pretending some SMS/e-mail/anything service integration to make Jobs take longer to complete executions
			Thread.sleep(250);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	@Override
	public List<Notification> listByFakeOrder(FakeOrder fakeOrder) {
		return notificationRepository.listByFakeOrder(fakeOrder);
	}
	
}