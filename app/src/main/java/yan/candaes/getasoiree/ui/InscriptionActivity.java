package yan.candaes.getasoiree.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import yan.candaes.getasoiree.R;
import yan.candaes.getasoiree.daos.DaoParticipant;
import yan.candaes.getasoiree.daos.Delegate;

public class InscriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        ((Button) findViewById(R.id.insBtnValid)).setOnClickListener(view -> {
            if (!((TextView) findViewById(R.id.insTxtPass1)).getText().toString()
                    .equals(((TextView) findViewById(R.id.insTxtPass2)).getText().toString())) {
                Toast.makeText(getApplicationContext(), "Mot de passe différent", Toast.LENGTH_SHORT).show();
            } else {
                String request = ("requete=creerCompte" +
                        "&login=" + ((TextView) findViewById(R.id.insTxtLogin)).getText().toString() +
                        "&nom=" + ((TextView) findViewById(R.id.insTxtNom)).getText().toString() +
                        "&prenom=" + ((TextView) findViewById(R.id.insTxtPrenom)).getText().toString() +
                        "&ddn=" + ((TextView) findViewById(R.id.insTxtDdn)).getText().toString() +
                        "&mail=" + ((TextView) findViewById(R.id.insTxtMail)).getText().toString() +
                        "&password=" + ((TextView) findViewById(R.id.insTxtPass1)).getText().toString()
                );
                DaoParticipant.getInstance().createAccount(request,new Delegate() {
                    @Override
                    public void whenWSInscriptionIsTerminated(Object result) {
                        // result boolean, reussi ou non
                        Log.d("Reussie ou non",request);
                        if((Boolean) result) Toast.makeText(getApplicationContext(), "Inscription réussie", Toast.LENGTH_SHORT).show();
                        else Toast.makeText(getApplicationContext(), "Inscription échoué", Toast.LENGTH_SHORT).show();
                    }

                });


            }
        });
        ((Button) findViewById(R.id.insBtnAnnuler)).setOnClickListener(view -> finish());

    }
}