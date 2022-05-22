package vn.ptit.services.impl;

import com.google.gson.Gson;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.hibernate.SessionFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ptit.dtos.ManufacturerDTO;
import vn.ptit.entities.elasticsearch.EManufacturer;
import vn.ptit.entities.mysql.Manufacturer;
import vn.ptit.repositories.elasticsearch.EManufacturerRepository;
import vn.ptit.repositories.mysql.ManufacturerRepository;
import vn.ptit.services.ManufacturerService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.*;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EManufacturerRepository eManufacturerRepository;
    @Autowired
    private ElasticsearchOperations operations;
    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Override
    @Transactional
    public ManufacturerDTO save(ManufacturerDTO manufacturerDTO) {
        Manufacturer manufacturer = modelMapper.map(manufacturerDTO,Manufacturer.class);
        manufacturer = manufacturerRepository.save(manufacturer);
        EManufacturer eManufacturer = modelMapper.map(manufacturer,EManufacturer.class);
        eManufacturerRepository.save(eManufacturer);
        return modelMapper.map(manufacturer,ManufacturerDTO.class);
    }

    @Override
    @Transactional
    public void delete(int id) {
        Manufacturer manufacturer = manufacturerRepository.findById(id).get();
        manufacturer.setStatus(false);
        manufacturerRepository.save(manufacturer);

        UpdateRequest request = new UpdateRequest("manufacturers", String.valueOf(id));
        Map<String,Object> map = new HashMap<>();
        map.put("status",false);
        request.doc(map);
        try {
            client.update(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ManufacturerDTO> findAll() {
        List<Manufacturer> manufacturers = manufacturerRepository.findAllWithStatusTrue();

        List<ManufacturerDTO> manufacturerDTOS = new ArrayList<>();
        manufacturers.forEach(manufacturer -> {
            ManufacturerDTO manufacturerDTO = modelMapper.map(manufacturer, ManufacturerDTO.class);
            manufacturerDTOS.add(manufacturerDTO);
        });
        return manufacturerDTOS;
    }

    @Override
    public ManufacturerDTO findById(int id) {
        Optional<Manufacturer> manufacturer = manufacturerRepository.findById(id);
        if(manufacturer.isPresent()){
            return modelMapper.map(manufacturer.get(),ManufacturerDTO.class);
        }
        return null;
    }
}
