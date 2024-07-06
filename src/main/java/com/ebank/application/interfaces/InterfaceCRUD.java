package com.ebank.application.interfaces;

import com.ebank.application.models.CharityCampaignModel;
import com.ebank.application.models.Publication;

import java.util.List;

public interface InterfaceCRUD<T> {
    // CRUD
    public String add(T t);

    public void delete(int t);

    public void update(T t, int id);

    public List<T> getAll();
    public Publication getById(int id);

    public CharityCampaignModel getCharityBy(int id);
}
