package com.ait.transporte.service.impl;

import com.ait.transporte.exception.OrderNotFoundException;
import com.ait.transporte.repository.IGenericRepository;
import com.ait.transporte.service.IGenericService;

import java.lang.reflect.Method;
import java.util.List;

public abstract class CRUDGenericImpl<T, ID> implements IGenericService<T, ID> {

    protected abstract IGenericRepository<T, ID> getRepository();

    @Override
    public List<T> findAll() {
        return getRepository().findAll();
    }

    @Override
    public T findById(ID id) {
        return getRepository().findById(id).orElseThrow(() -> new OrderNotFoundException("TRANSPORT ID NOT FOUND "+id));
    }

    @Override
    public T create(T t) {
        return getRepository().save(t);
    }

    @Override
    public T update(T t, ID id) throws Exception {

        //T _ = findById(id);

        String className = t.getClass().getSimpleName();
        String setID = "setId" + className;
        Method methodName = t.getClass().getMethod(setID, id.getClass());
        methodName.invoke(t, id);

        return getRepository().save(t);
    }

    @Override
    public void delete(ID id) {
        getRepository().deleteById(id);
    }
}

