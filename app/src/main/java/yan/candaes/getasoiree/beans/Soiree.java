package yan.candaes.getasoiree.beans;

public class Soiree{
    private String id;
    private String libelleCourt;
    private String descriptif;
    private String dateDebut;
    private String heureDebut;
    private String adresse;
    private String latitude;
    private String longitude;
    private String login;

    public String getId() {
        return id;
    }

    public String getLibelleCourt() {
        return libelleCourt;
    }

    public String getDescriptif() {
        return descriptif;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getHeureDebut() {
        return heureDebut;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLogin() {
        return login;
    }

    public Soiree(String id, String libelleCourt, String descriptif, String dateDebut, String heureDebut, String adresse, String latitude, String longitude, String login) {
        this.id = id;
        this.libelleCourt = libelleCourt;
        this.descriptif = descriptif;
        this.dateDebut = dateDebut;
        this.heureDebut = heureDebut;
        this.adresse = adresse;
        this.latitude = latitude;
        this.longitude = longitude;
        this.login = login;
    }

    @Override
    public String toString() {
        return libelleCourt +  " le " + dateDebut + ", de " + login;
    }
}
