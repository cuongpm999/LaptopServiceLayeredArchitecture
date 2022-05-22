package vn.ptit.apis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.ptit.dtos.CartDTO;
import vn.ptit.dtos.LaptopDTO;
import vn.ptit.entities.mysql.Cart;
import vn.ptit.services.CartService;
import vn.ptit.services.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/cart")
@Slf4j
public class CartApi {
    @Autowired
    CartService cartService;

    @GetMapping(path = "/find-current", produces = "application/json")
    public ResponseEntity<CartDTO> findCurrentCart(@RequestParam("username") String username) {
        CartDTO res = cartService.findCurrentCartByCustomer(username);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping(path = "/add", produces = "application/json")
    public ResponseEntity<String> addToCart(@RequestParam("username") String username,@RequestParam("laptopId") int laptopId) {
        cartService.addToCart(username,laptopId);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PutMapping(path = "/edit", produces = "application/json")
    public ResponseEntity<String> editCart(@RequestParam("username") String username,@RequestParam("laptopId") int laptopId, @RequestParam("quantity") int quantity) {
        cartService.editCart(username,laptopId,quantity);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete", produces = "application/json")
    public ResponseEntity<String> deleteCart(@RequestParam("username") String username,@RequestParam("laptopId") int laptopId) {
        cartService.deleteCart(username,laptopId);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
