package com.wangy.common.model;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.List;

/**
 * 分页（请求/返回）数据包装类
 *
 * @author wangy
 * @date 2021-1-29 16:55
 * @see Page
 * @see OrderItem
 */
@Data
public class PageDomain<T> {

    private Long currentPage;

    private Long pageSize;

    private Long total;

    private Long pages;

    private List<T> records;

    public PageDomain(IPage<T> page) {
        this.currentPage = page.getCurrent();
        this.pageSize = page.getSize();
        this.total = page.getTotal();
        this.pages = page.getPages();
        this.records = page.getRecords();
    }

    public PageDomain(IPage<T> page, List<T> records) {
        this.currentPage = page.getCurrent();
        this.pageSize = page.getSize();
        this.total = page.getTotal();
        this.pages = page.getPages();
        this.records = records;
    }

    public class OrderItems extends OrderItem {
        // no features add right now.
    }
}
