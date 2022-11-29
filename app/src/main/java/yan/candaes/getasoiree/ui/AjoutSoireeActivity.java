package yan.candaes.getasoiree.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import yan.candaes.getasoiree.R;
import yan.candaes.getasoiree.daos.DaoParticipant;
import yan.candaes.getasoiree.daos.Delegate;

public class AjoutSoireeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_soiree);
        Switch sw = ((Switch) findViewById(R.id.addSoirSwitch));
        sw.setOnClickListener(view -> {
            if (((TextView)findViewById(R.id.addSoirTxtAdr)).isInputMethodTarget()){
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            };

                    if (sw.isChecked()) {
                        findViewById(R.id.addSoirTxtAdr).setVisibility(View.INVISIBLE);
                        findViewById(R.id.addSoirLon).setVisibility(View.VISIBLE);
                        findViewById(R.id.addSoirLat).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.addSoirTxtAdr).setVisibility(View.VISIBLE);
                        findViewById(R.id.addSoirLon).setVisibility(View.INVISIBLE);
                        findViewById(R.id.addSoirLat).setVisibility(View.INVISIBLE
                        );
                    }
                }
        );

        ((Button) findViewById(R.id.addSoirBtnAdd)).setOnClickListener(view -> {
            String request = ("requete=addSoiree" +
                    "&libelleCourt=" + ((TextView) findViewById(R.id.addSoirTxtLib)).getText().toString() +
                    "&descriptif=" + ((TextView) findViewById(R.id.addSoirTxtDesc)).getText().toString() +
                    "&dateDebut=" + ((TextView) findViewById(R.id.addSoirTxtDate)).getText().toString() +
                    "&heureDebut=" + ((TextView) findViewById(R.id.addSoirTxtHeure)).getText().toString());
            if (sw.isChecked())
                request += "&latitude=" + ((TextView) findViewById(R.id.addSoirLat)).getText().toString() +
                        "&longitude=" + ((TextView) findViewById(R.id.addSoirLon)).getText().toString();
            else
                request += "&adresse=" + ((TextView) findViewById(R.id.addSoirBtnAdd)).getText().toString();
            DaoParticipant.getInstance().simpleRequest(request, new Delegate() {
                @Override
                public void WSRequestIsTerminated(Object result) {
                    if ((boolean) result) {
                        Toast.makeText(getApplicationContext(), "ajout réussie", Toast.LENGTH_SHORT).show();
                        setResult(1);
                        finish();

                    } else
                        Toast.makeText(getApplicationContext(), "ajout échoué", Toast.LENGTH_SHORT).show();
                }

            });


        });

        findViewById(R.id.addSoirBtnRetour).setOnClickListener(view -> finish());
    }
}