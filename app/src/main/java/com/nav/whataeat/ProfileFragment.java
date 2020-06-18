package com.nav.whataeat;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    // Class Variables
    private View mainView;

    // Fragment Variables required to make the fragment run
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    // Constructor
    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Profile");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_profile, container, false);
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
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Profile");

        initializeDataFromDBAndDisplay();
    }

    public void initializeDataFromDBAndDisplay() {
        // Database open
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        long rowID = 1;
        String fields[] = new String[] {
                "_id",
                "user_dob",
                "user_gender",
                "user_height"
        };
        Cursor c = db.select("users", fields, "_id", rowID);
        String stringUserDOB = c.getString(1);
        String stringUserGender = c.getString(2);
        String stringUserHeight = c.getString(3);

        // DOB
        String[] items1 = stringUserDOB.split("-");
        String stringUserDOBYear = items1[0];
        String stringUserDOBMonth = items1[1];
        String stringUserDOBDay = items1[2];

        // filling the numbers in the spinners
        int spinnerDOBDaySelectedIndex = 0;
        String[] arraySpinnerDOBDay = new String[31];
        int ctr = 0;
        for(int i=0; i<31; i++){
            ctr = i+1;
            arraySpinnerDOBDay[i] = "" + ctr;

            if(stringUserDOBDay.equals("0" + ctr) || stringUserDOBDay.equals("" + ctr)){
                spinnerDOBDaySelectedIndex = i;
            }
        }


        Spinner spinnerDOBDay = (Spinner) getActivity().findViewById(R.id.spinnerEditDay);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, arraySpinnerDOBDay);
        spinnerDOBDay.setAdapter(adapter);
        spinnerDOBDay.setSelection(spinnerDOBDaySelectedIndex);

        int intUserDOBMonth = 0;
        stringUserDOBDay.replace("0", "");
        try {
            intUserDOBMonth = Integer.parseInt(stringUserDOBMonth);
        }
        catch (NumberFormatException nfe){
            System.out.println("Could not parse " + nfe);
        }
        intUserDOBMonth = intUserDOBMonth-1;
        Spinner spinnerDOBMonth = (Spinner) getActivity().findViewById(R.id.spinnerEditMonth);
        spinnerDOBMonth.setSelection(intUserDOBMonth);

        int spinnerDOBYearSelectedIndex = 0;
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        String[] arraySpinnerDOBYear = new String[100];
        int end = year - 100;
        int index = 0;

        for(int x=year; x>end; x--){
            arraySpinnerDOBYear[index] = "" + x;

            if(stringUserDOBYear.equals("" + year)) {
                spinnerDOBYearSelectedIndex = index;
            }
            index++;
        }

        Spinner spinnerDOBYear = (Spinner) getActivity().findViewById(R.id.spinnerEditYear);
        ArrayAdapter<String> adapter_1 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, arraySpinnerDOBYear);
        spinnerDOBYear.setAdapter(adapter_1);
        spinnerDOBYear.setSelection(spinnerDOBYearSelectedIndex);

        // Gender
        RadioButton radioButtonGenderMale = (RadioButton)getActivity().findViewById(R.id.radioButtonEditMale);
        RadioButton radioButtonGenderFemale = (RadioButton)getActivity().findViewById(R.id.radioButtonEditFemale);
        if(stringUserGender.startsWith("m")) {
            radioButtonGenderMale.setChecked(true);
            radioButtonGenderFemale.setChecked(false);
        } else {
            radioButtonGenderMale.setChecked(false);
            radioButtonGenderFemale.setChecked(true);
        }

        // Height
        EditText editTextEditHeight = (EditText)getActivity().findViewById(R.id.editTextEditHeight);
        editTextEditHeight.setText(stringUserHeight);

        Button buttonEditProfileSave = (Button)getActivity().findViewById(R.id.buttonProfileSave);
        buttonEditProfileSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfileSubmit();
            }
        });

        // Database close
        db.close();
    }

    public void editProfileSubmit() {
        // Database open
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        // Error
        int error = 0;

        // Date of Birth Date
        Spinner spinnerDay = (Spinner)getActivity().findViewById(R.id.spinnerEditDay);
        String stringDOBDay = spinnerDay.getSelectedItem().toString();
        int intDay = 0;
        try {
            intDay = Integer.parseInt(stringDOBDay);
            if(intDay < 10) {
                stringDOBDay = "0" + stringDOBDay;
            }
        }
        catch (NumberFormatException nfe){
            System.out.println("Could not parse " + nfe);
            error = 1;
            Toast.makeText(getActivity(), "Please select a day for your birthday", Toast.LENGTH_SHORT).show();
        }

        // Date of Birth Month
        Spinner spinnerMonth = (Spinner)getActivity().findViewById(R.id.spinnerEditMonth);
        String stringDOBMonth = spinnerMonth.getSelectedItem().toString();
        int positionDOBMonth = spinnerMonth.getSelectedItemPosition();
        int month = positionDOBMonth + 1;
        if(month < 10) {
            stringDOBMonth = "0" + month;
        } else {
            stringDOBMonth = "" + month;
        }

        // Date of Birth Year
        Spinner spinnerYear = (Spinner)getActivity().findViewById(R.id.spinnerEditYear);
        String stringYear = spinnerYear.getSelectedItem().toString();
        int intDOBYear = 0;
        try {
            intDOBYear = Integer.parseInt(stringYear);
        }
        catch (NumberFormatException nfe){
            System.out.println("Could not parse " + nfe);
            error = 1;
            Toast.makeText(getActivity(), "Please select a year for your birthday", Toast.LENGTH_SHORT).show();
        }

        // putting the DOB together
        String dateOfBirth = intDOBYear + "-" + stringDOBMonth + "-" + stringDOBDay;
        String dateOfBirthSQL = db.quoteSmart(dateOfBirth);

        // Gender
        RadioGroup radioGroupEditGender = (RadioGroup)getActivity().findViewById(R.id.radioGroupEditGender);
        int radioButtonId = radioGroupEditGender.getCheckedRadioButtonId();
        View radioButtonGender = radioGroupEditGender.findViewById(radioButtonId);
        int position = radioGroupEditGender.indexOfChild(radioButtonGender);

        String stringGender = "";
        if(radioButtonId == 0) {
            stringGender = "male";
        } else if(radioButtonId == 1) {
            stringGender = "female";
        }
        String stringGenderSQL = db.quoteSmart(stringGender);

        // Height
        EditText editTextHeight = (EditText)getActivity().findViewById(R.id.editTextEditHeight);
        String stringHeightCm = editTextHeight.getText().toString();

        double doubleHeight = 0;

        try {
            doubleHeight = Double.parseDouble(stringHeightCm);
            stringHeightCm = "" + doubleHeight;
        }
        catch (NumberFormatException nfe) {
            error = 1;
            Toast.makeText(getActivity(), "Height has to be number", Toast.LENGTH_SHORT).show();
        }
        String stringHeightSQL = db.quoteSmart(stringHeightCm);

        if(error == 0) {
            long id = 1;
            String fields[] = new String[] {
                    "user_dob",
                    "user_gender",
                    "user_height"
            };
            String values[] = new String[] {
                    dateOfBirthSQL,
                    stringGenderSQL,
                    stringHeightSQL,
            };

            db.update("users", "_id", id, fields, values);

            Toast.makeText(getActivity(), "Changes saved.", Toast.LENGTH_LONG).show();
        }

        // Database close
        db.close();
    }

    public interface OnFragmentInteractionListener {
    }
}