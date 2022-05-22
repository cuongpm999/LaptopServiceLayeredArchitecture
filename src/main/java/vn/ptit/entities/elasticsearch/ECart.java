package vn.ptit.entities.elasticsearch;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@Document(indexName = "carts")
public class ECart {
    @Id
    private String id;
    @Field(name="userId", type = FieldType.Text)
    private String userId;
    @Field(name="createdAt", type = FieldType.Date)
    private Date createdAt;
    @Field(name = "totalAmount", type = FieldType.Double)
    private double totalAmount;
}
