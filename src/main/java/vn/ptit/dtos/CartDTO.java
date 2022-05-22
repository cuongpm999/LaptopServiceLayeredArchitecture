package vn.ptit.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.ptit.entities.mysql.LineItem;
import vn.ptit.entities.mysql.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private String id;
    private Date createdAt;
    private UserDTO user;
    private double totalAmount;
    private List<LineItemDTO> lineItems;
}
