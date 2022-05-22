package vn.ptit.apis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.ptit.dtos.CommentDTO;
import vn.ptit.dtos.LaptopDTO;
import vn.ptit.services.LaptopService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/laptop")
@Slf4j
public class LaptopApi {
    @Autowired
    LaptopService laptopService;

    @PostMapping(path = "/insert", produces = "application/json")
    public ResponseEntity<LaptopDTO> insert(@RequestBody LaptopDTO laptopDTO) {
        LaptopDTO res = laptopService.save(laptopDTO);
        System.out.println(laptopDTO.toString());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(path = "/find-all", produces = "application/json")
    public ResponseEntity<List<LaptopDTO>> findAll() {
        List<LaptopDTO> res = laptopService.findAll();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(path = "/find/{id}", produces = "application/json")
    public ResponseEntity<LaptopDTO> findById(@PathVariable("id") int id) {
        LaptopDTO res = laptopService.findById(id);
        if (res != null) {
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(path = "/delete/{id}", produces = "application/json")
    public ResponseEntity<String> delete(@PathVariable("id") int id) {
        laptopService.delete(id);
        return new ResponseEntity<>(String.valueOf(id), HttpStatus.OK);
    }

    @PutMapping(path = "/update", produces = "application/json")
    public ResponseEntity<LaptopDTO> update(@RequestBody LaptopDTO laptopDTO) {
        LaptopDTO res = laptopService.save(laptopDTO);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(path = "/find-by-name/{name}", produces = "application/json")
    public ResponseEntity<List<LaptopDTO>> findByName(@PathVariable("name") String name) {
        List<LaptopDTO> res = laptopService.findByName(name);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(path = "/find-by-manufacturer/{idManufacturer}", produces = "application/json")
    public ResponseEntity<List<LaptopDTO>> findByManufacturer(@PathVariable("idManufacturer") String idManufacturer, @RequestParam(value = "limit",required = false) Integer limit) {
        List<LaptopDTO> res = laptopService.findAllWithManufacturer(idManufacturer,limit);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(path = "/find-same-item", produces = "application/json")
    public ResponseEntity<List<LaptopDTO>> findSameItem(@RequestParam("idManufacturer") String idManufacturer, @RequestParam("idLaptop") String idLaptop) {
        List<LaptopDTO> res = laptopService.findSameItem(idManufacturer, idLaptop);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(path = "/filter_", produces = "application/json")
    public ResponseEntity<List<LaptopDTO>> filter(@RequestParam(value = "tabName", required = false) String tabName, @RequestParam("manufacturerId") String manufacturerId) {
        List<LaptopDTO> res = laptopService.filter(tabName, manufacturerId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(path = "/filter-with-name", produces = "application/json")
    public ResponseEntity<List<LaptopDTO>> filterWithName(@RequestParam(value = "tabName", required = false) String tabName, @RequestParam("name") String name) {
        List<LaptopDTO> res = laptopService.filterWithName(tabName, name);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(path = "/filter", produces = "application/json")
    public ResponseEntity<List<LaptopDTO>> filter(@RequestParam(value = "tabName", required = false) String tabName,
                                                  @RequestParam(value = "manufacturerId", required = false) Integer manufacturerId,
                                                  @RequestParam(value = "cpu", required = false) String cpu,
                                                  @RequestParam(value = "ram", required = false) String ram,
                                                  @RequestParam(value = "hardDrive", required = false) String hardDrive,
                                                  @RequestParam(value = "vga", required = false) String vga) {
        List<LaptopDTO> res = laptopService.filter(tabName, manufacturerId, cpu, ram, hardDrive, vga);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping(path = "/insert-comment", produces = "application/json")
    public ResponseEntity<LaptopDTO> insertComment(@RequestBody CommentDTO commentDTO) {
        LaptopDTO res = laptopService.insertComment(commentDTO);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}
