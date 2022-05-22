package vn.ptit.entities.mysql;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "shipments")
@Data
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    @Column(name = "address", nullable = false, length = 255)
    private String address;
    @Column(name = "price",nullable = false)
    private double price;
    @Column(name = "status", nullable = true)
    private boolean status;

    @PrePersist
    void createdAt() {
        this.status = true;
    }
}
