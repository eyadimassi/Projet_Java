package Projetjava;

import java.time.LocalDate;

public class Produit {
	 private int id;
     private String nom;
     private String description;
     private int prix;
     private int categorie_id;
     private int marque_id;
     private LocalDate created_at;
     private String image_path=null;
     private String imagehover_path=null;
     private int ordered;
     private String shade=null;

  public int getId() {
      return id;
  }

  public String getNom() {
      return nom;
  }

  public String getDescription() {
      return description;
  }

  public int getPrix() {
      return prix;
  }

  public int getCategorie_id() {
      return categorie_id;
  }

  public int getMarque_id() {
      return marque_id;
  }

  public LocalDate getCreated_at() {
      return created_at;
  }

  public String getImage_path() {
      return image_path;
  }

  public String getImagehover_path() {
      return imagehover_path;
  }

  public int getOrdered() {
      return ordered;
  }

  public String getShade() {
      return shade;
  }

  public void setId(int id) {
      this.id = id;
  }

  public void setNom(String nom) {
      this.nom = nom;
  }

  public void setDescription(String description) {
      this.description = description;
  }

  public void setPrix(int prix) {
      this.prix = prix;
  }

  public void setCategorie_id(int categorie_id) {
      this.categorie_id = categorie_id;
  }

  public void setMarque_id(int marque_id) {
      this.marque_id = marque_id;
  }

  public void setCreated_at(LocalDate created_at) {
      this.created_at = created_at;
  }

  public void setImage_path(String image_path) {
      this.image_path = image_path;
  }

  public void setImagehover_path(String imagehover_path) {
      this.imagehover_path = imagehover_path;
  }

  public void setOrdered(int order) {
      this.ordered = order;
  }

  public void setShade(String shade) {
      this.shade = shade;
  }

  public Produit(int id, String nom, String description, int prix, int categorie_id, int marque_id, 
                 LocalDate created_at, String image_path, String imagehover_path, int ordered, String shade) {
      this.id = id;
      this.nom = nom;
      this.description = description;
      this.prix = prix;
      this.categorie_id = categorie_id;
      this.marque_id = marque_id;
      this.created_at = created_at;
      this.image_path = image_path;
      this.imagehover_path = imagehover_path;
      this.ordered = ordered;
      this.shade = shade;
  }

  @Override
  public String toString() {
      return "Produit{" + "id=" + id + ", nom=" + nom + ", description=" + description + ", prix=" + prix + ", categorie_id=" + categorie_id + ", marque_id=" + marque_id + ", created_at=" + created_at + ", image_path=" + image_path + ", imagehover_path=" + imagehover_path + ", order=" + ordered + ", shade=" + shade + '}';
  }

}
