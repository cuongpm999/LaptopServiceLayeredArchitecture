package vn.ptit.repositories.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.ptit.entities.mysql.Cart;
import vn.ptit.entities.mysql.LineItem;

import java.util.List;

@Repository
public interface LineItemRepository extends JpaRepository<LineItem, Integer> {
    @Query("select l from LineItem l where l.cart.id = ?1 and l.laptop.id = ?2")
    List<LineItem> findWithCartIdAndLaptopId(int cartId, int laptopId);

    @Query("select l from LineItem l where l.cart.id = ?1")
    List<LineItem> findWithCartId(int cartId);
}
