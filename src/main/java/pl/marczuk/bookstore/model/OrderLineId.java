package pl.marczuk.bookstore.model;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.util.List;

@Data
@Embeddable
public class OrderLineId implements Serializable {
    private String bookIsbn;
    private Long orderId;

}
