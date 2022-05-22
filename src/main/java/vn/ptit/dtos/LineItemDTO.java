package vn.ptit.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.ptit.entities.mysql.Cart;
import vn.ptit.entities.mysql.Laptop;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LineItemDTO {
    private String id;
    private int quantity;
    private LaptopDTO laptop;
}
