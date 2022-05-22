package vn.ptit.repositories.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import vn.ptit.entities.elasticsearch.EShipment;

@Repository
public interface EShipmentRepository extends ElasticsearchRepository<EShipment, String> {
}
