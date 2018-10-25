package com.github.example.factory;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import org.modelmapper.ModelMapper;

import javax.inject.Singleton;

@Factory
public class ModelMapperFactory {

    @Bean
    @Singleton
    ModelMapper create() {
        return new ModelMapper();
    }
}
