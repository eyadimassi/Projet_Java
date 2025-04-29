package Projetjava;

public class Categorie {
	private int id;
    private String nom;
    private int parent_id;

    public Categorie(int id, String nom, int parent_id) {
        this.id = id;
        this.nom = nom;
        this.parent_id = parent_id;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    @Override
    public String toString() {
        return "Categorie{" + "id=" + id + ", nom=" + nom + ", parent_id=" + parent_id + '}';
    }

}
