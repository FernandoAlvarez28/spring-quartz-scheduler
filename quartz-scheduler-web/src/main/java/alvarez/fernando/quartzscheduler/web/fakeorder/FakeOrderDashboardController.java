package alvarez.fernando.quartzscheduler.web.fakeorder;

import alvarez.fernando.quartzscheduler.core.fakeorder.FakeOrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class FakeOrderDashboardController {
	
	private final FakeOrderService fakeOrderService;
	
	@GetMapping("/orders/stats")
	public ResponseEntity<FakeOrderStats> stats() {
		return ResponseEntity.ok().body(new FakeOrderStats(fakeOrderService.countByStatus()));
	}
	
}