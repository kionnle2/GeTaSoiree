package yan.candaes.getasoiree.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import yan.candaes.getasoiree.R;

import yan.candaes.getasoiree.daos.DaoParticipant;
import yan.candaes.getasoiree.daos.Delegate;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((Button) findViewById(R.id.mainBtnLogin)).setOnClickListener(view -> {

            String request = ("requete=connexion" +
                    "&login=" + ((TextView) findViewById(R.id.mainTxtLogin)).getText().toString() +
                    "&password=" + ((TextView) findViewById(R.id.mainTxtPass)).getText().toString());

            DaoParticipant.getInstance().connexionAccount(request, new Delegate() {
                @Override
                public void WSRequestIsTerminated(Object result) {
                    // result boolean, reussi ou non
                    if ((boolean) result){
                        goToSoireeActivity();
                   /* DaoParticipant.getInstance().*/}
                    else
                        Toast.makeText(getApplicationContext(), "connexion échoué", Toast.LENGTH_SHORT).show();
                }

            });
        });


        ((Button) findViewById(R.id.mainBtnInscrire)).setOnClickListener(view ->
                {
                    Intent intent = new Intent(this, InscriptionActivity.class);
                    startActivity(intent);
                }
        );

    }



    private void goToSoireeActivity() {
        Intent intent = new Intent(this, SoireeActivity.class);
        startActivity(intent);
    }


}
