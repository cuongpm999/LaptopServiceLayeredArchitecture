package vn.ptit.repositories.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ptit.entities.mysql.Cart;
import vn.ptit.entities.mysql.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {
}
