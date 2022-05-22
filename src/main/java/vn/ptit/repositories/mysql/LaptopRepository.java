package vn.ptit.repositories.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.ptit.entities.mysql.Laptop;

import java.util.List;

@Repository
public interface LaptopRepository extends JpaRepository<Laptop, Integer> {
    @Query("SELECT l FROM Laptop l WHERE l.status = true")
    List<Laptop> findAllWithStatusTrue();

    @Query("select avg(c.star) from Comment c where c.laptop.id = ?1")
    Double avgStar(int laptopId);
}
