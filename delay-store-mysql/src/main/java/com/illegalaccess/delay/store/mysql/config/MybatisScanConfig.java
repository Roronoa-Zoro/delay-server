package com.illegalaccess.delay.store.mysql.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Jimmy Li
 * @date 2021-03-04 10:29
 */
@Configuration
@MapperScan("com.illegalaccess.delay.store.mysql.mapper")
public class MybatisScanConfig {
}
