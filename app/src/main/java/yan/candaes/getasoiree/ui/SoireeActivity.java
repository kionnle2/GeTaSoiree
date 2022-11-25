package yan.candaes.getasoiree.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import yan.candaes.getasoiree.R;
import yan.candaes.getasoiree.beans.Soiree;
import yan.candaes.getasoiree.daos.DaoParticipant;
import yan.candaes.getasoiree.daos.Delegate;

public class SoireeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soiree);
/*TODO array adapter*/
      //  ArrayAdapter   adapter = new ArrayAdapter<Soiree>();
        String request = ("requete=getLesSoirees" );
        DaoParticipant.getInstance().createAccount(request, new Delegate() {
            @Override
            public void WSRequestIsTerminated(Object result) {
                if ((boolean) result) {
                    Toast.makeText(getApplicationContext(), "Inscription réussie", Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Toast.makeText(getApplicationContext(), "Inscription échoué", Toast.LENGTH_SHORT).show();
            }
        });
    }
}