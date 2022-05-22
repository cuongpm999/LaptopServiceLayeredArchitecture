package vn.ptit.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LaptopDTO {
    private String id;
    private String name;
    private String cpu;
    private String hardDrive;
    private String ram;
    private String vga;
    private double price;
    private double discount;
    private double screen;
    private String video;
    private String image;
    private ManufacturerDTO manufacturer;
    private Date createdAt;
    private boolean status;
    private double averageStar;
    private List<CommentDTO> comments;
}
