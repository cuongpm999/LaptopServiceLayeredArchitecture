package vn.ptit.entities.mysql;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "createdAt", nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date createdAt;

    @Column(name = "status", nullable = false, length = 255)
    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shipmentId", nullable = false)
    private Shipment shipment;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cartId", nullable = false)
    private Cart cart;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paymentId", nullable = false)
    private Payment payment;

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
        this.status = "Đã giao hàng";
    }

}
