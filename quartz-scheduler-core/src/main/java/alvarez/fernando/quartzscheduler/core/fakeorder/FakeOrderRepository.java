package alvarez.fernando.quartzscheduler.core.fakeorder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface FakeOrderRepository extends JpaRepository<FakeOrder, Long>, FakeOrderStatsRepository {

	@Query("FROM FakeOrder AS FO LEFT JOIN FETCH FO.notifications ORDER BY FO.id DESC") //Ordering by ID because it's created too fast for having any difference ordering by creationDate
	List<FakeOrder> listAllOrdersByCreationDateDesc();

	@Query("FROM FakeOrder WHERE status = :status ORDER BY id ASC")
	List<FakeOrder> listAllOrdersByStatusOrderingByIdAsc(@Param("status") FakeOrder.Status status);

}