package com.sigma.dto;

import com.sigma.model.Acheteur;
import com.sigma.model.ResponsableAchat;

import java.util.List;



public class EquipeDto {
  private ResponsableAchat responsable;

  private String libelle;

  private List<Acheteur> membres;

  public String getLibelle() {
    return this.libelle;
  }

  public void setLibelle(String libelle) {
    this.libelle = libelle;
  }

  public ResponsableAchat getResponsable() {
    return this.responsable;
  }

  public void setResponsable(ResponsableAchat responsable) {
    this.responsable = responsable;
  }

  public List<Acheteur> getMembres() {
    return this.membres;
  }

  public void setMembres(List<Acheteur> membres) {
    this.membres = membres;
  }
}
