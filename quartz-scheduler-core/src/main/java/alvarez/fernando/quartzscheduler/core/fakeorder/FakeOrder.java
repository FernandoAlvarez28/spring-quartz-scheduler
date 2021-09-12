package alvarez.fernando.quartzscheduler.core.fakeorder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "FAKE_ORDER")
public class FakeOrder {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fakeOrderSequenceGenerator")
	@SequenceGenerator(name="fakeOrderSequenceGenerator", sequenceName = "FAKE_ORDER_ID_SEQ")
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATION_DATE", nullable = false)
	private Date creationDate = new Date();
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", nullable = false)
	private Status status = Status.NEW;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PROCESSED_DATE")
	private Date processedDate;
	
	public void updateStatus(Status newStatus) {
		if (this.status.equals(newStatus)) {
			return;
		}
		
		this.status = newStatus;
		
		if (Status.DELIVERED.equals(newStatus)) {
			this.processedDate = new Date();
		}
	}
	
	public boolean isExpired() {
		return Status.EXPIRED.equals(this.status);
	}
	
	public boolean isProcessed() {
		return Status.DELIVERED.equals(this.status);
	}
	
	@Getter
	@AllArgsConstructor
	public enum Status {
		
		NEW("New"),
		PAID("Paid"),
		EXPIRED("Expired"),
		SENT("Sent"),
		DELIVERED("Delivered"),
		;
		
		private final String description;
		
	}
	
}