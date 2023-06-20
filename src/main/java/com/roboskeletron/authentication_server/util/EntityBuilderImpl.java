package com.roboskeletron.authentication_server.util;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EntityBuilderImpl<T extends Entity> implements EntityBuilder<T> {
    private final T entity;

    public EntityBuilderImpl<T> setName(String name){
        entity.setName(name);
        return this;
    }

    public EntityBuilder<T> setId(int id){
        entity.setId(id);
        return this;
    }

    @Override
    public T build() {
        return entity;
    }
}
