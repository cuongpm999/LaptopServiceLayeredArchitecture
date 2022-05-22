package vn.ptit.services;

import org.springframework.stereotype.Service;
import vn.ptit.dtos.CommentDTO;
import vn.ptit.dtos.LaptopDTO;

import java.util.List;

public interface LaptopService {
    public LaptopDTO save(LaptopDTO laptopDTO);

    public List<LaptopDTO> findAll();

    public LaptopDTO findById(int id);

    public void delete(int id);

    public List<LaptopDTO> findByName(String name);

    public List<LaptopDTO> findAllWithManufacturer(String idManufacturer,Integer limit);

    public List<LaptopDTO> findSameItem(String idManufacturer, String idLaptop);

    public List<LaptopDTO> filter(String tabName, String manufacturerId);

    public List<LaptopDTO> filter(String tabName, Integer manufacturerId, String cpu, String ram,
                                  String hardDrive, String vga);

    public List<LaptopDTO> filterWithName(String tabName, String name);

    public LaptopDTO insertComment(CommentDTO commentDTO);
}
