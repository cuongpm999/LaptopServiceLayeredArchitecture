package vn.ptit.services.impl;

import org.elasticsearch.client.RestHighLevelClient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ptit.dtos.CartDTO;
import vn.ptit.dtos.LaptopDTO;
import vn.ptit.dtos.LineItemDTO;
import vn.ptit.entities.elasticsearch.ECart;
import vn.ptit.entities.elasticsearch.ELineItem;
import vn.ptit.entities.mysql.Cart;
import vn.ptit.entities.mysql.Laptop;
import vn.ptit.entities.mysql.LineItem;
import vn.ptit.entities.mysql.User;
import vn.ptit.repositories.elasticsearch.ECartRepository;
import vn.ptit.repositories.elasticsearch.ELineItemRepository;
import vn.ptit.repositories.mysql.CartRepository;
import vn.ptit.repositories.mysql.LaptopRepository;
import vn.ptit.repositories.mysql.LineItemRepository;
import vn.ptit.repositories.mysql.UserRepository;
import vn.ptit.services.CartService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    LaptopRepository laptopRepository;
    @Autowired
    LineItemRepository lineItemRepository;
    @Autowired
    private ElasticsearchOperations operations;
    @Autowired
    private RestHighLevelClient client;
    @Autowired
    ECartRepository eCartRepository;
    @Autowired
    ELineItemRepository eLineItemRepository;

    @Override
    public CartDTO findCurrentCartByCustomer(String username) {
        User user = userRepository.findByUsernameAndStatusTrue(username).get(0);
        String jpql = "select c from Cart c where c.id not in (select o.cart.id from Order o where o.user.id = " + user.getId() + ") and c.user.id = " + user.getId();
        Query query = entityManager.createQuery(jpql, Cart.class);
        if (query.getResultList().size() == 0) return null;
        Cart cart = (Cart) query.getResultList().get(0);
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<LineItemDTO> lineItemDTOs = new ArrayList<>();
        cart.getLineItems().forEach(l -> {
            LineItemDTO lineItemDTO = modelMapper.map(l, LineItemDTO.class);
            LaptopDTO laptopDTO = modelMapper.map(l.getLaptop(), LaptopDTO.class);
            lineItemDTO.setLaptop(laptopDTO);
            lineItemDTOs.add(lineItemDTO);
        });
        cartDTO.setLineItems(lineItemDTOs);
        return cartDTO;
    }

    public Cart findCurrentCart(String username) {
        User user = userRepository.findByUsernameAndStatusTrue(username).get(0);
        String jpql = "select c from Cart c where c.id not in (select o.cart.id from Order o where o.user.id = " + user.getId() + ") and c.user.id = " + user.getId();
        Query query = entityManager.createQuery(jpql, Cart.class);
        if (query.getResultList().size() == 0) return null;
        return (Cart) query.getResultList().get(0);
    }

    @Override
    @Transactional
    public void addToCart(String username, int laptopId) {
        Cart cart = findCurrentCart(username);
        if (cart == null) {
            cart = new Cart();
            User user = userRepository.findByUsernameAndStatusTrue(username).get(0);
            cart.setUser(user);
            cart.setTotalAmount(0);
            cart = cartRepository.save(cart);
            ECart eCart = modelMapper.map(cart, ECart.class);
            eCart.setUserId(String.valueOf(cart.getUser().getId()));
            eCartRepository.save(eCart);
        }
        List<LineItem> lineItems = lineItemRepository.findWithCartIdAndLaptopId(cart.getId(), laptopId);
        if (lineItems.size() == 0) {
            LineItem lineItem = new LineItem();
            lineItem.setCart(cart);
            Laptop laptop = laptopRepository.findById(laptopId).get();
            lineItem.setLaptop(laptop);
            lineItem.setQuantity(1);
            lineItem = lineItemRepository.save(lineItem);
            ELineItem eLineItem = modelMapper.map(lineItem, ELineItem.class);
            eLineItem.setCartId(String.valueOf(cart.getId()));
            eLineItem.setLaptopId(String.valueOf(laptopId));
            eLineItemRepository.save(eLineItem);
        } else {
            LineItem lineItem = lineItems.get(0);
            lineItem.setQuantity(lineItem.getQuantity() + 1);
            lineItem = lineItemRepository.save(lineItem);
            ELineItem eLineItem = modelMapper.map(lineItem, ELineItem.class);
            eLineItem.setCartId(String.valueOf(cart.getId()));
            eLineItem.setLaptopId(String.valueOf(laptopId));
            eLineItemRepository.save(eLineItem);
        }

        Laptop laptop = laptopRepository.findById(laptopId).get();
        cart.setTotalAmount(cart.getTotalAmount() + laptop.getPrice() * (100 - laptop.getDiscount()) / 100);
        cart = cartRepository.save(cart);
        ECart eCart = modelMapper.map(cart, ECart.class);
        eCart.setUserId(String.valueOf(cart.getUser().getId()));
        eCartRepository.save(eCart);
    }

    @Override
    @Transactional
    public void editCart(String username, int laptopId, int quantity) {
        Cart cart = findCurrentCart(username);

        List<LineItem> lineItems = lineItemRepository.findWithCartIdAndLaptopId(cart.getId(), laptopId);
        LineItem lineItem = lineItems.get(0);
        lineItem.setQuantity(quantity);
        lineItem = lineItemRepository.save(lineItem);
        ELineItem eLineItem = modelMapper.map(lineItem, ELineItem.class);
        eLineItem.setCartId(String.valueOf(cart.getId()));
        eLineItem.setLaptopId(String.valueOf(laptopId));
        eLineItemRepository.save(eLineItem);

        lineItems = lineItemRepository.findWithCartId(cart.getId());
        Laptop laptop = laptopRepository.findById(laptopId).get();
        double totalAmount = lineItems.stream().mapToDouble(l -> l.getQuantity() * (l.getLaptop().getPrice() * (100 - l.getLaptop().getDiscount()) / 100)).sum();
        cart.setTotalAmount(totalAmount);
        cart = cartRepository.save(cart);
        ECart eCart = modelMapper.map(cart, ECart.class);
        eCart.setUserId(String.valueOf(cart.getUser().getId()));
        eCartRepository.save(eCart);
    }

    @Override
    @Transactional
    public void deleteCart(String username, int laptopId) {
        Cart cart = findCurrentCart(username);

        List<LineItem> lineItems = lineItemRepository.findWithCartIdAndLaptopId(cart.getId(), laptopId);
        LineItem lineItem = lineItems.get(0);
        lineItemRepository.deleteById(lineItem.getId());
        eLineItemRepository.deleteById(String.valueOf(lineItem.getId()));

        lineItems = lineItemRepository.findWithCartId(cart.getId());
        if(lineItems.size()==0){
            cartRepository.deleteById(cart.getId());
            eCartRepository.deleteById(String.valueOf(cart.getId()));
            return;
        }

        Laptop laptop = laptopRepository.findById(laptopId).get();
        double totalAmount = lineItems.stream().mapToDouble(l -> l.getQuantity() * (l.getLaptop().getPrice() * (100 - l.getLaptop().getDiscount()) / 100)).sum();
        cart.setTotalAmount(totalAmount);
        cart = cartRepository.save(cart);
        ECart eCart = modelMapper.map(cart, ECart.class);
        eCart.setUserId(String.valueOf(cart.getUser().getId()));
        eCartRepository.save(eCart);

    }
}
