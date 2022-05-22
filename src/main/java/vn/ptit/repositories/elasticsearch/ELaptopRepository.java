package vn.ptit.repositories.elasticsearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import vn.ptit.entities.elasticsearch.ELaptop;

import java.util.List;

@Repository
public interface ELaptopRepository extends ElasticsearchRepository<ELaptop, String> {
    Page<ELaptop> findByManufacturerIdAndStatusTrueOrderByCreatedAtDesc(String id, Pageable pageable);

    List<ELaptop> findByManufacturerIdAndStatusTrueAndIdNot(String idManufacturer, String idLaptop);
}
