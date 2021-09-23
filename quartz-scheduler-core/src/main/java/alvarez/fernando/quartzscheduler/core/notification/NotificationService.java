package alvarez.fernando.quartzscheduler.core.notification;

import alvarez.fernando.quartzscheduler.core.fakeorder.FakeOrder;

import java.util.List;

public interface NotificationService {

	void notifyAboutChangedStatus(FakeOrder fakeOrder);
	
	List<Notification> listByFakeOrder(FakeOrder fakeOrder);

}