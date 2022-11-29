package yan.candaes.getasoiree;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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
        Soiree s = DaoParticipant.getInstance().getLocalSoirees().get(
                getIntent().getIntExtra("position", -1));
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
        refreshList(request);

        ((Button) findViewById(R.id.infoBtnBack)).setOnClickListener(View -> {


            finish();
        });

    }

    private void refreshList(String request) {
        DaoParticipant.getInstance().getLesParticipants(request, new Delegate() {
            @Override
            public void WSRequestIsTerminated(Object result) {
                if ((boolean) result) infoAdap.notifyDataSetChanged();
                else
                    Toast.makeText(getApplicationContext(), "la récuperation des particiants a échouée", Toast.LENGTH_LONG).show();
            }
        });
    }

}