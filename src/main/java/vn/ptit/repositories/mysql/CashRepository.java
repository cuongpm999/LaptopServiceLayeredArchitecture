package vn.ptit.repositories.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ptit.entities.mysql.Cash;
import vn.ptit.entities.mysql.Payment;

@Repository
public interface CashRepository extends JpaRepository<Cash,Integer> {
}
