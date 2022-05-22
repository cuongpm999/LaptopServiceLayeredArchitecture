package vn.ptit.repositories.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ptit.entities.mysql.Cart;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Integer> {
}
