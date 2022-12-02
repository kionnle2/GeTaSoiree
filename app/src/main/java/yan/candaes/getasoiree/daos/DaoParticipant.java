package yan.candaes.getasoiree.daos;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import yan.candaes.getasoiree.beans.Participant;
import yan.candaes.getasoiree.beans.Soiree;
import yan.candaes.getasoiree.net.WSConnexionHTTPS;


public class DaoParticipant {

    private static DaoParticipant instance = null;
    private final List<Soiree> soirees;
    private final List<Participant> participants;
    private final ObjectMapper mapper = new ObjectMapper();
    private Participant logParticipant;


    private DaoParticipant() {
        soirees = new ArrayList<>();
        participants = new ArrayList<>();
    }

    public static DaoParticipant getInstance() {
        if (instance == null) {
            instance = new DaoParticipant();
        }
        return instance;
    }

    public Participant getLogParticipant() {
        return logParticipant;
    }

    public List<Soiree> getLocalSoirees() {
        return soirees;
    }

    public List<Participant> getLocalParticipants() {
        return participants;
    }

    public void simpleRequest(String request, Delegate delegate) {
        WSConnexionHTTPS ws = new WSConnexionHTTPS() {
            @Override
            public void onPostExecute(String s) {
                boolean wsRetour = false;
                if (s != null) {
                    try {
                        JSONObject jo = new JSONObject(s);
                        wsRetour = jo.getBoolean("success");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }}
                    delegate.WSRequestIsTerminated(wsRetour);

            }
        };
        ws.execute(request);
    }


    public void connexionAccount(String request, Delegate delegate) {
        WSConnexionHTTPS ws = new WSConnexionHTTPS() {
            @Override
            public void onPostExecute(String s) {
                boolean wsRetour = false;
                if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    wsRetour = jo.getBoolean("success");
                    jo = jo.getJSONObject("response");
                    logParticipant = new Participant(
                            jo.getString("login"),
                            jo.getString("nom"),
                            jo.getString("prenom"),
                            jo.getString("ddn"),
                            jo.getString("mail")
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }}
                delegate.WSRequestIsTerminated(wsRetour);
            }
        };
        ws.execute(request);
    }

    public void getLesSoirees(String request, Delegate delegate) {
        WSConnexionHTTPS ws = new WSConnexionHTTPS() {
            @Override
            public void onPostExecute(String s) {
                boolean wsRetour = false;
                if (s != null) {
                try {

                    JSONObject jo = new JSONObject(s);
                    JSONArray ja = jo.getJSONArray("response");
         /*             soirees =  mapper.readValue(ja.toString(),  new TypeReference<List<Soiree>>(){});
                      soirees =  mapper.readValue(ja.toString(), List<Soiree>.class);*/
                    soirees.clear();
                    for (int i = 0; i < ja.length(); i++) {
                        //soirees.add(mapper.readValue((DataInput) ja.getJSONObject(i), Soiree.class));
                        jo = ja.getJSONObject(i);

                        soirees.add(
                                new Soiree(
                                        jo.getString("id"),
                                        jo.getString("libelleCourt"),
                                        jo.getString("descriptif"),
                                        jo.getString("dateDebut"),
                                        jo.getString("heureDebut"),
                                        jo.getString("adresse"),
                                        jo.getString("latitude"),
                                        jo.getString("longitude"),
                                        jo.getString("login")
                                )
                        );
                    }
                    jo = new JSONObject(s);
                    wsRetour = jo.getBoolean("success");
                } catch (JSONException e) {
                    e.printStackTrace();
                }}
                delegate.WSRequestIsTerminated(wsRetour);
            }
        };
        ws.execute(request);
    }


    public void getLesParticipants(String request, Delegate delegate) {
        WSConnexionHTTPS ws = new WSConnexionHTTPS() {
            @Override
            public void onPostExecute(String s) {
                boolean wsRetour = false;
                if (s != null) {
                try {

                    JSONObject jo = new JSONObject(s);
                    JSONArray ja = jo.getJSONArray("response");
                    /*  participants =  mapper.readValue(ja.toString(),  new TypeReference<List<Participant>>(){});
                      participants =  mapper.readValue(ja.toString(), List<Soiree>.class);*/
                    participants.clear();
                    for (int i = 0; i < ja.length(); i++) {
                        //participants.add(mapper.readValue((DataInput) ja.getJSONObject(i), Participant.class));
                        jo = ja.getJSONObject(i);

                        participants.add(
                                new Participant(
                                        jo.getString("login"),
                                        jo.getString("nom"),
                                        jo.getString("prenom"),
                                        jo.getString("ddn"),
                                        jo.getString("mail")
                                )
                        );
                    }
                    jo = new JSONObject(s);
                    wsRetour = jo.getBoolean("success");
                } catch (JSONException e) {
                    e.printStackTrace();
                }}
                delegate.WSRequestIsTerminated(wsRetour);
            }
        };
        ws.execute(request);

    }

    public boolean isInscrit() {
        for (Participant p : participants) {
            if (logParticipant.getLogin().equalsIgnoreCase(p.getLogin())) return true;
        }
        return false;
    }
}