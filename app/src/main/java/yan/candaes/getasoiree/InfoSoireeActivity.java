package yan.candaes.getasoiree;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
        Soiree s = DaoParticipant.getInstance().getLocalSoirees().get(getIntent().getIntExtra("position", -1));
        ((TextView) findViewById(R.id.infoLib)).setText(s.getLibelleCourt());
        ((TextView) findViewById(R.id.infoDesc)).setText(s.getDescriptif());
        String date[] = s.getDateDebut().split("-");
        ((TextView) findViewById(R.id.infoDate)).setText("Date : " + date[2] + "-" + date[1] + "-" + date[0]);
        String heure[] = s.getHeureDebut().split(":");
        ((TextView) findViewById(R.id.infoHeure)).setText("Heure : " + heure[0] + ":" + heure[1]);
        ((TextView) findViewById(R.id.infoLog)).setText("Soirée déposée  par " + s.getLogin());


        infoAdap = new ArrayAdapter(this, android.R.layout.simple_list_item_1, DaoParticipant.getInstance().getLocalParticipants());
        ((ListView) (findViewById(R.id.infoLvParticipant))).setAdapter(infoAdap);
        String request = ("requete=getLesParticipants&soiree=" + s.getId());
        refreshList(request, s);

        ((Button) findViewById(R.id.infoBtnBack)).setOnClickListener(View -> {
            finish();
        });

    }


    private void refreshList(String request, Soiree s) {
        DaoParticipant.getInstance().getLesParticipants(request, new Delegate() {
            @Override
            public void WSRequestIsTerminated(Object result) {
                if ((boolean) result) {
                    infoAdap.notifyDataSetChanged();
                    setupButton(s);
                } else
                    Toast.makeText(getApplicationContext(), "la récuperation des particiants a échouée", Toast.LENGTH_LONG).show();
            }
        });
    }
/*TODO  3 state Button*/
    private void setupButton(Soiree s) {
        //Si créateur propose de suppr sinon si inscrit propose de desinscrire et inversement
        int ETAT;
        String BTNNAME;
        if (DaoParticipant.getInstance().getLogParticipant().getLogin().equalsIgnoreCase(s.getLogin())) {
            ETAT = 0;
            BTNNAME = "supprimer";
        } else if (DaoParticipant.getInstance().isInscrit()) {
            ETAT = 1;
            BTNNAME = "Se désinscrire";
        } else {
            ETAT = 2;
            BTNNAME = "S'inscrire";
        }
        ((Button) findViewById(R.id.infoBtnInscription)).setText(BTNNAME);

////////////////////////////////////////////////////////////////////////////////////////////////////
        ((Button) findViewById(R.id.infoBtnInscription)).setOnClickListener(view -> {
            String req = "requete=";
            if (ETAT == 0) {
                req += ("delSoiree" + "&soiree=" + s.getId());
                String b = "suppression échouée";
            } else if (ETAT == 1) {
                req += ("desinscrire" + "&soiree=" + s.getId());
                String b = "desinscription échouée";
            } else {
                req = ("inscrire" + "&soiree=" + s.getId());
                String b = "inscription échouée";
            }
            DaoParticipant.getInstance().simpleRequest(req, new Delegate() {
                @Override
                public void WSRequestIsTerminated(Object result) {
                    if ((boolean) result) {
                        if (((Button) findViewById(R.id.infoBtnInscription)).getText().toString().equalsIgnoreCase("S'inscrire"))
                            ((Button) findViewById(R.id.infoBtnInscription)).setText("Se désinscrire");
                        else
                            ((Button) findViewById(R.id.infoBtnInscription)).setText("S'inscrire");
                    } else
                        Toast.makeText(getApplicationContext(), "Desinscription échoué", Toast.LENGTH_SHORT).show();
                }
            });
            ((Button) findViewById(R.id.infoBtnInscription)).setText("S'inscrire");
        });
    }

}