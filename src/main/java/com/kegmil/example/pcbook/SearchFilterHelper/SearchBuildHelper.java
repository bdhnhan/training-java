package com.kegmil.example.pcbook.SearchFilterHelper;

import com.kegmil.example.pcbook.pb.Filter;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class SearchBuildHelper {
    public static QueryBuilder buildQueryBuilder(Filter filter) {
        BoolQueryBuilder context = QueryBuilders.boolQuery();

        //a query write here

        return context;
    }
}
