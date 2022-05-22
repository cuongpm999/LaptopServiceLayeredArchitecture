package vn.ptit.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String id;
    private String fullName;
    private String address;
    private String email;
    private String mobile;
    private boolean sex;
    private Date dateOfBirth;
    private String username;
    private String password;
    private String position;
    private String image;
    private boolean status;
}
