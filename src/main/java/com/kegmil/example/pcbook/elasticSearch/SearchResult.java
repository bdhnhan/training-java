package com.kegmil.example.pcbook.elasticSearch;

import org.elasticsearch.common.document.DocumentField;

import java.util.List;
import java.util.Map;

public class SearchResult<T> {
    private List<T> hits;
    private Map<String, Map<String, DocumentField>> scriptFields;
    private int totalHits;
    private int totalPage;

    public SearchResult() {
    }

    public SearchResult(List<T> hits, int totalHits, Map<String, Map<String, DocumentField>> scriptFields) {
        this.hits = hits;
        this.totalHits = totalHits;
        this.scriptFields = scriptFields;
    }

    public SearchResult(List<T> hits, int totalHits, int totalPage, Map<String, Map<String, DocumentField>> scriptFields) {
        this.hits = hits;
        this.totalHits = totalHits;
        this.totalPage = totalPage;
        this.scriptFields = scriptFields;
    }

    public Map<String, Map<String, DocumentField>> getScriptFields() {
        return this.scriptFields;
    }

    public void setScriptFields(Map<String, Map<String, DocumentField>> scriptFields) {
        this.scriptFields = scriptFields;
    }

    public List<T> getHits() {
        return this.hits;
    }

    public void setHits(List<T> hits) {
        this.hits = hits;
    }

    public int getTotalHits() {
        return this.totalHits;
    }

    public void setTotalHits(int totalHits) {
        this.totalHits = totalHits;
    }

    public int getTotalPage() {
        return this.totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
