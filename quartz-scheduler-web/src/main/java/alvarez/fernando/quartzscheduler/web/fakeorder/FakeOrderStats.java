package alvarez.fernando.quartzscheduler.web.fakeorder;

import alvarez.fernando.quartzscheduler.core.fakeorder.FakeOrder;
import lombok.Getter;

import java.awt.*;
import java.util.List;
import java.util.*;

public class FakeOrderStats {

	private final LinkedHashMap<FakeOrder.Status, Long> statusQuantityMap;
	
	FakeOrderStats(Map<FakeOrder.Status, Long> map) {
		this.statusQuantityMap = new LinkedHashMap<>(map.size());
		
		for (FakeOrder.Status status : FakeOrder.Status.values()) {
			final Long quantity = map.get(status);
			this.statusQuantityMap.put(status, quantity != null ? quantity : 0);
		}
	}
	
	public Collection<String> getStatus() {
		final List<String> statusDescriptions = new ArrayList<>(this.statusQuantityMap.size());
		for (FakeOrder.Status status : this.statusQuantityMap.keySet()) {
			statusDescriptions.add(status.getDescription());
		}
		return statusDescriptions;
	}
	
	public Collection<Long> getQuantities() {
		return this.statusQuantityMap.values();
	}
	
	public Collection<ColorVO> getColors() {
		final List<ColorVO> colors = new ArrayList<>(this.statusQuantityMap.size());
		for (FakeOrder.Status status : this.statusQuantityMap.keySet()) {
			colors.add(new ColorVO(status.getColor()));
		}
		return colors;
	}
	
	@Getter
	public static class ColorVO {
		
		private final int red;
		private final int green;
		private final int blue;
		
		private ColorVO(Color color) {
			this.red = color.getRed();
			this.green = color.getGreen();
			this.blue = color.getBlue();
		}
		
	}

}