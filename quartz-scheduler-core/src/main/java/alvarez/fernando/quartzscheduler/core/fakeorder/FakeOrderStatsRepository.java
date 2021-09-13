package alvarez.fernando.quartzscheduler.core.fakeorder;

import java.util.Map;

interface FakeOrderStatsRepository {
	
	Map<FakeOrder.Status, Long> countByStatus();
	
}