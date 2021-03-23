package com.wangy.base;

import com.wangy.WebSecurityDemoApplicationTests;
import com.wangy.model.entity.Spitter;
import com.wangy.service.mapper.SpitterMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author wangy
 * @version 1.0
 * @date 2021/3/23 / 21:19
 */
@SpringBootTest
@Slf4j
public class DaoLockTest extends WebSecurityDemoApplicationTests {

    @Resource
    private SpitterMapper spitterMapper;

    @Autowired
    private ThreadPoolTaskExecutor poolExecutor;

    @Autowired
    private DataSourceTransactionManager txManager;
    @Autowired
    private TransactionDefinition txDefinition;

    CountDownLatch latch = new CountDownLatch(2);


    /**
     * 测试mybatis-plus的乐观锁
     */
    @Order(1)
    @Test
    public void optimisticLockTest() throws Exception {
        final Spitter entity = spitterMapper.selectById(1);
        for (int i = 0; i < 2; i++) {
            if (i % 2 == 0) {
                poolExecutor.execute(() -> {
                    try {
                        entity.setUsername("sc300");
                        spitterMapper.updateById(entity);
                    } finally {
                        latch.countDown();
                    }
                    log.info("{} updated.", Thread.currentThread().getName());
                });
            } else {
                poolExecutor.execute(() -> {
                    try {
                        entity.setUsername("sc3000");
                        spitterMapper.updateById(entity);
                    } finally {
                        latch.countDown();
                    }
                    log.info("{} updated.", Thread.currentThread().getName());
                });
            }
        }
        latch.await();
        log.info("updated version: {}", spitterMapper.selectById(1).getVersion());

    }

    /**
     * 测试悲观锁
     * <p>
     * 配合事务使用
     */
    @Order(2)
    @Test
    public void pessimisticLockTest() throws Exception {
        for (int i = 0; i < 2; i++) {
            if (i % 2 == 0) {
                poolExecutor.execute(() -> {
                    TransactionStatus tx = txManager.getTransaction(txDefinition);
                    try {
                        Spitter entity = spitterMapper.selectByIdLocked(1);
                        int version = entity.getVersion();
                        log.info("{} get username:{}, version:{}",
                            Thread.currentThread().getName(), entity.getUsername(), version);
                        spitterService.lambdaUpdate()
                            .set(Spitter::getUsername, "sc"+ (++version))
                            .set(Spitter::getVersion, version)
                            .eq(Spitter::getId, entity.getId())
                            .update();
//                        TimeUnit.SECONDS.sleep(1);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        txManager.rollback(tx);
                    } finally {
                        latch.countDown();
                    }
                    txManager.commit(tx);
                    log.info("{} updated.", Thread.currentThread().getName());
                });
            } else {
                poolExecutor.execute(() -> {
                    TransactionStatus tx = txManager.getTransaction(txDefinition);
                    try {
                        Spitter entity = spitterMapper.selectByIdLocked(1);
                        int version = entity.getVersion();
                        log.info("{} get username:{}, version:{}",
                            Thread.currentThread().getName(), entity.getUsername(), version);
                        spitterService.lambdaUpdate()
                            .set(Spitter::getUsername, "sc" + (++version))
                            .set(Spitter::getVersion, version)
                            .eq(Spitter::getId, entity.getId())
                            .update();
                    } finally {
                        latch.countDown();
                    }
                    txManager.commit(tx);
                    log.info("{} updated.", Thread.currentThread().getName());
                });
            }
        }
        latch.await();
        log.info("updated version: {}", spitterMapper.selectById(1).getVersion());
    }
}
