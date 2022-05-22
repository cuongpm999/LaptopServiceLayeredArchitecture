package vn.ptit.services;

import org.springframework.stereotype.Service;
import vn.ptit.dtos.ManufacturerDTO;

import java.util.List;

public interface ManufacturerService {
    public ManufacturerDTO save(ManufacturerDTO manufacturerDTO);
    public void delete (int id);
    public List<ManufacturerDTO> findAll();
    public ManufacturerDTO findById(int id);
}
