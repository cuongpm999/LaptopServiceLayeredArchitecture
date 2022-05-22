package vn.ptit.dtos;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type_")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CashDTO.class, name = "cash"),
        @JsonSubTypes.Type(value = CreditDTO.class, name = "credit"),
        @JsonSubTypes.Type(value = DigitalWalletDTO.class, name = "digitalWallet"),
})
public class PaymentDTO {
    private String id;
    private double totalMoney;
}
