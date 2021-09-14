package alvarez.fernando.quartzscheduler.core.notification;

import alvarez.fernando.quartzscheduler.core.fakeorder.FakeOrder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "NOTIFICATION")
public class Notification {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notificationSequenceGenerator")
	@SequenceGenerator(name="notificationSequenceGenerator", sequenceName = "NOTIFICATION_ID_SEQ")
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE", nullable = false)
	private Date date;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FAKE_ORDER_ID", nullable = false, referencedColumnName = "ID", foreignKey = @ForeignKey(name = "NOTIFICATION_FAKE_ORDER_ID_FK"))
	private FakeOrder fakeOrder;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "FAKE_ORDER_STATUS", nullable = false)
	private FakeOrder.Status fakeOrderStatus;
	
	public Notification() {
	
	}
	
	public Notification(FakeOrder fakeOrder) {
		this.date = new Date();
		this.fakeOrder = fakeOrder;
		this.fakeOrderStatus = fakeOrder.getStatus();
	}
	
}
