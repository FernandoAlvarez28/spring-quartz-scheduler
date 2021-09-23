package alvarez.fernando.quartzscheduler.web.notification;

import alvarez.fernando.quartzscheduler.core.fakeorder.FakeOrder;
import alvarez.fernando.quartzscheduler.core.fakeorder.FakeOrderService;
import alvarez.fernando.quartzscheduler.core.notification.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@AllArgsConstructor
public class NotificationController {
	
	private final FakeOrderService fakeOrderService;
	
	private final NotificationService notificationService;
	
	@GetMapping({"/orders/{fakeOrderId}/notifications"})
	public ModelAndView home(@PathVariable Long fakeOrderId) {
		final ModelAndView modelAndView = new ModelAndView("notifications");
		
		final Optional<FakeOrder> fakeOrder = fakeOrderService.findById(fakeOrderId);
		
		if (!fakeOrder.isPresent()) {
			return new ModelAndView("redirect:/");
		}
		
		modelAndView.addObject("order", fakeOrder.get());
		modelAndView.addObject("notifications", notificationService.listByFakeOrder(fakeOrder.get()));
		
		return modelAndView;
	}
	
}