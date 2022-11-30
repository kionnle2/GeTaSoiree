package yan.candaes.getasoiree.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import yan.candaes.getasoiree.R;
import yan.candaes.getasoiree.beans.DatePicker;
import yan.candaes.getasoiree.daos.DaoParticipant;
import yan.candaes.getasoiree.daos.Delegate;

public class AjoutSoireeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    TextView dP;
    TextView hP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_soiree);
        dP = ((TextView) findViewById(R.id.addSoirDate));
        dP.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePicker mDatePickerDialogFragment;
                mDatePickerDialogFragment = new DatePicker();
                mDatePickerDialogFragment.show(getSupportFragmentManager(), "DATE PICK");

            }
        });
        hP = ((TextView) findViewById(R.id.addSoirheure));
        hP.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(AjoutSoireeActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                hP.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, true);
                timePickerDialog.show();
            }
        });


        Switch sw = ((Switch) findViewById(R.id.addSoirSwitch));
        sw.setOnClickListener(view -> {
                    if (((TextView) findViewById(R.id.addSoirTxtAdr)).isInputMethodTarget()) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    ;

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
        SimpleDateFormat dateEn = new SimpleDateFormat("yyyy-MM-dd");
        ((Button) findViewById(R.id.addSoirBtnAdd)).setOnClickListener(view -> {
            String request = ("requete=addSoiree" +
                    "&libelleCourt=" + ((TextView) findViewById(R.id.addSoirTxtLib)).getText().toString() +
                    "&descriptif=" + ((TextView) findViewById(R.id.addSoirTxtDesc)).getText().toString() +
                    "&dateDebut=" +dateEn.format((((TextView) findViewById(R.id.addSoirTxtDate)).getText().toString())) +
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

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(mCalendar.getTime());
        dP.setText(selectedDate);
    }

}


