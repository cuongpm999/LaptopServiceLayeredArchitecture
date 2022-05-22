package vn.ptit.entities.elasticsearch;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "payments")
public class EPayment {
    @Id
    private String id;
    @Field(name = "totalMoney", type = FieldType.Double)
    private double totalMoney;
}
