package vn.ptit.repositories.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ptit.entities.mysql.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Integer> {
}
