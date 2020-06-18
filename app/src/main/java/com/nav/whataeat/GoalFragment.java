package com.nav.whataeat;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GoalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoalFragment extends Fragment {

    // Class Variables
    private View mainView;

    private MenuItem menuItemEdit;

    // Fragment Variables required to make the fragment run
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    // Constructor
    public GoalFragment() {
        // Required empty public constructor
    }

    public static GoalFragment newInstance(String param1, String param2) {
        GoalFragment fragment = new GoalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Set title
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Goal");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_goal, container, false);
        return mainView;
    }

    // set Main View
    private void setMainView(int id) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(id, null);
        ViewGroup rootView = (ViewGroup) getView();
        rootView.removeAllViews();
        rootView.addView(mainView);
    }

    // On Activity Created
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toast.makeText(getActivity(), "Food", Toast.LENGTH_SHORT).show();

        // Set title
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Goal");

        initializeDataFromDBAndDisplay();

        setHasOptionsMenu(true);
    }

    // creating Options action toolbar
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // Inflate Menu
        MenuInflater menuInflater = ((MainActivity)getActivity()).getMenuInflater();
        inflater.inflate(R.menu.menu_goal, menu);

        menuItemEdit = menu.findItem(R.id.goal_action_edit);

    }

    // when action icon clicked on
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        int id = menuItem.getItemId();

        if(id == R.id.goal_action_edit) {
            goalEdit();
        }

        return super.onOptionsItemSelected(menuItem);
    }

    // Get data from database and display
    private void initializeDataFromDBAndDisplay() {
        // Database open
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        long rowID = 1;
        String fieldUser[] = new String[] {
                "_id",
                "user_activity_level"
        };
        Cursor cUser = db.select("users", fieldUser, "_id", rowID);


        String fields[] = new String[] {
                "_id",
                "goal_current_weight",
                "goal_target_weight",
                "goal_i_want_to",
                "goal_weekly_goal",
                "goal_date",
                "goal_energy"
        };
        Cursor c = db.select("goal", fields, "_id");

        String goalID = c.getString(0);
        String goalCurrentWeight = c.getString(1);
        String goalTargetWeight = c.getString(2);
        String goalIWantTo = c.getString(3);
        String goalWeeklyGoal = c.getString(4);
        String goalActivityLevel = cUser.getString(1);
        String goalDate = c.getString(5);
        String goalEnergy = c.getString(6);

        // Current Weight
        TextView textViewCurrentWeight = (TextView)getActivity().findViewById(R.id.textViewCurrentWeight_1);
        textViewCurrentWeight.setText(goalCurrentWeight + " kg    (" +goalDate + ")");

        // Target Weight
        TextView textViewTargetWeight = (TextView)getActivity().findViewById(R.id.textViewTargetWeight_1);
        textViewTargetWeight.setText(goalTargetWeight + " kg    (" +goalDate + ")");

        // Method
        TextView textViewMethod = (TextView)getActivity().findViewById(R.id.textViewMethod_1);

        String method = "";
        if(goalIWantTo.equals("0")) {
            method = "Loose " + goalWeeklyGoal + " kg per week";
        } else {
            method = "Gain " + goalWeeklyGoal + " kg per week";
        }
        textViewMethod.setText(method);

        // Activity Level
        TextView textViewActivityLevel = (TextView)getActivity().findViewById(R.id.textViewActivityLevel_1);
        if(goalActivityLevel.equals("0")) {
            textViewActivityLevel.setText("Little to no exercise");
        } else if(goalActivityLevel.equals("1")) {
            textViewActivityLevel.setText("Light Exercise (1-3 days a week)");
        } else if(goalActivityLevel.equals("2")) {
            textViewActivityLevel.setText("Moderate Exercise (3-5 days a week)");
        } else if(goalActivityLevel.equals("3")) {
            textViewActivityLevel.setText("Heavy Exercise (6-7 days a week)");
        } else if(goalActivityLevel.equals("4")) {
            textViewActivityLevel.setText("Very Heavy Exercise (twice a day, heavy workouts)");
        }

        // per Day Energy
        TextView textViewEnergyGoals = (TextView)getActivity().findViewById(R.id.textViewEnergyGoals_1);
        textViewEnergyGoals.setText(goalEnergy + " calories per day.");

        // Database close
        db.close();
    }

    private void goalEdit() {
        // Change Layout
        int id = R.layout.fragment_goal_edit;
        setMainView(id);

        // Get Data From Database

        // Database open
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        long rowID = 1;
        String fieldUser[] = new String[] {
                "_id",
                "user_activity_level"
        };
        Cursor cUser = db.select("users", fieldUser, "_id", rowID);


        String fields[] = new String[] {
                "_id",
                "goal_current_weight",
                "goal_target_weight",
                "goal_i_want_to",
                "goal_weekly_goal",
                "goal_date",
                "goal_energy"
        };
        Cursor c = db.select("goal", fields, "_id");

        String goalID = c.getString(0);
        String goalCurrentWeight = c.getString(1);
        String goalTargetWeight = c.getString(2);
        String goalIWantTo = c.getString(3);
        String goalWeeklyGoal = c.getString(4);
        String goalActivityLevel = cUser.getString(1);
        String goalDate = c.getString(5);
        String goalEnergy = c.getString(6);

        // Current Weight
        EditText textViewCurrentWeight = (EditText)getActivity().findViewById(R.id.editTextEditCurrentWeight);
        textViewCurrentWeight.setText(goalCurrentWeight);

        // Target Weight
        EditText textViewTargetWeight = (EditText)getActivity().findViewById(R.id.editTextEditTargetWeight);
        textViewTargetWeight.setText(goalTargetWeight);

        // Method
        Spinner spinnerIWantTo = (Spinner)getActivity().findViewById(R.id.spinnerEditIWantTo);
        if(goalIWantTo.equals("0")) {
            spinnerIWantTo.setSelection(0);
        } else {
            spinnerIWantTo.setSelection(1);
        }

        // Weekly Goal
        Spinner spinnerWeeklyGoal = (Spinner)getActivity().findViewById(R.id.spinnerEditWeeklyGoal);
        if(spinnerWeeklyGoal.equals("0.5")) {
            spinnerWeeklyGoal.setSelection(0);
        } else if(spinnerWeeklyGoal.equals("1.0")) {
            spinnerWeeklyGoal.setSelection(1);
        } else if(spinnerWeeklyGoal.equals("1.5")) {
            spinnerWeeklyGoal.setSelection(2);
        }

        // Activity Level
        Spinner spinnerActivityLevel = (Spinner)getActivity().findViewById(R.id.spinnerEditActivityLevel);
        int intActivityLevel = 0;
        try {
            intActivityLevel = Integer.parseInt(goalActivityLevel);
        }
        catch (NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }
        spinnerActivityLevel.setSelection(intActivityLevel);

        // Save Button Listener
        Button buttonSaveGoal = (Button)getActivity().findViewById(R.id.buttonGoalSave);
        buttonSaveGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editGoalSubmit();
            }
        });

        // Database close
        db.close();
    }

    // Edit Goal Submit Button Clicked
    private void editGoalSubmit() {
        // Error
        int error = 0;

        // Get Data From Database

        // Database open
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        long rowID = 1;
        String fieldUser[] = new String[] {
                "_id",
                "user_dob",
                "user_gender",
                "user_height",
                "user_activity_level"
        };
        Cursor cUser = db.select("users", fieldUser, "_id", rowID);
        String stringUserDOB = cUser.getString(1);
        String stringUserGender = cUser.getString(2);
        String stringUserHeight = cUser.getString(3);
        String stringUserActivityLevel = cUser.getString(4);

        // Get Age
        String[] items1 = stringUserDOB.split("-");
        String stringYear = items1[0];
        String stringMonth = items1[1];
        String stringDay = items1[2];

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

        // Get Height
        double doubleUserHeight = 0;
        try {
            doubleUserHeight = Double.parseDouble(stringUserHeight);
        }
        catch (NumberFormatException nfe){
            System.out.println("Could not parse " + nfe);
        }

        // Current Weight
        EditText editTextCurrentWeight = (EditText)getActivity().findViewById(R.id.editTextEditCurrentWeight);
        String stringCurrentWeight = editTextCurrentWeight.getText().toString();

        double doubleCurrentWeight = 0;
        if(stringCurrentWeight.isEmpty()) {
            Toast.makeText(getContext(), "Please enter current weight", Toast.LENGTH_LONG).show();
            error = 1;
        }
        else {
            try {
                doubleCurrentWeight = Double.parseDouble(stringCurrentWeight);
            }
            catch (NumberFormatException nfe) {
                Toast.makeText(getContext(), "Current weight has to be a number", Toast.LENGTH_LONG).show();
                error = 1;
            }
        }
        String stringCurrentWeightSQL = db.quoteSmart(stringCurrentWeight);

        // Getting target weight
        EditText editTextTargetWeight = (EditText)getActivity().findViewById(R.id.editTextEditTargetWeight);
        String stringTargetWeight = editTextTargetWeight.getText().toString();

        double doubleTargetWeight = 0;

        if(stringTargetWeight.isEmpty()) {
            Toast.makeText(getContext(), "Please enter target weight", Toast.LENGTH_LONG).show();
            error = 1;
        }
        else {
            try {
                doubleTargetWeight = Double.parseDouble(stringTargetWeight);
            }
            catch (NumberFormatException nfe) {
                Toast.makeText(getContext(), "Target weight has to be a number", Toast.LENGTH_LONG).show();
                error = 1;
            }
        }
        String stringTargetWeightSQL = db.quoteSmart(stringTargetWeight);

        // I want to
        Spinner spinnerIWantTo = (Spinner)getActivity().findViewById(R.id.spinnerEditIWantTo);
        int intIWantTo = spinnerIWantTo.getSelectedItemPosition();
        String stringIWantTo = "" + intIWantTo;
        String stringIWantToSQL = db.quoteSmart(stringIWantTo);

        // Weekly Goal
        Spinner spinnerWeeklyGoal = (Spinner)getActivity().findViewById(R.id.spinnerEditWeeklyGoal);
        String stringWeeklyGoal = spinnerWeeklyGoal.getSelectedItem().toString();
        String stringWeeklyGoalSQL = db.quoteSmart(stringWeeklyGoal);

        // Activity Level
        Spinner spinnerActivityLevel = (Spinner)getActivity().findViewById(R.id.spinnerEditActivityLevel);
//        0: Little to no exercise
//        1: Light exercise (1–3 days a week)
//        2: Moderate exercise (3–5 days a week)
//        3: Heavy exercise (6–7 days a week)
//        4: Very heavy exercise (twice a day, extra heavy)
        int intActivityLevel = spinnerActivityLevel.getSelectedItemPosition();
        String stringActivityLevel = "" + intActivityLevel;
        String stringActivityLevelSQL = db.quoteSmart(stringActivityLevel);

        if(error == 0) {
            // Insert into Database

            DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
            String goalDate = df1.format(Calendar.getInstance().getTime());
            String goalDateSql = db.quoteSmart(goalDate);

            // BMR : Energy
            double goalEnergyBMR = 0;

            if(stringUserGender.startsWith("m")) {
                // Male
                goalEnergyBMR = 66.5 + (13.75*doubleTargetWeight) + (5.003*doubleUserHeight) - (6.755*intUserAge);

            } // Male
            else {
                // Female
                goalEnergyBMR = 55.1 + (9.563*doubleTargetWeight) + (1.803*doubleUserHeight) - (4.676*intUserAge);
            } // Female
//            bmr = Math.round(bmr);

            // Taking Activity Level into account
            if(stringActivityLevel.equals("0")) {
                goalEnergyBMR = goalEnergyBMR * 1.2;
            } else if(stringActivityLevel.equals("1")) {
                goalEnergyBMR = goalEnergyBMR * 1.375; // slightly active
            } else if(stringActivityLevel.equals("2")) {
                goalEnergyBMR = goalEnergyBMR * 1.55; // moderately active
            } else if(stringActivityLevel.equals("3")) {
                goalEnergyBMR = goalEnergyBMR * 1.725; // active lifestyle
            } else if(stringActivityLevel.equals("4")) {
                goalEnergyBMR = goalEnergyBMR * 1.9; // very active lifestyle
            }
            goalEnergyBMR = Math.round(goalEnergyBMR);
            String goalEnergyBMRSQL = db.quoteSmart("" + goalEnergyBMR);

            // Insert
            String inpFields = "'_id', " +
                    "'goal_current_weight', " +
                    "'goal_target_weight', " +
                    "'goal_i_want_to', " +
                    "'goal_weekly_goal', " +
                    "'goal_date', " +
                    "'goal_energy'";

            String inpValues = "NULL, " +
                    stringCurrentWeightSQL + ", " +
                    stringTargetWeightSQL + ", " +
                    stringIWantToSQL + ", " +
                    stringWeeklyGoalSQL + ", " +
                    goalDateSql + ", " +
                    goalEnergyBMRSQL;

            db.insert("goal", inpFields, inpValues);
            db.insert("users", "'user_activity_level'", stringActivityLevelSQL);

            Toast.makeText(getActivity(), "Changes saved.", Toast.LENGTH_SHORT).show();

            // Move User to correct design
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.flContent, new GoalFragment(), GoalFragment.class.getClass()).commit();

        } // no error

        // Database close
        db.close();
    }

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

    public interface OnFragmentInteractionListener {
    }
}