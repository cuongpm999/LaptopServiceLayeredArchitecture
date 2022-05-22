package vn.ptit.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import vn.ptit.dtos.*;
import vn.ptit.entities.Message;
import vn.ptit.entities.elasticsearch.*;
import vn.ptit.entities.mysql.*;
import vn.ptit.repositories.elasticsearch.*;
import vn.ptit.repositories.mysql.CashRepository;
import vn.ptit.repositories.mysql.CreditRepository;
import vn.ptit.repositories.mysql.DigitalWalletRepository;
import vn.ptit.repositories.mysql.OrderRepository;
import vn.ptit.services.OrderService;
import vn.ptit.services.SendMailService;

import javax.mail.MessagingException;
import java.text.NumberFormat;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CreditRepository creditRepository;
    @Autowired
    private DigitalWalletRepository digitalWalletRepository;
    @Autowired
    private CashRepository cashRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ECreditRepository eCreditRepository;
    @Autowired
    private ECashRepository eCashRepository;
    @Autowired
    private EDigitalWalletRepository eDigitalWalletRepository;
    @Autowired
    private EPaymentRepository ePaymentRepository;
    @Autowired
    private EOrderRepository eOrderRepository;
    @Autowired
    private EUserRepository eUserRepository;
    @Autowired
    private EShipmentRepository eShipmentRepository;
    @Autowired
    private ECartRepository eCartRepository;
    @Autowired
    private ELaptopRepository eLaptopRepository;
    @Autowired
    private EManufacturerRepository eManufacturerRepository;
    @Autowired
    private ELineItemRepository eLineItemRepository;
    private RestTemplate rest = new RestTemplate();
    @Autowired private SendMailService sendMailService;


    @Override
    @Transactional
    public OrderDTO insert(OrderDTO orderDTO, String token) {
        User user = modelMapper.map(orderDTO.getUser(), User.class);
        Credit credit = null;
        Cash cash = null;
        DigitalWallet digitalWallet = null;

        Order order = new Order();

        if (orderDTO.getPayment() instanceof CreditDTO) {
            credit = modelMapper.map(orderDTO.getPayment(), Credit.class);
            credit = creditRepository.save(credit);
            order.setPayment(credit);

            ECredit eCredit = modelMapper.map(credit, ECredit.class);
            eCredit.setPaymentId(String.valueOf(credit.getId()));
            eCreditRepository.save(eCredit);
            EPayment ePayment = modelMapper.map(credit, EPayment.class);
            ePaymentRepository.save(ePayment);

        }
        if (orderDTO.getPayment() instanceof CashDTO) {
            cash = modelMapper.map(orderDTO.getPayment(), Cash.class);
            cash = cashRepository.save(cash);
            order.setPayment(cash);

            ECash eCash = modelMapper.map(cash, ECash.class);
            eCash.setPaymentId(String.valueOf(cash.getId()));
            eCashRepository.save(eCash);
            EPayment ePayment = modelMapper.map(cash, EPayment.class);
            ePaymentRepository.save(ePayment);
        }
        if (orderDTO.getPayment() instanceof DigitalWalletDTO) {
            digitalWallet = modelMapper.map(orderDTO.getPayment(), DigitalWallet.class);
            digitalWallet = digitalWalletRepository.save(digitalWallet);
            order.setPayment(digitalWallet);

            EDigitalWallet eDigitalWallet = modelMapper.map(digitalWallet, EDigitalWallet.class);
            eDigitalWallet.setPaymentId(String.valueOf(digitalWallet.getId()));
            eDigitalWalletRepository.save(eDigitalWallet);
            EPayment ePayment = modelMapper.map(digitalWallet, EPayment.class);
            ePaymentRepository.save(ePayment);
        }

        Shipment shipment = modelMapper.map(orderDTO.getShipment(), Shipment.class);
        Cart cart = modelMapper.map(orderDTO.getCart(), Cart.class);

        order.setCart(cart);
        order.setShipment(shipment);
        order.setUser(user);

        order = orderRepository.save(order);

        EOrder eOrder = modelMapper.map(order, EOrder.class);
        eOrder.setCartId(String.valueOf(order.getCart().getId()));
        eOrder.setPaymentId(String.valueOf(order.getPayment().getId()));
        eOrder.setUserId(String.valueOf(order.getUser().getId()));
        eOrder.setShipmentId(String.valueOf(order.getShipment().getId()));

        eOrderRepository.save(eOrder);

        orderDTO = modelMapper.map(order, OrderDTO.class);
        orderDTO.setCart(modelMapper.map(order.getCart(), CartDTO.class));
        orderDTO.setPayment(modelMapper.map(order.getPayment(), PaymentDTO.class));
        orderDTO.setShipment(modelMapper.map(order.getShipment(), ShipmentDTO.class));
        orderDTO.setUser(modelMapper.map(order.getUser(), UserDTO.class));

        Locale local = new Locale("vi", "VN");
        NumberFormat numberFormat = NumberFormat.getInstance(local);
        Message message = new Message(token,"Bạn đã đặt hàng thành công","Tổng tiền: "+numberFormat.format(order.getPayment().getTotalMoney())+" đ");

        rest.postForObject("http://localhost:9696/notification/token",message,String.class);

        try {
            sendMailService.sendMail(order);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return orderDTO;
    }

    @Override
    public List<OrderDTO> findOrderByCustomer(String username) {
        EUser eUser = eUserRepository.findByUsername(username).get(0);
        List<OrderDTO> orderDTOS = new ArrayList<>();
        List<EOrder> eOrders = eOrderRepository.findByUserIdOrderByCreatedAtDesc(eUser.getId());
        eOrders.forEach(o -> {
            OrderDTO orderDTO = modelMapper.map(o, OrderDTO.class);
            orderDTO.setUser(null);
            orderDTO.setShipment(modelMapper.map(eShipmentRepository.findById(o.getShipmentId()).get(), ShipmentDTO.class));
            Optional<ECredit> eCredit = eCreditRepository.findById(o.getPaymentId());
            Optional<ECash> eCash = eCashRepository.findById(o.getPaymentId());
            Optional<EDigitalWallet> eDigitalWallet = eDigitalWalletRepository.findById(o.getPaymentId());

            if (eCash.isPresent()) {
                orderDTO.setPayment(modelMapper.map(eCash.get(), CashDTO.class));
            }
            if (eCredit.isPresent()) {
                orderDTO.setPayment(modelMapper.map(eCredit.get(), CreditDTO.class));
            }
            if (eDigitalWallet.isPresent()) {
                orderDTO.setPayment(modelMapper.map(eDigitalWallet.get(), DigitalWalletDTO.class));
            }

            ECart eCart = eCartRepository.findById(o.getCartId()).get();
            List<ELineItem> eLineItem = eLineItemRepository.findByCartId(eCart.getId());
            List<LineItemDTO> lineItemDTOS = new ArrayList<>();
            eLineItem.forEach(l -> {
                LineItemDTO lineItemDTO = modelMapper.map(l, LineItemDTO.class);
                ELaptop eLaptop = eLaptopRepository.findById(l.getLaptopId()).get();
                EManufacturer eManufacturer = eManufacturerRepository.findById(eLaptop.getManufacturerId()).get();
                LaptopDTO laptopDTO = modelMapper.map(eLaptop, LaptopDTO.class);
                laptopDTO.setManufacturer(modelMapper.map(eManufacturer, ManufacturerDTO.class));
                lineItemDTO.setLaptop(laptopDTO);
                lineItemDTOS.add(lineItemDTO);
            });

            CartDTO cartDTO = modelMapper.map(eCart, CartDTO.class);
            cartDTO.setLineItems(lineItemDTOS);
            cartDTO.setUser(null);
            orderDTO.setUser(modelMapper.map(eUser, UserDTO.class));
            orderDTO.setCart(cartDTO);

            orderDTOS.add(orderDTO);
        });

        return orderDTOS;
    }

    @Override
    public List<OrderDTO> findAll() {
        List<OrderDTO> orderDTOS = new ArrayList<>();
        Iterable<EOrder> eOrders = eOrderRepository.findAll();
        eOrders.forEach(o -> {
            OrderDTO orderDTO = modelMapper.map(o, OrderDTO.class);
            orderDTO.setUser(null);
            orderDTO.setShipment(modelMapper.map(eShipmentRepository.findById(o.getShipmentId()).get(), ShipmentDTO.class));
            Optional<ECredit> eCredit = eCreditRepository.findById(o.getPaymentId());
            Optional<ECash> eCash = eCashRepository.findById(o.getPaymentId());
            Optional<EDigitalWallet> eDigitalWallet = eDigitalWalletRepository.findById(o.getPaymentId());

            if (eCash.isPresent()) {
                orderDTO.setPayment(modelMapper.map(eCash.get(), CashDTO.class));
            }
            if (eCredit.isPresent()) {
                orderDTO.setPayment(modelMapper.map(eCredit.get(), CreditDTO.class));
            }
            if (eDigitalWallet.isPresent()) {
                orderDTO.setPayment(modelMapper.map(eDigitalWallet.get(), DigitalWalletDTO.class));
            }

            ECart eCart = eCartRepository.findById(o.getCartId()).get();
            List<ELineItem> eLineItem = eLineItemRepository.findByCartId(eCart.getId());
            List<LineItemDTO> lineItemDTOS = new ArrayList<>();
            eLineItem.forEach(l -> {
                LineItemDTO lineItemDTO = modelMapper.map(l, LineItemDTO.class);
                ELaptop eLaptop = eLaptopRepository.findById(l.getLaptopId()).get();
                EManufacturer eManufacturer = eManufacturerRepository.findById(eLaptop.getManufacturerId()).get();
                LaptopDTO laptopDTO = modelMapper.map(eLaptop, LaptopDTO.class);
                laptopDTO.setManufacturer(modelMapper.map(eManufacturer, ManufacturerDTO.class));
                lineItemDTO.setLaptop(laptopDTO);
                lineItemDTOS.add(lineItemDTO);
            });

            EUser eUser = eUserRepository.findById(o.getUserId()).get();

            CartDTO cartDTO = modelMapper.map(eCart, CartDTO.class);
            cartDTO.setLineItems(lineItemDTOS);
            cartDTO.setUser(null);
            orderDTO.setUser(modelMapper.map(eUser, UserDTO.class));
            orderDTO.setCart(cartDTO);

            orderDTOS.add(orderDTO);
        });

        Collections.sort(orderDTOS, new Comparator<OrderDTO>() {
            @Override
            public int compare(OrderDTO o1, OrderDTO o2) {
                return Long.compare(o2.getCreatedAt().getTime(), o1.getCreatedAt().getTime());
            }
        });

        return orderDTOS;
    }

    @Override
    public OrderDTO findById(String id) {
        EOrder o = eOrderRepository.findById(id).get();

        OrderDTO orderDTO = modelMapper.map(o, OrderDTO.class);
        orderDTO.setUser(null);
        orderDTO.setShipment(modelMapper.map(eShipmentRepository.findById(o.getShipmentId()).get(), ShipmentDTO.class));
        Optional<ECredit> eCredit = eCreditRepository.findById(o.getPaymentId());
        Optional<ECash> eCash = eCashRepository.findById(o.getPaymentId());
        Optional<EDigitalWallet> eDigitalWallet = eDigitalWalletRepository.findById(o.getPaymentId());

        if (eCash.isPresent()) {
            orderDTO.setPayment(modelMapper.map(eCash.get(), CashDTO.class));
        }
        if (eCredit.isPresent()) {
            orderDTO.setPayment(modelMapper.map(eCredit.get(), CreditDTO.class));
        }
        if (eDigitalWallet.isPresent()) {
            orderDTO.setPayment(modelMapper.map(eDigitalWallet.get(), DigitalWalletDTO.class));
        }

        ECart eCart = eCartRepository.findById(o.getCartId()).get();
        List<ELineItem> eLineItem = eLineItemRepository.findByCartId(eCart.getId());
        List<LineItemDTO> lineItemDTOS = new ArrayList<>();
        eLineItem.forEach(l -> {
            LineItemDTO lineItemDTO = modelMapper.map(l, LineItemDTO.class);
            ELaptop eLaptop = eLaptopRepository.findById(l.getLaptopId()).get();
            EManufacturer eManufacturer = eManufacturerRepository.findById(eLaptop.getManufacturerId()).get();
            LaptopDTO laptopDTO = modelMapper.map(eLaptop, LaptopDTO.class);
            laptopDTO.setManufacturer(modelMapper.map(eManufacturer, ManufacturerDTO.class));
            lineItemDTO.setLaptop(laptopDTO);
            lineItemDTOS.add(lineItemDTO);
        });

        EUser eUser = eUserRepository.findById(o.getUserId()).get();

        CartDTO cartDTO = modelMapper.map(eCart, CartDTO.class);
        cartDTO.setLineItems(lineItemDTOS);
        cartDTO.setUser(null);
        orderDTO.setUser(modelMapper.map(eUser, UserDTO.class));
        orderDTO.setCart(cartDTO);

        return orderDTO;
    }
}
