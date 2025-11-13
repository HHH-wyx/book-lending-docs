package com.library.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 数据库初始化类，用于在应用启动时初始化表结构和数据
 */
@Component
@Slf4j
public class DatabaseInitializer implements ApplicationRunner {

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            log.info("开始初始化数据库...");
            
            // 读取并执行初始化SQL脚本
            ClassPathResource resource = new ClassPathResource("init.sql");
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator(resource);
            populator.setSqlScriptEncoding(StandardCharsets.UTF_8.name());
            populator.execute(dataSource);
            
            log.info("数据库初始化完成！");
        } catch (Exception e) {
            log.error("数据库初始化失败：", e);
            // 打印脚本内容以便调试
            try {
                ClassPathResource resource = new ClassPathResource("init.sql");
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                    String line;
                    StringBuilder scriptContent = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        scriptContent.append(line).append("\n");
                    }
                    log.info("初始化脚本内容：\n{}", scriptContent.toString());
                }
            } catch (Exception ex) {
                log.error("读取初始化脚本失败：", ex);
            }
        }
    }
}