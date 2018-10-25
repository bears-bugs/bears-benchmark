package com.github.example.controller;

import io.micronaut.core.util.CollectionUtils;
import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public abstract class AbstractController<M, D> {

    private final ModelMapper modelMapper;

    AbstractController(final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    protected abstract Class<D> getDtoClass();

    D convertToDto(final M source) {
        return modelMapper.map(source, getDtoClass());
    }

    List<D> convertToDto(final Collection<M> source) {
        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }
        return source.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
