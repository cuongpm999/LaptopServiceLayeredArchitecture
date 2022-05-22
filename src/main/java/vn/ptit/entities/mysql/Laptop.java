package vn.ptit.entities.mysql;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "laptops")
@Data
public class Laptop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    @Column(name = "cpu", nullable = false, length = 255)
    private String cpu;
    @Column(name = "hardDrive", nullable = false, length = 255)
    private String hardDrive;
    @Column(name = "ram", nullable = false, length = 255)
    private String ram;
    @Column(name = "vga", nullable = false, length = 255)
    private String vga;
    @Column(name = "price",nullable = false)
    private double price;
    @Column(name = "discount",nullable = false)
    private double discount;
    @Column(name = "screen",nullable = false)
    private double screen;
    @Column(name = "video", nullable = false, length = 255)
    private String video;
    @Column(name = "createdAt", nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date createdAt;
    @Column(name = "status", nullable = true)
    private boolean status;
    @Column(name = "image", nullable = false, length = 1000)
    private String image;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manufacturerId", nullable = false)
    private Manufacturer manufacturer;

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
        this.status = true;
    }

}
