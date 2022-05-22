package vn.ptit.repositories.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ptit.entities.mysql.Cart;
import vn.ptit.entities.mysql.Shipment;

import java.util.List;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment,Integer> {
    List<Shipment> findByStatusTrue();
}
