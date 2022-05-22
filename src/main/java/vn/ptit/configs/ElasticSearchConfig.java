package vn.ptit.configs;

import lombok.Data;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "vn.ptit.repositories.elasticsearch")
@ComponentScan(basePackages = {"vn.ptit.entities.elasticsearch"})
@ConfigurationProperties(prefix = "elasticsearch")
@Data
public class ElasticSearchConfig {
    private String hostname;
    private int port;

    @Bean
    @Profile("dev")
    public RestHighLevelClient client() {
        RestClientBuilder builder = RestClient.builder(
                new HttpHost(hostname, port));
        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
    }

    @Bean
    @Profile("dev")
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(client());
    }

}
