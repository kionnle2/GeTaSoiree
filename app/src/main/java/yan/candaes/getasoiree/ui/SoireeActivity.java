package yan.candaes.getasoiree.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import yan.candaes.getasoiree.AjoutSoireeActivity;
import yan.candaes.getasoiree.R;
import yan.candaes.getasoiree.beans.Participant;
import yan.candaes.getasoiree.daos.DaoParticipant;
import yan.candaes.getasoiree.daos.Delegate;

public class SoireeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soiree);

        ArrayAdapter<Participant> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, DaoParticipant.getInstance().getLocalSoirees());
        ((ListView) (findViewById(R.id.soirLV))).setAdapter(adapter);
        String request = ("requete=getLesSoirees");

        DaoParticipant.getInstance().getSoiree(request, new Delegate() {
            @Override
            public void WSRequestIsTerminated(Object result) {
                if ((boolean) result) adapter.notifyDataSetChanged();
                else
                    Toast.makeText(getApplicationContext(), "récuperation des soirées échoué", Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.soirBtnRetour).setOnClickListener(view -> deconnexion());
        findViewById(R.id.soirBtnAdd).setOnClickListener(view -> {
            Intent intent = new Intent(this, AjoutSoireeActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        deconnexion();

    }

    public void deconnexion() {
        DaoParticipant.getInstance().simpleRequest("requete=deconnexion", new Delegate() {
            @Override
            public void WSRequestIsTerminated(Object result) {
                // result boolean, reussi ou non
                Toast.makeText(getApplicationContext(), "deconnexion", Toast.LENGTH_SHORT).show();
                finish();
            }

        });
    }
}