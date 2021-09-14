package alvarez.fernando.quartzscheduler.core.notification;

import alvarez.fernando.quartzscheduler.core.fakeorder.FakeOrder;

public interface NotificationService {

	void notifyAboutChangedStatus(FakeOrder fakeOrder);

}