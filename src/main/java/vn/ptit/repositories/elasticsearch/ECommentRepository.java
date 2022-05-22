package vn.ptit.repositories.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import vn.ptit.entities.elasticsearch.EComment;

@Repository
public interface ECommentRepository extends ElasticsearchRepository<EComment,String> {
}
