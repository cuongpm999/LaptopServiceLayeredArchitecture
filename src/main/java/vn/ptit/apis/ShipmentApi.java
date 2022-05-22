package vn.ptit.apis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.ptit.dtos.ShipmentDTO;
import vn.ptit.services.ShipmentService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/shipment")
@Slf4j
public class ShipmentApi {
    @Autowired
    private ShipmentService shipmentService;

    @PostMapping(path = "/insert", produces = "application/json")
    public ResponseEntity<ShipmentDTO> insert(@RequestBody ShipmentDTO shipmentDTO) {
        ShipmentDTO res = shipmentService.save(shipmentDTO);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping(path = "/update", produces = "application/json")
    public ResponseEntity<ShipmentDTO> update(@RequestBody ShipmentDTO shipmentDTO) {
        ShipmentDTO res = shipmentService.save(shipmentDTO);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(path = "/find-all", produces = "application/json")
    public ResponseEntity<List<ShipmentDTO>> findAll() {
        List<ShipmentDTO> res = shipmentService.findAll();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(path = "/find/{id}", produces = "application/json")
    public ResponseEntity<ShipmentDTO> findById(@PathVariable("id") int id) {
        ShipmentDTO res = shipmentService.findById(id);
        if (res != null) {
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(path = "/delete/{id}", produces = "application/json")
    public ResponseEntity<String> delete(@PathVariable("id") int id) {
        shipmentService.delete(id);
        return new ResponseEntity<>(String.valueOf(id), HttpStatus.OK);
    }

}
