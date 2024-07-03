package com.ebank.application.interfaces;

import java.util.List;

public interface InterfaceCRUD<T> {
    // CRUD
    public void add(T t);

    public void delete(int t);

    public void update(T t, int id);

    public List<T> getAll();
}
