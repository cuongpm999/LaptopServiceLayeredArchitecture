package vn.ptit.dtos;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeName("credit")
public class CreditDTO extends PaymentDTO{
    private String number;
    private String type;
    private String date;
}
