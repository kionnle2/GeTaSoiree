package yan.candaes.getasoiree.beans;

public class Participant {
    String login;
    String mdp;
    String mdp2;
    String nom;
    String prenom;
    String ddn;
    String mail;

    public Participant(String login, String mdp, String mdp2, String nom, String prenom, String ddn, String mail) {
        this.login = login;
        this.mdp = mdp;
        this.mdp2 = mdp2;
        this.nom = nom;
        this.prenom = prenom;
        this.ddn = ddn;
        this.mail = mail;
    }

    public String getLogin() {
        return login;
    }

    public String getMdp() {
        return mdp;
    }

    public String getMdp2() {
        return mdp2;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getDdn() {
        return ddn;
    }

    public String getMail() {
        return mail;
    }

}
