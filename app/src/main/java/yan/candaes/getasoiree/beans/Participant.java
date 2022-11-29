package yan.candaes.getasoiree.beans;

public class Participant {
    String login;
    String nom;
    String prenom;
    String ddn;
    String mail;

    public Participant(String login, String nom, String prenom, String ddn, String mail) {
        this.login = login;
        this.nom = nom;
        this.prenom = prenom;
        this.ddn = ddn;
        this.mail = mail;
    }

    public String getLogin() {
        return login;
    }

    @Override
    public String toString() {
        return login + "(" + nom + " " + prenom + ')';
    }
}
