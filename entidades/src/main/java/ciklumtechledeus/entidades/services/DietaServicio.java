package ciklumtechledeus.entidades.services;

import ciklumtechledeus.entidades.repositories.DietaRepository;

public class DietaServicio {
    private DietaRepository repo;

    public DietaServicio(DietaRepository dietaRepository){
        this.repo = dietaRepository;
    }
    
}
