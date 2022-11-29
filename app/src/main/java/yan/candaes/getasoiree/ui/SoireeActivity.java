package yan.candaes.getasoiree.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import yan.candaes.getasoiree.R;
import yan.candaes.getasoiree.beans.Participant;
import yan.candaes.getasoiree.daos.DaoParticipant;
import yan.candaes.getasoiree.daos.Delegate;

public class SoireeActivity extends AppCompatActivity {
    ArrayAdapter<Participant> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soiree);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, DaoParticipant.getInstance().getLocalSoirees());
        ((ListView) (findViewById(R.id.soirLV))).setAdapter(adapter);
        String request = ("requete=getLesSoirees");

        refreshList(request);

        findViewById(R.id.soirBtnRetour).setOnClickListener(view -> deconnexion());
        findViewById(R.id.soirBtnAdd).setOnClickListener(view -> {
            Intent intent = new Intent(this, AjoutSoireeActivity.class);
            startActivity(intent);
        });

        ((Button) findViewById(R.id.soirBtnMap)).setOnClickListener(view -> {
                    Intent intent = new Intent(this, CartesSoireesActivity.class);
                    startActivity(intent);
                }
        );
        ((ListView) findViewById(R.id.soirLV)).setOnItemClickListener((adapterView, view, i, l) -> {

        });
    }

    private void refreshList(String request) {
        DaoParticipant.getInstance().getSoiree(request, new Delegate() {
            @Override
            public void WSRequestIsTerminated(Object result) {
                if ((boolean) result) adapter.notifyDataSetChanged();
                else
                    Toast.makeText(getApplicationContext(), "récuperation des soirées échoué", Toast.LENGTH_LONG).show();
            }
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