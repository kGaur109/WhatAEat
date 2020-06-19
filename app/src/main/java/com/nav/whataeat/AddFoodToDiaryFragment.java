package com.nav.whataeat;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Objects;


public class AddFoodToDiaryFragment extends Fragment {
    // Class Variables
    private View mainView;
    private Cursor listCursorFood;
    private String currentFoodId;
    private String currentMealNumber;

    // Important Fragment Variables
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    // Constructor
    public AddFoodToDiaryFragment() {
        // Required empty public constructor
    }

    public static AddFoodToDiaryFragment newInstance(String param1, String param2) {
        AddFoodToDiaryFragment fragment = new AddFoodToDiaryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    // On Activity Created
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set title
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Add food to diary");

        Bundle bundle = this.getArguments();
        String stringMealName = "0";

        if(bundle != null) {
            stringMealName = bundle.getString("mealName");
        }

        // populate the list of food
        populateListWithFood("0", stringMealName);

//        initializeHome();

//        setHasOptionsMenu(true);
    }

    // Populate List with Food Items
    private void populateListWithFood(String parentID, String parentName) {

        // changing the view
        int id = R.layout.fragment_food;
        setMainView(id);

        // Database
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        String fields[] = new String[] {
                "_id",
                "food_name",
                "food_category",
                "food_calorie"
        };
        listCursorFood = db.select("food", fields, "food_name");

        // Find ListView to populate
        ListView lvFood = (ListView) getActivity().findViewById(R.id.listViewFood);

        // Setup cursor adapter
        foodCursorAdaptor foodCursorAdaptor = new foodCursorAdaptor(getActivity(), listCursorFood);

        // attach cursor adapter to the ListView
        lvFood.setAdapter(foodCursorAdaptor);

        // onClick
        lvFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                foodListItemClicked(listCursorFood.getString(1).toString());
            }
        });

//        // setting onClick to null to make it non clickable
//        lvfood.setEnabled(false);
//        lvfood.setOnItemClickListener(null);

        db.close();
    } // populateListWithFood

    // on click food list item
    private void foodListItemClicked(String foodName) {
        // changing view
        int id = R.layout.fragment_add_food_to_diary_view_food;
        setMainView(id);

        // Database open
        DBAdapter db = new DBAdapter(getActivity());
        db.open();


        String fields[] = new String[] {
                "_id",
                "food_name",
                "food_category",
                "food_calorie",
        };
        String foodNameSQL = db.quoteSmart(foodName);
        Cursor cursorFood = db.select("food", fields, "food_name", foodNameSQL);

        currentFoodId = cursorFood.getString(0).toString();

        // find fields to populate in inflated template
        TextView textViewFoodName = (TextView) getActivity().findViewById(R.id.textViewFoodName);
        TextView textViewFoodCalorie = (TextView) getActivity().findViewById(R.id.textViewFoodCalorie);
        TextView textViewFoodCategory = (TextView) getActivity().findViewById(R.id.textViewFoodCategory);

        // extract properties from the cursor
        String getName = cursorFood.getString(1);
        String getCategory = cursorFood.getString(2);
        String getCalories = cursorFood.getString(3);

        String subline = getCalories;


        // populate fields with extracted properties
        textViewFoodName.setText(getName);
        textViewFoodCalorie.setText(subline);
        textViewFoodCategory.setText(getCategory);

        if(getCategory.equals("Breakfast")) {
            currentMealNumber = "1";
        } else if(getCategory.equals("Lunch")) {
            currentMealNumber = "2";
        } else if(getCategory.equals("Snacks")) {
            currentMealNumber = "3";
        } else if(getCategory.equals("Dinner")) {
            currentMealNumber = "4";
        }

        // Serving Size
        EditText editTextServingSizeGram = (EditText)getActivity().findViewById(R.id.editTextServingSizeGram);
        editTextServingSizeGram.setText("100");

        // Listener for Add Button
        Button buttonAddFood = (Button)getActivity().findViewById(R.id.buttonAddFood);
        buttonAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFoodToDiary();
            }
        });

        // Database close
        db.close();
    }

    // Adding food to Food Diary
    private void addFoodToDiary() {
        // Database open
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        // Error
        int error = 0;

        // get in gram
        EditText editTextServingSizeGram = (EditText)getActivity().findViewById(R.id.editTextServingSizeGram);
        String stringServingSizeGram = editTextServingSizeGram.getText().toString();
        String fdServingSizeGramSQL = db.quoteSmart(stringServingSizeGram);

        double doubleServingSizeGram = 0;
        try {
            doubleServingSizeGram = Double.parseDouble(stringServingSizeGram);
        }
        catch (NumberFormatException nfe) {
            error = 1;
            Toast.makeText(getActivity(), "Please enter a number in gram", Toast.LENGTH_SHORT).show();
        }
        if(stringServingSizeGram.equals("")) {
            error = 1;
            Toast.makeText(getActivity(), "Gram cannot be an empty field", Toast.LENGTH_SHORT).show();
        }

        // Date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // month starts with 0
        month = month + 1;
        String stringMonth = "";
        if(month < 10) {
            stringMonth = "0" + month;
        }
        else {
            stringMonth = "" + month;
        }

        String stringDay = "";
        if(day < 10) {
            stringDay = "0" + day;
        }
        else {
            stringDay = "" + day;
        }

        String stringFdDate = year + "-" + stringMonth + "-" + stringDay;
        String stringFdDateSQL = db.quoteSmart(stringFdDate);

        // Food ID
        String foodIdSQL = db.quoteSmart(currentFoodId);

        // Meal Number
        String mealNumberSQL = "0";
        try {
            mealNumberSQL = db.quoteSmart(currentMealNumber);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // Energy Calculated
        TextView textViewFoodCalorie = (TextView) getActivity().findViewById(R.id.textViewFoodCalorie);
        String stringEnergy = textViewFoodCalorie.getText().toString();

        double doubleEnergyPerHundred = Double.parseDouble(stringEnergy);
        double doubleFbEnergyCalculated = doubleServingSizeGram*doubleEnergyPerHundred/100;
//        Toast.makeText(getActivity(), "Energy: " + doubleFbEnergyCalculated, Toast.LENGTH_SHORT).show();
        String stringFbEnergyCalculated = "" + doubleFbEnergyCalculated;
        String stringFbEnergyCalculatedSQL = db.quoteSmart(stringFbEnergyCalculated);

        // insert to SQL
        if(error == 0) {
            String inpFields = "_id, " +
                    "fd_date, " +
                    "fd_meal_number, " +
                    "fd_food_id, " +
                    "fd_serving_size_gram, " +
                    "fd_food_energy";

            String inpValues = "NULL, " + stringFdDateSQL + ", " +  mealNumberSQL + ", " + foodIdSQL + ", " + fdServingSizeGramSQL + ", " + stringFbEnergyCalculatedSQL;

            db.insert("food_diary", inpFields, inpValues);

            Toast.makeText(getActivity(), "Food diary updated.", Toast.LENGTH_SHORT).show();

            // Changing Fragment to HomeFragment
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = HomeFragment.class;
            try {
                fragment = (Fragment)fragmentClass.newInstance();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        }

        // Database close
        db.close();

    } // addFoodToDiary

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_add_food_to_diary, container, false);
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

    public interface OnFragmentInteractionListener {
    }
}