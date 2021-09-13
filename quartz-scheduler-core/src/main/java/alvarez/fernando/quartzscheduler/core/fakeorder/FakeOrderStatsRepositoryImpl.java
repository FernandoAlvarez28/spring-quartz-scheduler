package alvarez.fernando.quartzscheduler.core.fakeorder;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
class FakeOrderStatsRepositoryImpl implements FakeOrderStatsRepository {
	
	private final EntityManager entityManager;
	
	@Override
	public Map<FakeOrder.Status, Long> countByStatus() {
		return entityManager
				.createQuery("SELECT status AS key, COUNT(*) AS value FROM FakeOrder GROUP BY key", Tuple.class)
				.getResultStream()
				.collect(
						Collectors.toMap(
								tuple -> ((FakeOrder.Status) tuple.get("key")),
								tuple -> (Long) tuple.get("value")
						)
				);
	}
}
