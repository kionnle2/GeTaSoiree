package yan.candaes.getasoiree.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import yan.candaes.getasoiree.R;
import yan.candaes.getasoiree.daos.DaoParticipant;
import yan.candaes.getasoiree.daos.Delegate;

public class MainActivity extends AppCompatActivity {
    public static final int RESULT_OK = 1;
    public final int LAUNCH_SECOND_ACTIVITY = 1;
    public final int LAUNCH_INSC_ACTIVITY = 2;

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
                    if ((boolean) result) {
                        goToSoireeActivity();
                        /* DaoParticipant.getInstance().*/
                    } else
                        Toast.makeText(getApplicationContext(), "connexion échoué", Toast.LENGTH_SHORT).show();
                }

            });
        });


        ((Button) findViewById(R.id.mainBtnInscrire)).setOnClickListener(view ->
                {
                    Intent intent = new Intent(this, InscriptionActivity.class);
                    startActivityForResult(intent, LAUNCH_INSC_ACTIVITY);
                }
        );

    }


    private void goToSoireeActivity() {
        Intent intent = new Intent(this, SoireeActivity.class);
        startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == LAUNCH_SECOND_ACTIVITY) {
                ((TextView) findViewById(R.id.mainTxtLogin)).setText("");
                ((TextView) findViewById(R.id.mainTxtPass)).setText("");
            } else if (requestCode == LAUNCH_INSC_ACTIVITY) {
                ((TextView) findViewById(R.id.mainTxtLogin)).setText(data.getStringExtra("lo"));
                ((TextView) findViewById(R.id.mainTxtPass)).setText(data.getStringExtra("md"));
            }
        }
    }
}