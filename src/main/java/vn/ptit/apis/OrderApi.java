package vn.ptit.apis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.ptit.dtos.OrderDTO;
import vn.ptit.services.OrderService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/order")
@Slf4j
public class OrderApi {
    @Autowired
    private OrderService orderService;

    @PostMapping("/insert")
    public ResponseEntity<OrderDTO> insert(@RequestBody OrderDTO orderDTO, @RequestParam("token") String token) {
        OrderDTO res = orderService.insert(orderDTO, token);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/find-by-user/{username}")
    public ResponseEntity<List<OrderDTO>> findByUser(@PathVariable("username") String username) {
        List<OrderDTO> res = orderService.findOrderByCustomer(username);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<OrderDTO>> findAll() {
        List<OrderDTO> res = orderService.findAll();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<OrderDTO> findById(@PathVariable("id") String id) {
        OrderDTO res = orderService.findById(id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
