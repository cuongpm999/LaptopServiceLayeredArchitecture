package vn.ptit.entities.elasticsearch;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "lineitems")
public class ELineItem {
    @Id
    private String id;
    @Field(name="cartId", type = FieldType.Text)
    private String cartId;
    @Field(name="laptopId", type = FieldType.Text)
    private String laptopId;
    @Field(name = "quantity", type = FieldType.Integer)
    private int quantity;
}
