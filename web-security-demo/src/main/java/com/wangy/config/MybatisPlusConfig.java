package com.wangy.config;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;


/**
 * 使用mybatis-plus，无需配置datasource和sqlSessionFactory（springboot自动配置）<br>
 * 这个配置文件一些其他内容（分页，...）
 *
 * @author wangy
 * @version 1.0
 * @date 2021/1/24 / 22:54
 * @see MybatisPlusAutoConfiguration
 * @see DataSourceAutoConfiguration
 * @see DataSourceTransactionManagerAutoConfiguration
 */
@Configuration
@MapperScan("com.wangy.service.mapper")
public class MybatisPlusConfig {

    @Bean
    public PaginationInterceptor pagination() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        // paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        // paginationInterceptor.setLimit(500);
        // 开启 count 的 join 优化,只针对部分 left join
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        return paginationInterceptor;
    }

    /**
     * auto configured by spring boot.
     */
    public TransactionManager transactionManager() {
        return new DataSourceTransactionManager();
    }
}
