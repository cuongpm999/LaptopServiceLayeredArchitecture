package vn.ptit.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManufacturerDTO {
    private String id;
    private String name;
    private String address;
    private Date createdAt;
    private boolean status;
}
