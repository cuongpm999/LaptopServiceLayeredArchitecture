package vn.ptit.entities.elasticsearch;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@Document(indexName = "laptops")
public class ELaptop {
    @Id
    private String id;
    @Field(name = "name", type = FieldType.Text)
    private String name;
    @Field(name = "cpu", type = FieldType.Text)
    private String cpu;
    @Field(name = "hardDrive", type = FieldType.Text)
    private String hardDrive;
    @Field(name = "ram", type = FieldType.Text)
    private String ram;
    @Field(name = "vga", type = FieldType.Text)
    private String vga;
    @Field(name = "price", type = FieldType.Double)
    private double price;
    @Field(name = "discount", type = FieldType.Double)
    private double discount;
    @Field(name = "screen", type = FieldType.Double)
    private double screen;
    @Field(name = "video", type = FieldType.Text)
    private String video;
    @Field(name="image", type = FieldType.Text)
    private String image;
    @Field(name="manufacturerId", type = FieldType.Text)
    private String manufacturerId;
    @Field(name="createdAt", type = FieldType.Date)
    private Date createdAt;
    @Field(name="status", type = FieldType.Boolean)
    private boolean status;

}
