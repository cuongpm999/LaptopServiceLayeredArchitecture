package vn.ptit.repositories.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import vn.ptit.entities.elasticsearch.EOrder;

import java.util.List;

@Repository
public interface EOrderRepository extends ElasticsearchRepository<EOrder,String> {
    List<EOrder> findByUserIdOrderByCreatedAtDesc(String userId);
}
