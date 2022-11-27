package yan.candaes.getasoiree;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import yan.candaes.getasoiree.daos.DaoParticipant;
import yan.candaes.getasoiree.daos.Delegate;

public class AjoutSoireeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_soiree);

        ((Button) findViewById(R.id.addSoirBtnAdd)).setOnClickListener(view -> {

            String request = ("requete=addSoiree" +
                    "&descriptif=FeteSansPersonne&dateDebut=2022-11-26&heureDebut=20:30:00&latitude=45.1234&longitude=2.7643" +
                    "&libelleCourt=" + ((TextView) findViewById(R.id.insTxtLogin)).getText().toString() +
                    "&descriptif=" + ((TextView) findViewById(R.id.insTxtNom)).getText().toString() +
                    "&dateDebut=" + ((TextView) findViewById(R.id.insTxtPrenom)).getText().toString() +
                    "&heureDebut=" + ((TextView) findViewById(R.id.insTxtDdn)).getText().toString() +
                    "&mail=" + ((TextView) findViewById(R.id.insTxtMail)).getText().toString() +
                    "&password=" + ((TextView) findViewById(R.id.insTxtPass1)).getText().toString()
            );
            DaoParticipant.getInstance().simpleRequest(request, new Delegate() {
                @Override
                public void WSRequestIsTerminated(Object result) {
                    if ((boolean) result) {
                        Toast.makeText(getApplicationContext(), "Inscription réussie", Toast.LENGTH_SHORT).show();
                        finish();

                    } else
                        Toast.makeText(getApplicationContext(), "Inscription échoué", Toast.LENGTH_SHORT).show();
                }

            });


        });

        findViewById(R.id.addSoirBtnRetour).setOnClickListener(view -> finish());
    }
}