package com.example.javaecommerce.pagination;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import java.io.Serializable;
import java.util.Objects;

public class OffsetPageRequest implements Pageable, Serializable {
    private final long limit;
    private final long offset;
    private final Sort sort;

    public OffsetPageRequest(final long limit, final long offset, final Sort sort) {
        if (offset < 0) {
            throw new IllegalArgumentException("Offset index must not be less than zero");
        }
        if (limit < 1) {
            throw new IllegalArgumentException("Limit must not be less than one");
        }

        this.limit = limit;
        this.offset = offset;
        this.sort = sort;
    }
//    public OffsetPageRequest(final long limit, final long offset) {
//        this.limit = limit;
//       this.offset = offset;
//       this.sort = Sort.unsorted();
//    }

    public OffsetPageRequest(final long limit, final long offset) {
        this(offset, limit, Sort.unsorted());
    }


    @Override
    public int getPageNumber() {
        return (int) (offset / limit);
    }

    @Override
    public int getPageSize() {
        return (int) limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return new OffsetPageRequest(getOffset() + getPageSize(), getPageSize(), getSort());
    }

    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? new OffsetPageRequest(getOffset() - getPageSize(), getPageSize(), getSort()) : this;
    }

    @Override
    public Pageable first() {
        return new OffsetPageRequest(0, getPageSize(), getSort());
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return new OffsetPageRequest(pageNumber, this.getPageSize(), this.getSort());
    }

    @Override
    public boolean hasPrevious() {
        return offset > limit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OffsetPageRequest that)) return false;
        return limit == that.limit && offset == that.offset && Objects.equals(sort, that.sort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(limit, offset, sort);
    }
    @Override
    public String toString() {
        return "OffsetPageRequest{" +
                "limit=" + limit +
                ", offset=" + offset +
                ", sort=" + sort +
                '}';
    }

}
