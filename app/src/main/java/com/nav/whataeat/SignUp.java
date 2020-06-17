package com.nav.whataeat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SignUp extends AppCompatActivity {

    private String[] arraySpinnerDOBDay = new String[31];
    private String[] arraySpinnerDOBYear = new String[101];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        int ctr = 0;
        for(int i=0; i<31; i++){
            ctr = i+1;
            this.arraySpinnerDOBDay[i] = "" + ctr;
        }

        Spinner spinnerDOBDay = (Spinner) findViewById(R.id.spinnerDay);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinnerDOBDay);
        spinnerDOBDay.setAdapter(adapter);


        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        for(int i=0; i<101; i++){
            this.arraySpinnerDOBYear[i] = "" + year;
            year--;
        }

        Spinner spinnerDOBYear = (Spinner) findViewById(R.id.spinnerYear);
        ArrayAdapter<String> adapter_1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinnerDOBYear);
        spinnerDOBYear.setAdapter(adapter_1);

//        SignUP Button Listener
        Button buttonSignUp = (Button)findViewById(R.id.buttonSignUp);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpSubmit();
            }
        });

    } // protected void onCreate

    // Sign Up Submit
    public void signUpSubmit() {
        // Error
        TextView textViewErrorMessage = (TextView)findViewById(R.id.textViewError);
        String errorMessage = "";

        // Email
        TextView textViewEmail = (TextView)findViewById(R.id.textViewEmail);

        EditText editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        String stringEmail = editTextEmail.getText().toString();
        if(stringEmail.isEmpty() || stringEmail.startsWith(" ")){
            textViewEmail.setTextColor(Color.RED);
            errorMessage = "Please fill in your Email";
        } else {
            textViewEmail.setTextColor(Color.GRAY);
        }

        // Date of Birth Date
        Spinner spinnerDay = (Spinner)findViewById(R.id.spinnerDay);
        String stringDay = spinnerDay.getSelectedItem().toString();
        int intDay = 0;
        try {
            intDay = Integer.parseInt(stringDay);
            if(intDay < 10) {
                stringDay = "0" + stringDay;
            }
        }
        catch (NumberFormatException nfe){
            System.out.println("Could not parse " + nfe);
            errorMessage = "Please select a day for your DOB";
        }

        // Date of Birth Month
        Spinner spinnerMonth = (Spinner)findViewById(R.id.spinnerMonth);
        String stringMonth = spinnerMonth.getSelectedItem().toString();

        if(stringMonth.startsWith("Jan")){
            stringMonth = "01";
        }else if(stringMonth.startsWith("Feb")){
            stringMonth = "02";
        }else if(stringMonth.startsWith("Mar")){
            stringMonth = "03";
        }else if(stringMonth.startsWith("Apr")){
            stringMonth = "04";
        }else if(stringMonth.startsWith("May")){
            stringMonth = "05";
        }else if(stringMonth.startsWith("Jun")){
            stringMonth = "06";
        }else if(stringMonth.startsWith("Jul")){
            stringMonth = "07";
        }else if(stringMonth.startsWith("Aug")){
            stringMonth = "08";
        }else if(stringMonth.startsWith("Sep")){
            stringMonth = "09";
        }else if(stringMonth.startsWith("Oct")){
            stringMonth = "10";
        }else if(stringMonth.startsWith("Nov")){
            stringMonth = "11";
        }else if(stringMonth.startsWith("Dec")){
            stringMonth = "12";
        }

        // Date of Birth Year
        Spinner spinnerYear = (Spinner)findViewById(R.id.spinnerYear);
        String stringYear = spinnerYear.getSelectedItem().toString();
        int intYear = 0;
        try {
            intYear = Integer.parseInt(stringYear);
        }
        catch (NumberFormatException nfe){
            System.out.println("Could not parse " + nfe);
            errorMessage = "Please select a year for your DOB";
        }

        // putting the DOB together
        String dateOfBirth = intYear + "-" + stringMonth + "-" + stringDay;

        // Gender
        RadioGroup radioGroupGender = (RadioGroup)findViewById(R.id.radioGroupGender);
        int selectedId = radioGroupGender.getCheckedRadioButtonId();
//        RadioButton radioButtonGender = (RadioButton)findViewById(selectedId);
//        String stringGender = radioButtonGender.getText().toString().toLowerCase();
        String stringGender = "";
        if(selectedId == 0) {
            stringGender = "male";
        } else if(selectedId == 1) {
            stringGender = "female";
        }

        // Height
        EditText editTextHeight = (EditText)findViewById(R.id.editTextHeight);
        String stringHeightCm = editTextHeight.getText().toString();

        double heightCm = 0;

        try {
            heightCm = Double.parseDouble(stringHeightCm);
        }
        catch (NumberFormatException nfe) {
            errorMessage = "Height has to be a number";
        }

        // Weight
        EditText editTextWeight = (EditText)findViewById(R.id.editTextWeight);
        String stringWeightKg = editTextWeight.getText().toString();

        double weightKg = 0;

        try {
            weightKg = Double.parseDouble(stringWeightKg);
        }
        catch (NumberFormatException nfe) {
            errorMessage = "Weight has to be a number";
        }

        // Activity Level
        Spinner spinnerActivityLevel = (Spinner)findViewById(R.id.spinnerActivity);
//        0: Little to no exercise
//        1: Light exercise (1–3 days a week)
//        2: Moderate exercise (3–5 days a week)
//        3: Heavy exercise (6–7 days a week)
//        4: Very heavy exercise (twice a day, extra heavy)
        int intActivityLevel =spinnerActivityLevel.getSelectedItemPosition();


        // Error Handling
        if(errorMessage.isEmpty()){
            textViewErrorMessage.setVisibility(View.GONE);

            // Inserting into Database
            DBAdapter db = new DBAdapter(this);
            db.open();

            // Quote Smart
            String stringEmailSQL = db.quoteSmart(stringEmail);
            String dateOfBirthSQL = db.quoteSmart(dateOfBirth);
            String stringGenderSQL = db.quoteSmart(stringGender);
            double heightCmSQL = db.quoteSmart(heightCm);
            int intActivityLevelSQL = db.quoteSmart(intActivityLevel);
            double weightKgSQL = db.quoteSmart(weightKg);

            String stringInput = "NULL, " + stringEmailSQL + "," + dateOfBirthSQL + "," + stringGenderSQL + "," + heightCmSQL + "," + intActivityLevelSQL;

            // Input for user table
            db.insert("users",
                    "user_id, user_email, user_dob, user_gender, user_height, user_activity_level",
                    stringInput);

            // Input for goal table
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String goalDate = df.format(Calendar.getInstance().getTime());

            String goalDateSQL = db.quoteSmart(goalDate);

            stringInput = "NULL, " + weightKgSQL + "," + goalDateSQL;

            db.insert("goal",
                    "goal_id, goal_current_weight, goal_date",
                    stringInput);

            Toast.makeText(this, "Signed Up Successfully", Toast.LENGTH_LONG).show();

            db.close();

            // Going forward to Goal's Page
            Intent i = new Intent(SignUp.this, SignUpGoal.class);
            startActivity(i);

        } else {
            textViewErrorMessage.setText(errorMessage);
            textViewErrorMessage.setVisibility(View.VISIBLE);
        }
    }
}// public class signUp