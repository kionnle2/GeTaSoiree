package yan.candaes.getasoiree.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import yan.candaes.getasoiree.R;
import yan.candaes.getasoiree.beans.Participant;
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
    }


}