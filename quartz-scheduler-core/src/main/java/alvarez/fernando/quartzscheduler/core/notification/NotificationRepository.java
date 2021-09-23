package alvarez.fernando.quartzscheduler.core.notification;

import alvarez.fernando.quartzscheduler.core.fakeorder.FakeOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

interface NotificationRepository extends JpaRepository<Notification, Long> {

	@Query("FROM Notification WHERE fakeOrder = :fakeOrder ORDER BY id DESC")
	List<Notification> listByFakeOrder(@Param("fakeOrder") FakeOrder fakeOrder);

}