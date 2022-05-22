package vn.ptit.repositories.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import vn.ptit.entities.elasticsearch.ECash;

@Repository
public interface ECashRepository extends ElasticsearchRepository<ECash,String> {
}
