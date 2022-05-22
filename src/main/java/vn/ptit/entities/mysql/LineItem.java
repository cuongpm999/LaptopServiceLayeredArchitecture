package vn.ptit.entities.mysql;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "lineitems")
@Data
public class LineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cartId", nullable = false)
    private Cart cart;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "laptopId", nullable = false)
    private Laptop laptop;
}
