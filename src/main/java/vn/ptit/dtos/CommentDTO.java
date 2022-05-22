package vn.ptit.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private String id;
    private Date createdAt;
    private int star;
    private UserDTO user;
    private int laptopId;
    private String content;
}
