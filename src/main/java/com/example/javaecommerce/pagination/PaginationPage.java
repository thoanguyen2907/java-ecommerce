package com.example.javaecommerce.pagination;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class PaginationPage<T> {
    private long totalRecords;
    private long offset;
    private long limit;
    private Collection<T> records;

    public PaginationPage(final long totalRecords, final long offset, final long limit, final List<T> records) {
        this.totalRecords = totalRecords;
        this.offset = offset;
        this.limit = limit;
        this.records = records;
    }

    public PaginationPage(final long totalRecords, final long offset, final long limit, final Set<T> records) {
        this.totalRecords = totalRecords;
        this.offset = offset;
        this.limit = limit;
        this.records = records;
    }

    public PaginationPage() {
        this(0, 0, 0, Collections.emptyList());
    }

    public long getTotalRecords() {
        return totalRecords == 0L ? records.size() : totalRecords;
    }

    public PaginationPage<T> setTotalRecords(final long totalRecords) {
        this.totalRecords = totalRecords;
        return this;
    }

    public long getOffset() {
        return offset;
    }

    public PaginationPage<T> setOffset(final long offset) {
        this.offset = offset;
        return this;
    }

    public long getLimit() {
        return limit;
    }

    public PaginationPage<T> setLimit(final long limit) {
        this.limit = limit;
        return this;
    }

    public Collection<T> getRecords() {
        return records;
    }

    public PaginationPage<T> setRecords(final Collection<T> records) {
        this.records = records == null ? Collections.emptyList() : records;
        return this;
    }
}
