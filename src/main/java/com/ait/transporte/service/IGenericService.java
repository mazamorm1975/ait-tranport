package com.ait.transporte.service;

import java.util.List;


public interface IGenericService <T,UUID>{

    List<T> findAll();
    T findById(UUID id);
    T create(T t);
    T update(T t,  UUID id) throws  Exception;
    void delete(UUID id);
}
