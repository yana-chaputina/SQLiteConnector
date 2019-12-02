package ru.rosbank.javaschool.datasource.javaconfig;

import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;

@Configuration
public class JavaConfiguration {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        val configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocation(new ClassPathResource("db.properties"));
        return configurer;
    }

    @Bean
    public static SQLiteDataSource sqLiteDataSource() {
        SQLiteDataSource sqLiteDataSource = new SQLiteDataSource();
        sqLiteDataSource.setUrl("${db_url}");
        return sqLiteDataSource;
    }

    @Bean
    public static JavaSqliteConnector connector(@Value("${login}") String login, @Value("${password}") String password, DataSource sqLiteDataSource) {
        return new JavaSqliteConnector(login, password, sqLiteDataSource);
    }

}
