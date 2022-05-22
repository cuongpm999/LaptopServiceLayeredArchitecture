package vn.ptit.services;

import vn.ptit.dtos.UserDTO;

import java.util.List;

public interface UserService {
    public UserDTO save(UserDTO userDTO);

    public List<UserDTO> findAll();

    public UserDTO findById(int id);

    public void delete(int id);

    public UserDTO checkLogin(String username, String password);

    public UserDTO findByUsername(String username);

    public UserDTO findByEmail(String email);

    public int changePassword(String password, Integer userId);
}
