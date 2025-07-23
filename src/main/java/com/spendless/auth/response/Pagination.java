package com.spendless.auth.response;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Pagination {
    private int currentPage;
    private int limit;
    private long totalItems;
    private int totalPages;
    private LocalDateTime createdAt=LocalDateTime.now();

    public Pagination(int currentPage, int limit, long totalItems, int totalPages) {
        this.currentPage = currentPage;
        this.limit = limit;
        this.totalItems = totalItems;
        this.totalPages = (int) Math.ceil(totalPages);
    }

    public Pagination() {

    }
}
