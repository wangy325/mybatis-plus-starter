package com.wangy.common.model;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 分页数据（请求/返回）包装类
 *
 * @author wangy
 * @date 2021-1-29 16:55
 * @see Page
 * @see OrderItem
 */
public class PageDomain<T> extends Page<T> {

    /**
     * rename from {@link Page#current}
     */
    private long currentPage;
    /**
     * rename from {@link Page#size}
     */
    private long pageSize;
    /**
     * rename from {@link Page#total}
     */
    private long totalItems;
    /**
     * num of pages
     */
    private long pages;

    /**
     * rename from {@link Page#orders}
     */
    private List<OrderItems> orders = new ArrayList<>();

    public PageDomain(long currentPage, long pageSize) {
        super(currentPage, pageSize);
        this.currentPage = super.current;
        this.pageSize = super.size;
    }

    public PageDomain(long currentPage, long pageSize, long totalItems) {
        super(currentPage, pageSize, totalItems);
        this.currentPage = super.current;
        this.pageSize = super.size;
        this.totalItems = super.total;
    }


    /**
     * override super {@link Page#addOrder(OrderItem...)}
     */
    @SafeVarargs
    public final PageDomain<T> addOrder(OrderItems... items) {
        orders.addAll(Arrays.asList(items));
        return this;
    }

    /**
     * override super {@link Page#addOrder(List)}
     */
    public PageDomain<T> addOrders(List<OrderItems> orderItemsList) {
        orders.addAll(orderItemsList);
        return this;
    }


    @Override
    public IPage<T> setPages(long pages) {
        long pages1 = super.getPages();
        this.pages = pages;
        return this;
    }

    public class OrderItems extends OrderItem {
        // no features add right now.
    }
}
