import java.sql.Blob;
import java.util.Scanner;

enum Version {
    VO, VF
}
enum Genre{Action, Aventure, Comédie, Drame, Fantaisie, Horreur, Romance,Sciencefiction, Thriller, Autre;}
public class Film {
    private String nom;
    private java.sql.Time  duree;
    private Genre genre;
    private Version version;

    private  int id;
    private Blob photo;
    private String displayName;

    public Film(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }



    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;  // This is what will be displayed in the JComboBox
    }


    public Film(String nom, java.sql.Time duree, Genre genre, Version version,int id) {
        this.nom = nom;
        this.duree = duree;
        this.genre = genre;
        this.version = version;
        this.id=id;
    }

    public Film(String nom, java.sql.Time duree, Genre genre, Version version, Blob photo, int id) {
        this.nom = nom;
        this.duree = duree;
        this.genre = genre;
        this.version = version;
        this.id=id;
        this.photo=photo;
    }

    public Film() {
        //TODO Auto-generated constructor stub
    }

    public void modifierVersion() {
        if (version == Version.VO) {
            version = Version.VF;

        } else {
            version = Version.VO;

        }
    }

    public void modifierFilm(int choix, Object object) {
        Scanner scanner = new Scanner(System.in);
        switch (choix) {
            case 1:
                this.setNom((String) object);
                break;
            case 2:

                this.setDuree((java.sql.Time) object);
                break;
            case 3:
                this.setGenre ((Genre) object);
                break;
            case 4:
                modifierVersion();
                break;
            default:
throw new IllegalArgumentException("Choix invalide: " + choix);
        }
    }

    public String getNom() {
        return nom;

    }

    public int getId() {
        return id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public java.sql.Time getDuree() {
        return duree;
    }

    public void setDuree(java.sql.Time  duree) {
        this.duree = duree;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Blob getPhoto() {
        return photo;
    }

    public void setPhoto(Blob photo) {
        this.photo = photo;
    }
}
