package yan.candaes.getasoiree.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import yan.candaes.getasoiree.R;
import yan.candaes.getasoiree.daos.DaoParticipant;
import yan.candaes.getasoiree.daos.Delegate;

public class InscriptionActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    SimpleDateFormat dateLong = new SimpleDateFormat("EEEE dd MMMM yyyy");
    SimpleDateFormat dateEn = new SimpleDateFormat("yyyy-MM-dd");
TextView dd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        dd=(TextView)findViewById(R.id.insTxtDdn) ;
        dd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                yan.candaes.getasoiree.beans.DatePicker mDatePickerDialogFragment;
                mDatePickerDialogFragment = new yan.candaes.getasoiree.beans.DatePicker();
                mDatePickerDialogFragment.show(getSupportFragmentManager(), "DATE PICK");

            }
        });
        ((Button) findViewById(R.id.insBtnValid)).setOnClickListener(view -> {
            if (!((TextView) findViewById(R.id.insTxtPass1)).getText().toString()
                    .equals(((TextView) findViewById(R.id.insTxtPass2)).getText().toString())) {
                Toast.makeText(getApplicationContext(), "Mot de passe différent", Toast.LENGTH_SHORT).show();
            } else {
                String request = "requete=creerCompte" +
                        "&login=" + ((TextView) findViewById(R.id.insTxtLogin)).getText().toString() +
                        "&nom=" + ((TextView) findViewById(R.id.insTxtNom)).getText().toString() +
                        "&prenom=" + ((TextView) findViewById(R.id.insTxtPrenom)).getText().toString();


                try {
                    String ddn = ((TextView) findViewById(R.id.insTxtDdn)).getText().toString();
                    ddn = dateEn.format(dateLong.parse(ddn));
                    request += "&ddn=" + ddn;
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                request += "&mail=" + ((TextView) findViewById(R.id.insTxtMail)).getText().toString() +
                        "&password=" + ((TextView) findViewById(R.id.insTxtPass1)).getText().toString();

                DaoParticipant.getInstance().simpleRequest(request, new Delegate() {
                    @Override
                    public void WSRequestIsTerminated(Object result) {
                        if ((boolean) result) {
                            Toast.makeText(getApplicationContext(), "Inscription réussie", Toast.LENGTH_SHORT).show();
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("lo", ((TextView) findViewById(R.id.insTxtLogin)).getText().toString());
                            returnIntent.putExtra("md", ((TextView) findViewById(R.id.insTxtPass1)).getText().toString());
                            setResult(MainActivity.RESULT_OK, returnIntent);
                            finish();

                        } else
                            Toast.makeText(getApplicationContext(), "Inscription échoué", Toast.LENGTH_SHORT).show();
                    }

                });


            }
        });
        ((Button) findViewById(R.id.insBtnAnnuler)).setOnClickListener(view -> finish());

    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(mCalendar.getTime());
        Log.d("TAGTAG", DateFormat.getDateInstance(DateFormat.FULL).format(mCalendar.getTime()));
        Log.d("TAGTAG", DateFormat.getDateInstance(DateFormat.FULL).format(mCalendar.getTime().getTime()));
        dd.setText(selectedDate);
    }
}