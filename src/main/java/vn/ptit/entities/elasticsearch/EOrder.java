package vn.ptit.entities.elasticsearch;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@Document(indexName = "orders")
public class EOrder {
    @Id
    private String id;
    @Field(name="createdAt", type = FieldType.Date)
    private Date createdAt;
    @Field(name="status", type = FieldType.Text)
    private String status;
    @Field(name="cartId", type = FieldType.Text)
    private String cartId;
    @Field(name="paymentId", type = FieldType.Text)
    private String paymentId;
    @Field(name="shipmentId", type = FieldType.Text)
    private String shipmentId;
    @Field(name="userId", type = FieldType.Text)
    private String userId;
}
