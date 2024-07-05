package com.ebank.application.models;

import java.time.LocalDate;

public class CharityCampaignModel extends User {
    private String CompagnieDeDon_Patente;
    private final String role = "CHARITY";

    public CharityCampaignModel(String name, String email, LocalDate dob, int acc_num, double balance, String password, String compagnieDeDon_Patente) {
        super(name, email, dob, acc_num, balance, password);
        CompagnieDeDon_Patente = compagnieDeDon_Patente;
    }

    public CharityCampaignModel() {
        super();
    }

    public CharityCampaignModel(String name, String userEmail, LocalDate dob, int accNum, double balance, String compagnieDeDon_Patente) {
        super(name, userEmail, dob, accNum, balance);
        CompagnieDeDon_Patente = compagnieDeDon_Patente;
    }

    public String getCompagnieDeDon_Patente() {
        return CompagnieDeDon_Patente;
    }

    public void setCompagnieDeDon_Patente(String compagnieDeDon_Patente) {
        CompagnieDeDon_Patente = compagnieDeDon_Patente;
    }

    public String getRole() {
        return role;
    }
}
