package yan.candaes.getasoiree.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import yan.candaes.getasoiree.R;
import yan.candaes.getasoiree.beans.Participant;
import yan.candaes.getasoiree.beans.Soiree;
import yan.candaes.getasoiree.daos.DaoParticipant;
import yan.candaes.getasoiree.daos.Delegate;

public class InfoSoireeActivity extends AppCompatActivity {
    ArrayAdapter<Participant> infoAdap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_soiree);

        //recup de la soiree et remplis les textes
        Soiree s = DaoParticipant.getInstance().getLocalSoirees().get(getIntent().getIntExtra("position", -1));
        ((TextView) findViewById(R.id.infoLib)).setText(s.getLibelleCourt());
        ((TextView) findViewById(R.id.infoDesc)).setText(s.getDescriptif());
        String[] date = s.getDateDebut().split("-");
        ((TextView) findViewById(R.id.infoDate)).setText("Date : " + date[2] + "-" + date[1] + "-" + date[0]);
        String[] heure = s.getHeureDebut().split(":");
        ((TextView) findViewById(R.id.infoHeure)).setText("Heure : " + heure[0] + ":" + heure[1]);
        ((TextView) findViewById(R.id.infoLog)).setText("Soirée déposée  par " + s.getLogin());

//adapter + get les participants
        infoAdap = new ArrayAdapter(this, android.R.layout.simple_list_item_1, DaoParticipant.getInstance().getLocalParticipants());
        ((ListView) (findViewById(R.id.infoLvParticipant))).setAdapter(infoAdap);
        String request = ("requete=getLesParticipants&soiree=" + s.getId());
        refreshList(request, s);

        findViewById(R.id.infoBtnBack).setOnClickListener(View -> finish());

    }


    private void refreshList(String request, Soiree s) {
        DaoParticipant.getInstance().getLesParticipants(request, new Delegate() {
            @Override
            public void WSRequestIsTerminated(Object result) {
                if ((boolean) result) {
                    infoAdap.notifyDataSetChanged();
                    DaoParticipant.getInstance().getLocalParticipants().forEach(p -> {
                        if (p.getLogin().equalsIgnoreCase(s.getLogin()))
                            ((TextView) findViewById(R.id.infoLog)).setText("Soirée déposée  par " + p.getNom() + " " + p.getPrenom());
                    });

                    setupButton(s);
                } else
                    Toast.makeText(getApplicationContext(), "la récuperation des particiants a échouée", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupButton(Soiree s) {
        //Si créateur propose de suppr sinon si inscrit propose de desinscrire et inversement
        int ETAT; //0=suppr 1=desinscr 2=inscr
        String BTNNAME;
        if (DaoParticipant.getInstance().getLogParticipant().getLogin().equalsIgnoreCase(s.getLogin())) {
            ETAT = 0;
            BTNNAME = "supprimer";
            findViewById(R.id.infoBtnInscription).setBackgroundColor(Color.parseColor("#b70000"));
        } else if (DaoParticipant.getInstance().isInscrit()) {
            ETAT = 1;
            BTNNAME = "Se désinscrire";
        } else {
            ETAT = 2;
            BTNNAME = "S'inscrire";
        }
        ((Button) findViewById(R.id.infoBtnInscription)).setText(BTNNAME);

//        findViewById(R.id.infoBtnInscription).setOnTouchListener((v, event) ->
//                );
        findViewById(R.id.infoBtnInscription).setOnClickListener(view ->
                Toast.makeText(getApplicationContext(), "  appuye long pour supprimer", Toast.LENGTH_SHORT).show());

        findViewById(R.id.infoBtnInscription).setOnLongClickListener(view -> {
            if (ETAT == 0) {
                String req = "requete=delSoiree&soiree=" + s.getId();
                String b = "suppression échouée";
                sendRequet(req, b, ETAT, s);
            }
            return true;
        });

        findViewById(R.id.infoBtnInscription).setOnClickListener(view -> {
            if (ETAT == 1) {
                String req = ("requete=desinscrire&soiree=" + s.getId());
                String b = "desinscription échouée";
                sendRequet(req, b, ETAT, s);
            } else if (ETAT == 2) {
                String req = ("requete=inscrire&soiree=" + s.getId());
                String b = "inscription échouée";
                sendRequet(req, b, ETAT, s);
            }
        });
    }

    private void sendRequet(String req, String b, int ETAT, Soiree s) {
        DaoParticipant.getInstance().simpleRequest(req, new Delegate() {
            @Override
            public void WSRequestIsTerminated(Object result) {
                if ((boolean) result && (ETAT != 0)) {
                    refreshList(("requete=getLesParticipants&soiree=" + s.getId()), s);
                    infoAdap.notifyDataSetChanged();
                } else if ((boolean) result) {
                    setResult(1);
                    finish();
                    Toast.makeText(getApplicationContext(), "Suppresion réusie", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), b, Toast.LENGTH_SHORT).show();
            }
        });
    }
}