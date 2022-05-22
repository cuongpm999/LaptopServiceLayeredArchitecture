package vn.ptit.entities.elasticsearch;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@Document(indexName = "manufacturers")
public class EManufacturer {
    @Id
    private String id;
    @Field(name = "name", type = FieldType.Text)
    private String name;
    @Field(name = "address", type = FieldType.Text)
    private String address;
    @Field(name="createdAt", type = FieldType.Date)
    private Date createdAt;
    @Field(name="status", type = FieldType.Boolean)
    private boolean status;
}
