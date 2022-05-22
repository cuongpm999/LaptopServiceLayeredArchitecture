package vn.ptit.services.impl;

import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ptit.dtos.UserDTO;
import vn.ptit.entities.elasticsearch.EUser;
import vn.ptit.entities.mysql.Manufacturer;
import vn.ptit.entities.mysql.User;
import vn.ptit.repositories.elasticsearch.EUserRepository;
import vn.ptit.repositories.mysql.UserRepository;
import vn.ptit.services.UserService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EUserRepository eUserRepository;
    @Autowired
    private RestHighLevelClient client;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public UserDTO save(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        user = userRepository.save(user);
        EUser eUser = modelMapper.map(user, EUser.class);
        eUserRepository.save(eUser);
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAllWithStatusTrue();
        List<UserDTO> userDTOS = new ArrayList<>();
        users.forEach(u -> userDTOS.add(modelMapper.map(u, UserDTO.class)));
        return userDTOS;
    }

    @Override
    public UserDTO findById(int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return modelMapper.map(user.get(), UserDTO.class);
        }
        return null;
    }

    @Override
    @Transactional
    public void delete(int id) {
        User user = userRepository.findById(id).get();
        user.setStatus(false);
        userRepository.save(user);

        UpdateRequest request = new UpdateRequest("users", String.valueOf(id));
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
    public UserDTO checkLogin(String username, String password) {
        List<User> users = userRepository.findByUsernameAndPasswordAndStatusTrue(username, password);
        if (users.isEmpty()) return null;
        User user = users.get(0);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return userDTO;
    }

    @Override
    public UserDTO findByUsername(String username) {
        List<User> users = userRepository.findByUsernameAndStatusTrue(username);
        if (users.isEmpty()) return null;
        User user = users.get(0);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return userDTO;
    }

    @Override
    public UserDTO findByEmail(String email) {
        List<User> users = userRepository.findByEmailAndStatusTrue(email);
        if (users.isEmpty()) return null;
        User user = users.get(0);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return userDTO;
    }

    @Override
    @javax.transaction.Transactional
    public int changePassword(String password, Integer userId) {
        String jpql = "update User u set u.password = :password where u.id = :id";
        Query query = entityManager.createQuery(jpql).setParameter("password", password).setParameter("id", userId);
        int res = query.executeUpdate();

        if (res == 1) {
            Map<String,Object> map = new HashMap<>();
            map.put("password",password);
            UpdateRequest request = new UpdateRequest("users", String.valueOf(userId))
                    .doc(map);
            try {
                client.update(request,RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return res;
    }
}
