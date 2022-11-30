package yan.candaes.getasoiree.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

        findViewById(R.id.soirBtnDel).setBackgroundColor(Color.parseColor("#b70000"));
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, DaoParticipant.getInstance().getLocalSoirees());
        ((ListView) (findViewById(R.id.soirLV))).setAdapter(adapter);

        refreshList("requete=getLesSoirees");

        findViewById(R.id.soirBtnRetour).setOnClickListener(view -> deconnexion());
        findViewById(R.id.soirBtnAdd).setOnClickListener(view -> {
            Intent intent = new Intent(this, AjoutSoireeActivity.class);
            startActivityForResult(intent, 1
            );
        });
        ((Button) findViewById(R.id.soirBtnDel)).setOnClickListener(v ->
                Toast.makeText(getApplicationContext(), "Appuye long pour suprimmer son compte", Toast.LENGTH_LONG).show());
        ((Button) findViewById(R.id.soirBtnDel)).setOnLongClickListener(v ->
        {
            DaoParticipant.getInstance().simpleRequest("requete=supprimerCompte", new Delegate() {
                @Override
                public void WSRequestIsTerminated(Object result) {
                    if ((boolean) result) {
                        Intent returnIntent = new Intent();
                        setResult(MainActivity.RESULT_OK, returnIntent);
                        finish();
                    } else
                        Toast.makeText(getApplicationContext(), "suppréssion du compte échoué", Toast.LENGTH_LONG).show();
                }
            });
            return true;
        });
        ((Button) findViewById(R.id.soirBtnMap)).setOnClickListener(view -> {
                    Intent intent = new Intent(this, CartesSoireesActivity.class);
                    startActivity(intent);
                }
        );
        ((ListView) findViewById(R.id.soirLV)).setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(this, InfoSoireeActivity.class);
            intent.putExtra("position", i);
            startActivityForResult(intent, 1);
        });
    }

    private void refreshList(String request) {
        DaoParticipant.getInstance().getLesSoirees(request, new Delegate() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refreshList("requete=getLesSoirees");

    }
}