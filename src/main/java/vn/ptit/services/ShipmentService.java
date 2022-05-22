package vn.ptit.services;

import vn.ptit.dtos.ShipmentDTO;

import java.util.List;

public interface ShipmentService {
    public ShipmentDTO save(ShipmentDTO shipmentDTO);
    public void delete (int id);
    public List<ShipmentDTO> findAll();
    public ShipmentDTO findById(int id);
}
