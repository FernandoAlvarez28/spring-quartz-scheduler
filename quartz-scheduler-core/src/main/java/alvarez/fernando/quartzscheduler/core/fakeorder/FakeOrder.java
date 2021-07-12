package alvarez.fernando.quartzscheduler.core.fakeorder;

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
	
	@Column(name = "PROCESSED", nullable = false)
	private boolean processed = false;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PROCESSED_DATE")
	private Date processedDate;
	
}