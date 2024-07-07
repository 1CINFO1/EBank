package com.ebank.application.interfaces;

import java.util.List;

public interface IDiscution<T> {
    // CRUD
    public int add(T t);

    public void delete(int t);

    public void update(T t, int id);

    public List<T> getAll();
}
