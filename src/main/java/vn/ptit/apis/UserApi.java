package vn.ptit.apis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.ptit.dtos.UserDTO;
import vn.ptit.services.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "api/user")
public class UserApi {
    @Autowired
    UserService userService;

    @PostMapping(path = "/insert", produces = "application/json")
    public ResponseEntity<UserDTO> insert(@RequestBody UserDTO userDTO) {
        UserDTO res = userService.save(userDTO);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(path = "/find-all", produces = "application/json")
    public ResponseEntity<List<UserDTO>> findAll() {
        List<UserDTO> res = userService.findAll();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(path = "/find/{id}", produces = "application/json")
    public ResponseEntity<UserDTO> findById(@PathVariable("id") int id) {
        UserDTO res = userService.findById(id);
        if (res != null)
            return new ResponseEntity<>(res, HttpStatus.OK);
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

    }

    @PutMapping(path = "/update", produces = "application/json")
    public ResponseEntity<UserDTO> update(@RequestBody UserDTO userDTO){
        UserDTO res = userService.save(userDTO);
        return new ResponseEntity<>(res,HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{id}", produces = "application/json")
    public ResponseEntity<String> delete(@PathVariable("id") int id){
        userService.delete(id);
        return new ResponseEntity<>(String.valueOf(id),HttpStatus.OK);
    }

    @GetMapping(path = "/check-login", produces = "application/json")
    public ResponseEntity<UserDTO> checkLogin(@RequestParam("username") String username,@RequestParam("password") String password) {
        UserDTO res = userService.checkLogin(username,password);
        if (res != null)
            return new ResponseEntity<>(res, HttpStatus.OK);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
    @GetMapping(path = "/find-by-username/{username}", produces = "application/json")
    public ResponseEntity<UserDTO> findByUsername(@PathVariable("username") String username) {
        UserDTO res = userService.findByUsername(username);
        if (res != null)
            return new ResponseEntity<>(res, HttpStatus.OK);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
    @GetMapping(path = "/find-by-email/{email}", produces = "application/json")
    public ResponseEntity<UserDTO> findByEmail(@PathVariable("email") String email) {
        UserDTO res = userService.findByEmail(email);
        if (res != null)
            return new ResponseEntity<>(res, HttpStatus.OK);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping(path = "/change-password", produces = "application/json")
    public ResponseEntity<Integer> changePassword(@RequestParam("password") String password,@RequestParam("userId") Integer userId){
        int res = userService.changePassword(password,userId);
        return new ResponseEntity<>(res,HttpStatus.OK);
    }
}
