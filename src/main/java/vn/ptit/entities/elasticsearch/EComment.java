package vn.ptit.entities.elasticsearch;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@Document(indexName = "comments")
public class EComment {
    @Id
    private String id;
    @Field(name="createdAt", type = FieldType.Date)
    private Date createdAt;
    @Field(name = "star", type = FieldType.Integer)
    private int star;
    @Field(name="laptopId", type = FieldType.Text)
    private String laptopId;
    @Field(name="content", type = FieldType.Text)
    private String content;
    @Field(name="userId", type = FieldType.Text)
    private String userId;
}
