package vn.ptit.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private String id;
    private Date createdAt;
    private String status;
    private UserDTO user;
    private ShipmentDTO shipment;
    private CartDTO cart;
    private PaymentDTO payment;
}
