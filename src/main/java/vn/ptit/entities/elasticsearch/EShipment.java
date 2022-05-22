package vn.ptit.entities.elasticsearch;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "shipments")
public class EShipment {
    @Id
    private String id;
    @Field(name = "name", type = FieldType.Text)
    private String name;
    @Field(name = "address", type = FieldType.Text)
    private String address;
    @Field(name = "price", type = FieldType.Double)
    private double price;
    @Field(name="status", type = FieldType.Boolean)
    private boolean status;
}
