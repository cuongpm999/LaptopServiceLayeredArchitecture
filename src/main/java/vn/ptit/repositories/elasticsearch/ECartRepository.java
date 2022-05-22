package vn.ptit.repositories.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import vn.ptit.entities.elasticsearch.ECart;

@Repository
public interface ECartRepository extends ElasticsearchRepository<ECart, String> {
}
