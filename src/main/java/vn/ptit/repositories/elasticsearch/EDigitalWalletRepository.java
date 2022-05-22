package vn.ptit.repositories.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import vn.ptit.entities.elasticsearch.EDigitalWallet;

@Repository
public interface EDigitalWalletRepository extends ElasticsearchRepository<EDigitalWallet, String> {
}
