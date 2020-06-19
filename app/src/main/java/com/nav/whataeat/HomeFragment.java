package com.nav.whataeat;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;


public class HomeFragment extends Fragment {

    // Class Variables
    private View mainView;
    private Cursor listCursor;

    private MenuItem menuItemEdit;

    // Holding Variables
    private String currentDateYear = "";
    private String currentDateMonth = "";
    private String currentDateDay = "";

    // Fragment Variables required to make the fragment run
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    // Constructor
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_home, container, false);
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

        // Set title
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Home");

        initializeHome();

//        setHasOptionsMenu(true);
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

//        if(id == R.id.goal_action_edit) {
//
//        }

        return super.onOptionsItemSelected(menuItem);
    }

    // Initializing Home
    private void initializeHome() {

        // Find Date
        if(currentDateYear.equals("") || currentDateMonth.equals("") || currentDateDay.equals("")) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            month += 1; // month starts with 0

            if(month < 10) {
                currentDateMonth = "0" + month;
            }
            else {
                currentDateMonth = "" + month;
            }

            if(day < 10) {
                currentDateDay = "0" + day;
            }
            else {
                currentDateDay = "" + day;
            }
            currentDateYear = "" + year;
        }

        String stringDate = currentDateYear + "-" + currentDateMonth + "-" + currentDateDay;

        // Fill table
        updateTableItems(stringDate, "0");

        // ImageButton Listener BreakFast
        ImageView imageViewAddBreakfast = (ImageView)getActivity().findViewById(R.id.imageViewAddBreakfast);
        imageViewAddBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFood(0, "Breakfast"); // 0 == Breakfast
            }
        });
        // TextView Listener BreakFast
        TextView textViewHeadlineBreakfast = (TextView)getActivity().findViewById(R.id.textViewHeadlineBreakfast);
        textViewHeadlineBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFood(0, "Breakfast"); // 0 == Breakfast
            }
        });

//        // ImageButton Listener Lunch
//        ImageView imageViewAddLunch = (ImageView)getActivity().findViewById(R.id.imageViewAddLunch);
//        imageViewAddBreakfast.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addFood(1, "Lunch"); // 1 == Lunch
//            }
//        });
//        // TextView Listener Lunch
//        TextView textViewHeadlineLunch = (TextView)getActivity().findViewById(R.id.textViewHeadlineLunch);
//        textViewHeadlineLunch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addFood(0, "Lunch"); // 1 == Lunch
//            }
//        });
//
//        // ImageButton Listener Snacks
//        ImageView imageViewAddSnacks = (ImageView)getActivity().findViewById(R.id.imageViewAddSnacks);
//        imageViewAddBreakfast.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addFood(2, "Snacks"); // 2 == Snacks
//            }
//        });
//        // TextView Listener Snacks
//        TextView textViewHeadlineSnacks = (TextView)getActivity().findViewById(R.id.textViewHeadlineSnacks);
//        textViewHeadlineSnacks.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addFood(2, "Snacks"); // 2 == Snacks
//            }
//        });
//
//        // ImageButton Listener Dinner
//        ImageView imageViewAddDinner = (ImageView)getActivity().findViewById(R.id.imageViewAddDinner);
//        imageViewAddBreakfast.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addFood(3, "Dinner"); // 3 == Dinner
//            }
//        });
//        // TextView Listener Dinner
//        TextView textViewHeadlineDinner = (TextView)getActivity().findViewById(R.id.textViewHeadlineDinner);
//        textViewHeadlineDinner.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addFood(3, "Dinner"); // 3 == Dinner
//            }
//        });

    } // initializeHome

    // Update Table Items
    private void updateTableItems(String stringDate, String stringMealNumber){

        // Database open
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        // Select for food Diary
        String[] fields = new String[] {
                "_id",
                "fd_food_id",
                "fd_date",
                "fd_meal_number",
                "fd_serving_size_gram",
                "fd_food_energy"
        };

        String stringDateSQL = db.quoteSmart(stringDate);
        String stringMealNumberSQL = db.quoteSmart(stringMealNumber);

        Cursor cursorFd = db.select("food_diary", fields, "fd_date", stringDateSQL);

        // Select for Food
        String[] fieldsFood = new String[] {
                "_id",
                "food_name",
                "food_calorie",
                "food_serving_size",
                "food_category",
        };

        Cursor cursorFood;

        // Select for Fdce
        Cursor cursorFdce;
        String[] fieldsFdce = new String[] {
                "_id",
                "fdce_id",
                "fdce_meal_no",
                "fdce_energy",
                "fdce_date"
        };

        String[] whereClause = new String[] {
                "fdce_date",
                "fdce_meal_no"
        };
        String[] whereCondition = new String[] {
                stringDateSQL,
                stringMealNumberSQL
        };
        String[] whereAndOr = new String[] {
                "AND"
        };

//        cursorFdce = db.select("food_diary_cal_eaten", fieldsFdce, "fdce_date", stringDateSQL);
        cursorFdce = db.select("food_diary_cal_eaten", fieldsFdce, whereClause, whereCondition, whereAndOr);

        int cursorFdceCount = cursorFdce.getCount();

        int errorFdce = 0;
        if(cursorFdceCount == 0) {
            String inpFieldsFdce = "_id, fdce_date, fdce_meal_no, fdce_energy";
            String inpValuesFdce = "NULL, " + stringDateSQL + ", " + stringMealNumberSQL + ", '0'";

            db.insert("food_diary_cal_eaten", inpFieldsFdce, inpValuesFdce);

            cursorFdce = db.select("food_diary_cal_eaten", fieldsFdce, whereClause, whereCondition, whereAndOr);
        }
        String stringFdceId = cursorFdce.getString(0);
        long longFdceId = Long.parseLong(stringFdceId);

        // Ready variables for sum
        int intFdceEnergy = 0;

        int intCursorFdCount = cursorFd.getCount();

        // Looping through the cursor
        for(int x=0; x<intCursorFdCount; x++ ){
//            String stringFdId = cursorFd.getString(0);
//            Toast.makeText(getActivity(), "ID: " + stringFdId, Toast.LENGTH_SHORT).show();

            // Values from Food Diary
            String fdFoodId = cursorFd.getString(1);
            String stringFdIdSQL = db.quoteSmart(fdFoodId);

            cursorFood = db.select("food", fieldsFood, "_id", stringFdIdSQL);

            // Values from Food table
            String foodName = cursorFood.getString(1);
            // energy of food from food diary
            String foodEnergy = cursorFd.getString(5);

            int intFoodEnergy = 0;
            try {
                intFoodEnergy = Integer.parseInt(foodEnergy);
            }
            catch (NumberFormatException nfe) {
                System.out.println("Could not parse " + nfe);
            }

            // Add table Rows
            TableLayout tableLayout = (TableLayout)getActivity().findViewById(R.id.tableLayoutBreakfastItems);
            TableRow tableRow = new TableRow(getActivity());
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            // Table Row: TextView Name
            TextView textViewItemName = new TextView(getActivity());
            textViewItemName.setText(foodName);
            tableRow.addView(textViewItemName); // adding view to row

            // Table Row: TextView Energy
            TextView textViewItemEnergy = new TextView(getActivity());
            textViewItemEnergy.setText(foodEnergy);
            tableRow.addView(textViewItemEnergy); // adding view to row

            // add row to table layout
            tableLayout.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

            // Sum field
            intFdceEnergy = intFdceEnergy + intFoodEnergy;

            cursorFd.moveToNext();
        }

        // Update  Fdce
        TextView textViewEnergyBreakfast = (TextView)getActivity().findViewById(R.id.textViewEnergyBreakfast);
        textViewEnergyBreakfast.setText("" + intFdceEnergy);

        String inpValue = "'" + intFdceEnergy + "'";

        db.update("food_diary_cal_eaten", "_id", longFdceId, "fdce_energy", inpValue);

        // Database close
        db.close();

    }// updateTableItems


    // Adding Food
    private void addFood(int mealNumber, String parentName) {

        Fragment fragment = null;
        Class fragmentClass = null;
        fragmentClass = AddFoodToDiaryFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Send Variable
        Bundle bundle = new Bundle();
        bundle.putString("mealNumber", ""+mealNumber);
        fragment.setArguments(bundle);

        // Move user to correct layout
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

    } // addFood

    public interface OnFragmentInteractionListener {
    }
}