package vn.ptit.repositories.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ptit.entities.mysql.Cash;
import vn.ptit.entities.mysql.DigitalWallet;

@Repository
public interface DigitalWalletRepository extends JpaRepository<DigitalWallet,Integer> {
}
