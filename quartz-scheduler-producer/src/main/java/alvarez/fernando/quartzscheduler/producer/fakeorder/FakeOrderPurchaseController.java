package alvarez.fernando.quartzscheduler.producer.fakeorder;

import alvarez.fernando.quartzscheduler.core.fakeorder.FakeOrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@AllArgsConstructor
public class FakeOrderPurchaseController {
	
	private final FakeOrderService fakeOrderService;
	
	@PostMapping("/orders")
	public ModelAndView purchaseOrders(int quantity) {
		fakeOrderService.createMany(quantity);
		
		return new ModelAndView("redirect:/");
	}
	
}