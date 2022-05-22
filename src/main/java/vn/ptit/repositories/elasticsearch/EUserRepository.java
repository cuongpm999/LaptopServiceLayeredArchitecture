package vn.ptit.repositories.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import vn.ptit.entities.elasticsearch.EUser;

import java.util.List;

@Repository
public interface EUserRepository extends ElasticsearchRepository<EUser, String> {
    List<EUser> findByUsername(String username);
}
