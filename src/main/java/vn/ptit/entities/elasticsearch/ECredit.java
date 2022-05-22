package vn.ptit.entities.elasticsearch;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "credits")
public class ECredit extends EPayment{
    @Field(name = "paymentId", type = FieldType.Text)
    private String paymentId;
    @Field(name = "number", type = FieldType.Text)
    private String number;
    @Field(name = "type", type = FieldType.Text)
    private String type;
    @Field(name = "date", type = FieldType.Text)
    private String date;
}
