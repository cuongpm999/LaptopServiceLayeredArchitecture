package vn.ptit.services;

import vn.ptit.dtos.OrderDTO;

import java.util.List;

public interface OrderService {
    public OrderDTO insert(OrderDTO orderDTO,String token);
    public List<OrderDTO> findOrderByCustomer(String username);
    public List<OrderDTO> findAll();
    public OrderDTO findById(String id);
}
