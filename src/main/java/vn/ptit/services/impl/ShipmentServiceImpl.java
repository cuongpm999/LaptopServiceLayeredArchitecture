package vn.ptit.services.impl;

import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ptit.dtos.ShipmentDTO;
import vn.ptit.entities.elasticsearch.EShipment;
import vn.ptit.entities.mysql.Shipment;
import vn.ptit.repositories.elasticsearch.EShipmentRepository;
import vn.ptit.repositories.mysql.ShipmentRepository;
import vn.ptit.services.ShipmentService;

import java.io.IOException;
import java.util.*;

@Service
public class ShipmentServiceImpl implements ShipmentService {
    @Autowired
    private ShipmentRepository shipmentRepository;
    @Autowired
    private EShipmentRepository eShipmentRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ElasticsearchOperations operations;
    @Autowired
    private RestHighLevelClient client;

    @Override
    @Transactional
    public ShipmentDTO save(ShipmentDTO shipmentDTO) {
        Shipment shipment = modelMapper.map(shipmentDTO, Shipment.class);
        shipment = shipmentRepository.save(shipment);
        EShipment eShipment = modelMapper.map(shipment, EShipment.class);
        eShipmentRepository.save(eShipment);
        return modelMapper.map(shipment, ShipmentDTO.class);
    }

    @Override
    @Transactional
    public void delete(int id) {
        Shipment shipment = shipmentRepository.findById(id).get();
        shipment.setStatus(false);
        shipmentRepository.save(shipment);

        UpdateRequest request = new UpdateRequest("shipments", String.valueOf(id));
        Map<String, Object> map = new HashMap<>();
        map.put("status", false);
        request.doc(map);
        try {
            client.update(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ShipmentDTO> findAll() {
        List<Shipment> shipments = shipmentRepository.findByStatusTrue();
        List<ShipmentDTO> shipmentDTOS = new ArrayList<>();
        shipments.forEach(s -> {
            ShipmentDTO shipmentDTO = modelMapper.map(s, ShipmentDTO.class);
            shipmentDTOS.add(shipmentDTO);
        });
        return shipmentDTOS;
    }

    @Override
    public ShipmentDTO findById(int id) {
        Optional<Shipment> shipment = shipmentRepository.findById(id);
        if(shipment.isPresent()){
            return modelMapper.map(shipment.get(),ShipmentDTO.class);
        }
        return null;
    }
}
