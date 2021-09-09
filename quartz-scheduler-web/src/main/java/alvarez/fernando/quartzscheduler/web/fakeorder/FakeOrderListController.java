package alvarez.fernando.quartzscheduler.web.fakeorder;

import alvarez.fernando.quartzscheduler.core.fakeorder.FakeOrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@AllArgsConstructor
public class FakeOrderListController {
	
	private final FakeOrderService fakeOrderService;
	
	@GetMapping({"/", "/orders"})
	public ModelAndView home() {
		final ModelAndView modelAndView = new ModelAndView("home");
		
		modelAndView.addObject("orders", fakeOrderService.listAllOrdersByCreationDateDesc());
		
		return modelAndView;
	}
	
}