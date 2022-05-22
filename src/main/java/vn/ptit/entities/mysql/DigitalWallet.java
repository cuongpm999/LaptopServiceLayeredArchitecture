package vn.ptit.entities.mysql;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="digitalwallets")
@PrimaryKeyJoinColumn(name="paymentId")
@Data
public class DigitalWallet extends Payment{
    @Column(name = "name", nullable = false, length = 255)
    private String name;
}
