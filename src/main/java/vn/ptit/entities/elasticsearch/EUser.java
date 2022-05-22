package vn.ptit.entities.elasticsearch;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@Document(indexName = "users")
public class EUser {
    @Id
    private String id;
    @Field(name = "fullName", type = FieldType.Text)
    private String fullName;
    @Field(name = "address", type = FieldType.Text)
    private String address;
    @Field(name = "email", type = FieldType.Text)
    private String email;
    @Field(name = "mobile", type = FieldType.Text)
    private String mobile;
    @Field(name = "sex", type = FieldType.Boolean)
    private boolean sex;
    @Field(name="dateOfBirth", type = FieldType.Date)
    private Date dateOfBirth;
    @Field(name = "username", type = FieldType.Text)
    private String username;
    @Field(name = "password", type = FieldType.Text)
    private String password;
    @Field(name = "position", type = FieldType.Text)
    private String position;
    @Field(name="image", type = FieldType.Text)
    private String image;
    @Field(name="status", type = FieldType.Boolean)
    private boolean status;
}
