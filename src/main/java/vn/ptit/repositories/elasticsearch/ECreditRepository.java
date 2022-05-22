package vn.ptit.repositories.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import vn.ptit.entities.elasticsearch.ECredit;

@Repository
public interface ECreditRepository extends ElasticsearchRepository<ECredit, String> {
}
