package vn.ptit.repositories.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.ptit.entities.mysql.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    @Query("SELECT u FROM User u WHERE u.status = true")
    List<User> findAllWithStatusTrue();

    List<User> findByUsernameAndPasswordAndStatusTrue(String username, String password);

    List<User> findByUsernameAndStatusTrue(String username);

    List<User> findByEmailAndStatusTrue(String email);
}
