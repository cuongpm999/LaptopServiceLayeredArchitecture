package vn.ptit.entities.mysql;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "payments")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "totalMoney",nullable = false)
    private double totalMoney;
}
