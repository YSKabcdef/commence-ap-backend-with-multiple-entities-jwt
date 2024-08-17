package portfolio.ecommencesite.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Email
    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)

    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDate localDate;

    @OneToOne
    @JoinColumn(name="paymentId")
    private Payment payment;

    private double totalAmount;
    private String orderStatus;

    @ManyToOne
    @JoinColumn(name = "addressId")
    private Address address;
}
