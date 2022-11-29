package yan.candaes.getasoiree.daos;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import yan.candaes.getasoiree.beans.Participant;
import yan.candaes.getasoiree.beans.Soiree;
import yan.candaes.getasoiree.beans.WsRetour;
import yan.candaes.getasoiree.net.WSConnexionHTTPS;


public class DaoParticipant {

    private static DaoParticipant instance = null;
    private List<Soiree> soirees;
    private final ObjectMapper mapper = new ObjectMapper();
    private Participant logParticipant;

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

    private DaoParticipant() {
        soirees = new ArrayList<>();
    }

    public void connexionAccount(String request, Delegate delegate) {
        WSConnexionHTTPS ws = new WSConnexionHTTPS() {
            @Override
            public void onPostExecute(String s) {
                boolean wsRetour = false;
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
                }
                delegate.WSRequestIsTerminated(wsRetour);
            }
        };
        ws.execute(request);
    }

    public void getSoiree(String request, Delegate delegate) {
        WSConnexionHTTPS ws = new WSConnexionHTTPS() {
            @Override
            public void onPostExecute(String s) {
                boolean wsRetour = false;
                JSONObject jo;
                JSONArray ja = null;
                try {
                    jo = new JSONObject(s);
                    ja = jo.getJSONArray("response");
                    //  soirees =  mapper.readValue(ja.toString(),  new TypeReference<List<Soiree>>(){});
                    //  soirees =  mapper.readValue(ja.toString(), List<Soiree>.class);
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
                }
                delegate.WSRequestIsTerminated(wsRetour);
            }
        };
        ws.execute(request);
    }


    public void simpleRequest(String request, Delegate delegate) {
        WSConnexionHTTPS ws = new WSConnexionHTTPS() {
            @Override
            public void onPostExecute(String s) {
                boolean wsRetour = false;
                try {
                    JSONObject jo = new JSONObject(s);
                    wsRetour = jo.getBoolean("success");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                delegate.WSRequestIsTerminated(wsRetour);
            }
        };
        ws.execute(request);
    }
}