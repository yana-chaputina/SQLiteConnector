package ru.rosbank.javaschool.datasource.kotlin

import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.context.support.beans
import org.springframework.core.io.ClassPathResource
import org.sqlite.SQLiteDataSource

val beans = beans {
    bean {
        PropertySourcesPlaceholderConfigurer().apply {
            setLocation(ClassPathResource("db.properties"))
        }
    }
    bean<KotlinSqliteConnector>("connector")
    bean("datasource") {
        SQLiteDataSource().apply {
            url = "java:/comp/env/jdbc/db"
        }
    }
}