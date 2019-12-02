package ru.rosbank.javaschool.datasource;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.QualifierAnnotationAutowireCandidateResolver;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.groovy.GroovyBeanDefinitionReader;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.sqlite.SQLiteDataSource;
import ru.rosbank.javaschool.datasource.annotation.AnnotationSqliteConnector;
import ru.rosbank.javaschool.datasource.groovy.GroovySqliteConnector;
import ru.rosbank.javaschool.datasource.javaconfig.JavaConfiguration;
import ru.rosbank.javaschool.datasource.javaconfig.JavaSqliteConnector;
import ru.rosbank.javaschool.datasource.kotlin.BeansKt;
import ru.rosbank.javaschool.datasource.kotlin.KotlinSqliteConnector;
import ru.rosbank.javaschool.datasource.programmatic.ProgrammaticSqliteConnector;
import ru.rosbank.javaschool.datasource.xml.XmlSqliteConnector;

import static org.junit.jupiter.api.Assertions.*;

class SqliteConnectorTests {

    @Test
    public void XmlConfig()
    {
        val context = new GenericApplicationContext();
        new XmlBeanDefinitionReader(context).loadBeanDefinitions("context.xml");
        context.refresh();
        XmlSqliteConnector result=(XmlSqliteConnector)context.getBean("connector");
        assertEquals("admin",result.getLogin());
        assertEquals("password",result.getPassword());
    }

    @Test
    public void AnnotationConfig()
    {
        val context = new AnnotationConfigApplicationContext("ru.rosbank.javaschool.datasource.annotation");
        AnnotationSqliteConnector result=(AnnotationSqliteConnector)context.getBean("connector");
        assertEquals("admin",result.getLogin());
        assertEquals("password",result.getPassword());
    }

    @Test
    public void JavaConfig()
    {
        val context = new AnnotationConfigApplicationContext(JavaConfiguration.class);
        JavaSqliteConnector result=(JavaSqliteConnector)context.getBean("connector");
        assertEquals("admin",result.getLogin());
        assertEquals("password",result.getPassword());
    }

    @Test
    public void ProgrammaticConfig()
    {
        val context = new GenericApplicationContext();
        context.registerBean(PropertySourcesPlaceholderConfigurer.class, () -> {
            val configurer = new PropertySourcesPlaceholderConfigurer();
            configurer.setLocation(new ClassPathResource("db.properties"));
            return configurer;
        });
        context.registerBean("datasource", SQLiteDataSource.class, db -> {
            db.getPropertyValues().addPropertyValue("url","${db_url}");
        });
        context.registerBean("connector", ProgrammaticSqliteConnector.class,
                "${login}", "${password}",
                new RuntimeBeanReference("datasource"));
        context.refresh();
        ProgrammaticSqliteConnector result=(ProgrammaticSqliteConnector)context.getBean("connector");
        assertEquals("admin",result.getLogin());
        assertEquals("password",result.getPassword());
    }

    @Test
    public void GroovyConfig()
    {
        val context = new GenericApplicationContext();
        val reader = new GroovyBeanDefinitionReader(context);
        reader.loadBeanDefinitions("context.groovy");
        context.refresh();
        GroovySqliteConnector result=(GroovySqliteConnector)context.getBean("connector");
        assertEquals("admin",result.getLogin());
        assertEquals("password",result.getPassword());
    }

    @Test
    public void KotlinConfig()
    {
        val factory = new DefaultListableBeanFactory();
        factory.setAutowireCandidateResolver(new QualifierAnnotationAutowireCandidateResolver());
        val context = new GenericApplicationContext(factory);
        BeansKt.getBeans().initialize(context);
        context.refresh();
        KotlinSqliteConnector result=(KotlinSqliteConnector)context.getBean("connector");
        assertEquals("admin",result.getLogin());
        assertEquals("password",result.getPassword());
    }

}
