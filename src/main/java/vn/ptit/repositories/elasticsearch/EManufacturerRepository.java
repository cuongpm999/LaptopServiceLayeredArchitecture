package vn.ptit.repositories.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import vn.ptit.entities.elasticsearch.EManufacturer;

@Repository
public interface EManufacturerRepository extends ElasticsearchRepository<EManufacturer, String> {
}
