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

    public static DaoParticipant getInstance() {
        if (instance == null) {
            instance = new DaoParticipant();
        }
        return instance;
    }

    public List<Soiree> getLocalSoirees() {
        return soirees;
    }

    private DaoParticipant() {
        soirees = new ArrayList<>();
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
                    Log.d("GETGETGET", ja.toString(4));
                    //  soirees =  mapper.readValue(ja.toString(),  new TypeReference<List<Soiree>>(){});
                    //  soirees =  mapper.readValue(ja.toString(), List<Soiree>.class);
                    for (int i = 0; i < ja.length(); i++) {
                        Log.d("GETGETGET", String.valueOf(ja.getJSONObject(i)));
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









/*

package com.example.suividestage.daos;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.suividestage.beans.Etudiant;
import com.example.suividestage.net.WSConnexionHTTPS;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DaoEtudiant {
    private static DaoEtudiant instance = null;
    private final List<Etudiant> etudiants;
    private final ArrayAdapter<Etudiant> arrayAdapterEtu;
    private final Context context;
    private final ObjectMapper om = new ObjectMapper();

    private DaoEtudiant(Context context) {
        this.context = context;
        etudiants = new ArrayList<>();
        arrayAdapterEtu = new ArrayAdapter(context, android.R.layout.simple_list_item_1, etudiants);
        refreshListEtudiants();
    }

    public List<Etudiant> getEtudiants() {
        return etudiants;
    }

    public ArrayAdapter<Etudiant> getArrayAdapterEtu() {
        return arrayAdapterEtu;
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new DaoEtudiant(context);
        }
    }

    public static DaoEtudiant getInstance() {
        return instance;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    public void refreshListEtudiants() {
        String url = "uc=getEtudiants";
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                traiterRetourGetEtudiants(s);

            }
        };

        wsConnexionHTTPS.execute(url);
    }

    private void traiterRetourGetEtudiants(String s) {
        try {
            etudiants.clear();
            Arrays.asList(om.readValue(s, Etudiant[].class)).forEach(etudiant -> etudiants.add(etudiant));
            arrayAdapterEtu.notifyDataSetChanged();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    public void delEtudiant(Etudiant e) {
        String url = "uc=delEtudiant&idEtu=" + e.getIdEtu();
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                traiterRetourDelEtudiant(s);
            }
        };
        wsConnexionHTTPS.execute(url);
    }

    private void traiterRetourDelEtudiant(String s) {
        if (s.equals("1")) {
            refreshListEtudiants();
            Toast.makeText(context, "L'étudiant a été supprimé avec succès", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "L'étudiant n'a pas été supprimé.", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateEtudiant(Etudiant e) {
        String url = "uc=updateEtudiant" +
                "&nomEtu=" + e.getNomEtu() +
                "&prenomEtu=" + e.getPrenomEtu() +
                "&nomEnt=" + e.getNomEnt() +
                "&latEnt=" + e.getLatEnt() +
                "&lngEnt=" + e.getLngEnt() +
                "&idEtu=" + e.getIdEtu();
        WSConnexionHTTPS wsConnexionHTTPS = (WSConnexionHTTPS) new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                traiterRetourUpdateEtudiant(s);
            }
        }.execute(url);
    }

    private void traiterRetourUpdateEtudiant(String s) {
        if (s.equals("1")) {
            refreshListEtudiants();
            Toast.makeText(context, "L'étudiant a été modifié avec succès", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "L'étudiant n'a pas été modifié.", Toast.LENGTH_SHORT).show();
        }
    }

    public void addEtudiant(Etudiant newEtudiant) {
        String url = "uc=addEtudiant" +
                "&nomEtu=" + newEtudiant.getNomEtu() +
                "&prenomEtu=" + newEtudiant.getPrenomEtu() +
                "&nomEnt=" + newEtudiant.getNomEnt() +
                "&latEnt=" + newEtudiant.getLatEnt() +
                "&lngEnt=" + newEtudiant.getLngEnt();
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                traiterRetourAddEtudiant(s);
            }
        };
        wsConnexionHTTPS.execute(url);
    }

    private void traiterRetourAddEtudiant(String s) {
        if (s.equals("1")) {
            refreshListEtudiants();
            Toast.makeText(context, "L'étudiant a été ajouté avec succès", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "L'étudiant n'a pas été ajouté.", Toast.LENGTH_SHORT).show();
        }
    }
}
 */