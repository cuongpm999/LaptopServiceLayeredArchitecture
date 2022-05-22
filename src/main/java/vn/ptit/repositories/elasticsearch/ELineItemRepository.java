package vn.ptit.repositories.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import vn.ptit.entities.elasticsearch.ELineItem;

import java.util.List;

@Repository
public interface ELineItemRepository extends ElasticsearchRepository<ELineItem, String> {
    List<ELineItem> findByCartId(String cartId);
}
