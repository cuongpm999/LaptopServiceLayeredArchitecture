package vn.ptit.entities.mysql;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "fullName", nullable = false, length = 255)
    private String fullName;
    @Column(name = "address", nullable = false, length = 255)
    private String address;
    @Column(name = "email", nullable = false, length = 255, unique = true)
    private String email;
    @Column(name = "mobile", nullable = false, length = 255)
    private String mobile;
    @Column(name = "sex", nullable = false)
    private boolean sex;
    @Column(name = "dateOfBirth", nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateOfBirth;
    @Column(name = "username", nullable = false, length = 255, unique = true)
    private String username;
    @Column(name = "password", nullable = false, length = 255)
    private String password;
    @Column(name = "position", nullable = false, length = 255)
    private String position;
    @Column(name = "image", nullable = false, length = 1000)
    private String image;
    @Column(name = "status", nullable = true)
    private boolean status;

    @PrePersist
    void created() {
        this.status = true;
    }
}
