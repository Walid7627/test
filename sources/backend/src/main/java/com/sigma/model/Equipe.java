package com.sigma.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.List;

@Entity
@JsonIdentityInfo(
  generator = ObjectIdGenerators.PropertyGenerator.class,
  property = "id")
public class Equipe {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @OneToOne
  private ResponsableAchat responsable;

  private String libelle;

  @OneToMany(mappedBy="equipe")
  private List<Acheteur> membres;
  
  @ManyToOne
  private Entite entite;

  public Equipe(String libelle, ResponsableAchat responsable, List<Acheteur> membres) {
    this.libelle = libelle;
    this.responsable = responsable;
    this.membres = membres;
  }

  public Equipe() { }

  public Long getId() {
    return this.id;
  }

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

  public void addMembre(Acheteur membre) {
    this.membres.add(membre);
  }

  public void removeMembre(Acheteur membre) {
    this.membres.remove(membre);
  }

public Entite getEntite() {
	return entite;
}

public void setEntite(Entite entite) {
	this.entite = entite;
}
}
