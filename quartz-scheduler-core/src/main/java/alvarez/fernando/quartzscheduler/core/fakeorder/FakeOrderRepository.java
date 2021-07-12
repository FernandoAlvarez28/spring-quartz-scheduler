package alvarez.fernando.quartzscheduler.core.fakeorder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface FakeOrderRepository extends JpaRepository<FakeOrder, Long> {

	@Query("FROM FakeOrder ORDER BY id DESC") //Ordering by ID because it's created too fast for having any difference ordering by creationDate
	List<FakeOrder> listAllOrdersByCreationDateDesc();

}