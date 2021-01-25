package com.wangy.config;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author wangy
 * @version 1.0
 * @date 2021/1/24 / 22:54
 */
@Configuration
@MapperScan("com.wangy.service.mapper")
public class MybatisPlusConfig {

    @Autowired
    DataSource hiKariDatasource;

    /*@Bean
    public MybatisConfiguration configuration(){
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setCacheEnabled(true);
    }*/

    @Bean
    public MybatisSqlSessionFactoryBean sqlSessionFactory(){
       MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean =
           new MybatisSqlSessionFactoryBean();
       mybatisSqlSessionFactoryBean.setDataSource(hiKariDatasource);
       return  mybatisSqlSessionFactoryBean;
    }

}
