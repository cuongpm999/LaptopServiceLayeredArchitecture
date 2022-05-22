package vn.ptit.repositories.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import vn.ptit.entities.elasticsearch.EPayment;

@Repository
public interface EPaymentRepository extends ElasticsearchRepository<EPayment,String> {
}
