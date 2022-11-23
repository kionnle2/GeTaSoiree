package yan.candaes.getasoiree.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import yan.candaes.getasoiree.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((Button) findViewById(R.id.mainBtnLogin)).setOnClickListener(view ->{

            Toast.makeText(getApplicationContext(), "Connection Reussie", Toast.LENGTH_SHORT).show();

        });


        ((Button) findViewById(R.id.mainBtnInscrire)).setOnClickListener(view ->
                {
                    Intent intent = new Intent(this, InscriptionActivity.class);
                    startActivity(intent);
                }
        );

    }
}