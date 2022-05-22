package vn.ptit.services.impl;

import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.Sum;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import vn.ptit.entities.mysql.Order;
import vn.ptit.repositories.elasticsearch.ELaptopRepository;
import vn.ptit.repositories.elasticsearch.EOrderRepository;
import vn.ptit.repositories.elasticsearch.EUserRepository;
import vn.ptit.repositories.mysql.OrderRepository;
import vn.ptit.services.StatisticService;
import vn.ptit.utils.ChartReport;
import vn.ptit.utils.RevenuePerMonthUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.util.*;

@Service
public class StatisticServiceImpl implements StatisticService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ElasticsearchOperations operations;
    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private ELaptopRepository eLaptopRepository;
    @Autowired
    private EOrderRepository eOrderRepository;
    @Autowired
    private EUserRepository eUserRepository;

    @PersistenceContext
    protected EntityManager entityManager;

    @Override
    public ChartReport statisticIncomeLast5Month() {
        List<Order> orders = orderRepository.findAll();
        String label[] = new String[5];
        double data[] = new double[5];

        label[0] = RevenuePerMonthUtil.getStringMonth4();
        label[1] = RevenuePerMonthUtil.getStringMonth3();
        label[2] = RevenuePerMonthUtil.getStringMonth2();
        label[3] = RevenuePerMonthUtil.getStringMonth1();
        label[4] = RevenuePerMonthUtil.getStringMonth();

        data[0] = RevenuePerMonthUtil.getTotalMoneyMonth4(orders);
        data[1] = RevenuePerMonthUtil.getTotalMoneyMonth3(orders);
        data[2] = RevenuePerMonthUtil.getTotalMoneyMonth2(orders);
        data[3] = RevenuePerMonthUtil.getTotalMoneyMonth1(orders);
        data[4] = RevenuePerMonthUtil.getTotalMoneyMonth(orders);

        return new ChartReport(label, data);
    }

    @Override
    public ChartReport itemBestSeller() {
        SearchRequest searchRequest = new SearchRequest("lineitems");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("count_agg").field("laptopId").size(1000).subAggregation(AggregationBuilders.sum("total_quantity").field("quantity"));
        searchSourceBuilder.aggregation(aggregationBuilder);
        searchRequest.source(searchSourceBuilder);

        String label[] = new String[5];
        double data[] = new double[5];

        Map<String, Double> map = new HashMap<>();

        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            Terms contractSums = searchResponse.getAggregations().get("count_agg");
            for (Terms.Bucket bucket : contractSums.getBuckets()) {
                Sum aggValue = bucket.getAggregations().get("total_quantity");
                map.put(eLaptopRepository.findById(bucket.getKey().toString()).get().getName(), aggValue.getValue());
                System.out.println(bucket.getKey() + " " + aggValue.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return Double.compare(o2.getValue(), o1.getValue());
            }
        });

        int i = 0;
        for (Map.Entry<String, Double> m : list) {
            if (i == 5) break;
            label[i] = m.getKey();
            data[i] = m.getValue();
            i++;
        }

        return new ChartReport(label, data);

    }

    @Override
    public ChartReport statisticUserTotalMoney() {
        String sql = "SELECT users.fullName, A.SoTienMua FROM users,(SELECT SUM(payments.totalMoney) AS SoTienMua, orders.userId FROM orders,payments WHERE orders.paymentId = payments.id GROUP BY orders.userId) AS A WHERE users.id = A.userId";
        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> records = query.getResultList();

        String label[] = new String[5];
        double data[] = new double[5];

        Map<String, Double> map = new HashMap<>();

        for (int i = 0; i < records.size(); i++) {
            map.put(records.get(i)[0].toString(), Double.parseDouble(records.get(i)[1].toString()));
        }

        List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return Double.compare(o2.getValue(), o1.getValue());
            }
        });

        int i = 0;
        for (Map.Entry<String, Double> m : list) {
            if (i == 5) break;
            label[i] = m.getKey();
            data[i] = m.getValue();
            i++;
        }

        return new ChartReport(label, data);
    }

    @Override
    public long totalOrder() {
        return eOrderRepository.count();
    }

    @Override
    public long totalUser() {
        return eUserRepository.count();
    }

    @Override
    public double totalMoney() {
        SearchRequest searchRequest = new SearchRequest("payments");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        AggregationBuilder aggregationBuilder = AggregationBuilders.sum("total_money").field("totalMoney");
        searchSourceBuilder.aggregation(aggregationBuilder);
        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            Sum sum = searchResponse.getAggregations().get("total_money");
            return sum.getValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
