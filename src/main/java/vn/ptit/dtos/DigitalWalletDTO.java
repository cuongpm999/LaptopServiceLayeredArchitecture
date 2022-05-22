package vn.ptit.dtos;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeName("digitalWallet")
public class DigitalWalletDTO extends PaymentDTO{
    private String name;
}
