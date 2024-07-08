package com.ebank.application.controllers;

import com.ebank.application.models.Cheque;
import com.ebank.application.services.ChequeService;

import java.util.List;

public class ChequeController {
    private ChequeService chequeService;

    public ChequeController() {
        this.chequeService = new ChequeService();
    }

    public String addCheque(Cheque c) {
        return chequeService.add(c);
    }

    public void deleteCheque(int id) {
        chequeService.delete(id);
    }

    public void updateCheque(Cheque c, int id) {
        chequeService.update(c, id);
    }

    public List<Cheque> getAllCheques() {
        return chequeService.getAll();
    }
}