package com.kegmil.example.pcbook.elasticSearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kegmil.example.pcbook.mapper.JsonHelper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaElasticSearch {
    private static final Logger logger = Logger.getLogger(JavaElasticSearch.class.getName());
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final TypeReference<Map<String, Object>> entityOfTypeReference = new TypeReference<Map<String, Object>>() {
    };

    public static BulkResponse pushData(String realm, String item) {
        try {
            RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
                    RestClient.builder(new HttpHost("localhost", 9200, "http"))
            );

            BulkRequest bulkRequest = new BulkRequest();
            bulkRequest.timeout(TimeValue.timeValueMinutes(20));
            Collections.singletonList(item).forEach(itemJson -> {
                try {
                    Map<String, Object> map = (Map) objectMapper.readValue(itemJson, entityOfTypeReference);
                    UpdateRequest updateRequest = (new UpdateRequest(realm, String.valueOf(map.get("id")))).doc(itemJson, XContentType.JSON).upsert(itemJson, XContentType.JSON);
                    bulkRequest.add(updateRequest);
                } catch (JsonProcessingException var7) {
                    var7.printStackTrace();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            return bulkResponse;
        } catch (Exception var8) {
            throw new RuntimeException(var8);
        }
    }

    private static SearchResponse search(String realm, SearchSourceBuilder searchSourceBuilder) {
        try {
            SearchRequest searchRequest = (new SearchRequest(new String[]{realm})).source(searchSourceBuilder);
            RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
                    RestClient.builder(new HttpHost("localhost", 9200, "http"))
            );
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            restHighLevelClient.close();
            return searchResponse;
        } catch (Exception var6) {
            throw new RuntimeException(var6);
        }
    }

    public static  <T> SearchResult<T> search(String realm, SearchSourceBuilder searchSourceBuilder, Class<T> targetClass) {
        SearchResponse searchResponse = search(realm, searchSourceBuilder);
        int totalHits = (int)searchResponse.getHits().getTotalHits().value;
        int size = searchSourceBuilder.size();
        int totalPage = totalHits / size + (totalHits % size == 0 ? 0 : 1);
        List<T> items = new ArrayList();
        SearchHit[] hits = searchResponse.getHits().getHits();
        convertSearchHitsToTargetObjects(hits, items, JsonHelper.getMapper(targetClass));
        return constructResultWithTotalPage(items, totalHits, totalPage, getScripFieldsFromSearchHits(searchResponse.getHits()));
    }

    private static <T> void convertSearchHitsToTargetObjects(SearchHit[] hits, List<T> items, Function<String, T> mapper) {
        SearchHit[] var4 = hits;
        int var5 = hits.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            SearchHit hit = var4[var6];
            if (hit.getSourceAsString() != null) {
                items.add(mapper.apply(hit.getSourceAsString()));
            }
        }
    }

    private static <T> SearchResult<T> constructResultWithTotalPage(List<T> items, int totalHits, int totalPage, Map<String, Map<String, DocumentField>> scripFieldsFromResponse) {
        return new SearchResult(items, totalHits, totalPage, scripFieldsFromResponse);
    }

    private static Map<String, Map<String, DocumentField>> getScripFieldsFromSearchHits(SearchHits searchHits) {
        return (Map) Stream.of(searchHits.getHits()).collect(Collectors.toMap(SearchHit::getId, SearchHit::getFields));
    }
}
