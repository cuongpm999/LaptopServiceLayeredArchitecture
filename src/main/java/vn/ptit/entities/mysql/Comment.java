package vn.ptit.entities.mysql;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comments")
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "star",nullable = false)
    private int star;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "laptopId", nullable = false)
    private Laptop laptop;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(name = "createdAt", nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date createdAt;

    @Column(name = "content", nullable = false, columnDefinition = "text")
    private String content;

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }

}
