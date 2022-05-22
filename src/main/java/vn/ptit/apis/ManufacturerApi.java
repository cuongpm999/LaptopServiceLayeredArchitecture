package vn.ptit.apis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.ptit.dtos.ManufacturerDTO;
import vn.ptit.services.ManufacturerService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/manufacturer")
public class ManufacturerApi {
    @Autowired
    ManufacturerService manufacturerService;

    @PostMapping(path = "/insert", produces = "application/json")
    public ResponseEntity<ManufacturerDTO> insert(@RequestBody ManufacturerDTO manufacturerDTO) {
        ManufacturerDTO res = manufacturerService.save(manufacturerDTO);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(path = "/find-all", produces = "application/json")
    public ResponseEntity<List<ManufacturerDTO>> findAll() {
        List<ManufacturerDTO> res = manufacturerService.findAll();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(path = "/find/{id}", produces = "application/json")
    public ResponseEntity<ManufacturerDTO> findById(@PathVariable("id") int id) {
        ManufacturerDTO res = manufacturerService.findById(id);
        if (res != null) {
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(path = "/delete/{id}", produces = "application/json")
    public ResponseEntity<String> delete(@PathVariable("id") int id) {
        manufacturerService.delete(id);
        return new ResponseEntity<>(String.valueOf(id), HttpStatus.OK);
    }

    @PutMapping(path = "/update", produces = "application/json")
    public ResponseEntity<ManufacturerDTO> update(@RequestBody ManufacturerDTO manufacturerDTO) {
        ManufacturerDTO res = manufacturerService.save(manufacturerDTO);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
