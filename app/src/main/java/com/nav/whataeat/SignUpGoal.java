package com.nav.whataeat;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class SignUpGoal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_goal);

        // Listener
        Button buttonSubmit = (Button)findViewById(R.id.buttonSubmit);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpGoalSubmit();
            }
        });
    }

    public void signUpGoalSubmit() {
        // Open Database
        DBAdapter db = new DBAdapter(this);
        db.open();

        // Error
        TextView textViewErrorMessage = (TextView)findViewById(R.id.textViewError);
        String errorMessage = "";

        // Getting target weight
        EditText editTextTargetWeight = (EditText)findViewById(R.id.editTextTargetWeight);
        String stringTargetWeight = editTextTargetWeight.getText().toString();

        double doubleTargetWeightKg = 0;

        try {
            doubleTargetWeightKg = Double.parseDouble(stringTargetWeight);
        }
        catch (NumberFormatException nfe) {
            errorMessage = "Target Weight has to be a number";
        }

        // Spinner I want to
        Spinner spinnerIWantTo = (Spinner)findViewById(R.id.spinnerIWantTo);
        int intIWantTo = spinnerIWantTo.getSelectedItemPosition();
        // 0 - Lose Weight
        // 1 - Gain Weight

        // Spinner Weekly Goal
        Spinner spinnerWeeklyGoal = (Spinner)findViewById(R.id.spinnerWeeklyGoal);
        String stringWeeklyGoal = spinnerWeeklyGoal.getSelectedItem().toString();
        // 0 - 0.5
        // 1 - 1.0

        // Update Database
        if(errorMessage.isEmpty()) {

            long goalId = 1;

            double targetWeightSQL = db.quoteSmart(doubleTargetWeightKg);
            db.update("goal", "_id", goalId, "goal_target_weight",targetWeightSQL);

            int intIWantToSQL = db.quoteSmart(intIWantTo);
            db.update("goal", "_id", goalId, "goal_i_want_to", intIWantToSQL);

            String weeklyGoalSQL = db.quoteSmart(stringWeeklyGoal);
            db.update("goal", "_id", goalId, "goal_weekly_goal", weeklyGoalSQL);

        }

        // Calculating
        if(errorMessage.isEmpty()) {

            long rowId = 1;
            String fields[] = new String[] {
                    "_id",
                    "user_dob",
                    "user_gender",
                    "user_height",
                    "user_activity_level"
            };
            Cursor c = db.selectPrimaryKey("users", "_id", rowId, fields);

            String userDOB = c.getString(1);
            String userGender = c.getString(2);
            String userHeight = c.getString(3);
            String userActivityLevel = c.getString(4);

            String[] items = userDOB.split("-");
            String stringYear = items[0];
            String stringMonth = items[1];
            String stringDay = items[2];

            int intYear = 0;
            try {
                intYear = Integer.parseInt(stringYear);
            }
            catch (NumberFormatException nfe){
                System.out.println("Could not parse " + nfe);
            }
            int intMonth = 0;
            try {
                intMonth = Integer.parseInt(stringMonth);
            }
            catch (NumberFormatException nfe){
                System.out.println("Could not parse " + nfe);
            }
            int intDay = 0;
            try {
                intDay = Integer.parseInt(stringDay);
            }
            catch (NumberFormatException nfe){
                System.out.println("Could not parse " + nfe);
            }

            int intUserAge = getAge(intYear, intMonth, intDay);

            double doubleUserHeight = 0;
            try {
                doubleUserHeight = Double.parseDouble(userHeight);
            }
            catch (NumberFormatException nfe){
                System.out.println("Could not parse " + nfe);
            }

            double bmr = 0;

            if(userGender.startsWith("m")) {
                // Male
                bmr = 66.5 + (13.75*doubleTargetWeightKg) + (5.003*doubleUserHeight) - (6.755*intUserAge);

            } // Male
            else {
                // Female
                bmr = 55.1 + (9.563*doubleTargetWeightKg) + (1.803*doubleUserHeight) - (4.676*intUserAge);
            } // Female
//            bmr = Math.round(bmr);

            // Taking Activity Level into account
            if(userActivityLevel.equals("0")) {
                bmr = bmr * 1.2;
            } else if(userActivityLevel.equals("1")) {
                bmr = bmr * 1.375; // slightly active
            } else if(userActivityLevel.equals("2")) {
                bmr = bmr * 1.55; // moderately active
            } else if(userActivityLevel.equals("3")) {
                bmr = bmr * 1.725; // active lifestyle
            } else if(userActivityLevel.equals("4")) {
                bmr = bmr * 1.9; // very active lifestyle
            }
            bmr = Math.round(bmr);

            // Loose or Gain Weight
            double doubleWeeklyGoal = 0;
            try {
                doubleWeeklyGoal = Double.parseDouble(stringWeeklyGoal);
            }
            catch (NumberFormatException nfe){
                System.out.println("Could not parse " + nfe);
            }

            // 1 kg fat = 7700 kcal
            double kcal = 7700*doubleWeeklyGoal;
            double bmrGoal = 0;
            if(intIWantTo == 0) {
                // Loose Weight
                bmrGoal = Math.round(bmr - (kcal/7));
            }
            else {
                // Gain Weight
                bmrGoal = Math.round(bmr + (kcal/7));
            }

            // Update Database
            double energySQL = db.quoteSmart(bmrGoal);
            long goalId = 1;
            db.update("goal", "_id", goalId, "goal_energy", energySQL);

        }

        // Error Handling
        if(!(errorMessage.isEmpty()))  {
            textViewErrorMessage.setText(errorMessage);
            textViewErrorMessage.setVisibility(View.VISIBLE);
        }

        db.close();

        // Going forward to MainActivity
        if(errorMessage.isEmpty()) {

            Intent i = new Intent(SignUpGoal.this, MainActivity.class);
            startActivity(i);
        }
    } // Sign Up Goal Submit

    // Getting Age from DOB
    private int getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if(today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age;
    }
}
