package vn.ptit.entities.mysql;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="cashs")
@PrimaryKeyJoinColumn(name="paymentId")
@Data
public class Cash extends Payment{
    @Column(name = "cashTendered", nullable = false)
    private double cashTendered;

}
