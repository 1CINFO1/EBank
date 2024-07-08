package com.ebank.application.controllers;

import com.ebank.application.models.CarteBancaire;
import com.ebank.application.services.CarteService;

import java.util.List;

public class CarteController {
    private CarteService carteService;

    public CarteController() {
        this.carteService = new CarteService();
    }

    public String addCarte(CarteBancaire carte) {
        return carteService.add(carte);
    }

    public void deleteCarte(int id) {
        carteService.delete(id);
    }

    public void updateCarte(CarteBancaire carte, int id) {
        carteService.update(carte, id);
    }

    public List<CarteBancaire> getAllCartes() {
        return carteService.getAll();
    }
}