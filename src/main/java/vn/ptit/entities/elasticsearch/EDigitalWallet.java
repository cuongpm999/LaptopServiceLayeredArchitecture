package vn.ptit.entities.elasticsearch;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "digitalwallets")
public class EDigitalWallet extends EPayment{
    @Field(name = "paymentId", type = FieldType.Text)
    private String paymentId;
    @Field(name = "name", type = FieldType.Text)
    private String name;
}
