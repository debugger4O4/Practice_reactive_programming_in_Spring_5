package ru.study.chapter_05._01_quick_start_is_the_key_to_success;

import org.springframework.beans.factory.groovy.GroovyBeanDefinitionReader;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;

/**
 * Быстрый старт как ключ к успеху.
 */
public class SpringApp {
    public static void main(String[] args) {
        // 3 разных способа регистрации компонентов в контексте Spring.
        GenericApplicationContext context = new GenericApplicationContext();

        new XmlBeanDefinitionReader(context)
                .loadBeanDefinitions("services.xml");

        new GroovyBeanDefinitionReader(context)
                .loadBeanDefinitions("services.groovy");

        new PropertiesBeanDefinitionReader(context)
                .loadBeanDefinitions("services.properties");

        context.refresh();

    }
}
