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
        updateTable(stringDate, "0");

        // ImageButton Listener
        ImageView imageViewAddBreakfast = (ImageView)getActivity().findViewById(R.id.imageViewAddBreakfast);
        imageViewAddBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFood(0, "Breakfast"); // 0 == Breakfast
            }
        });

    } // initializeHome

    // Update Table
    private void updateTable(String stringDate, String mealNumber){

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

//            // Update table
//            TextView textViewBreakfastItemName = (TextView) getActivity().findViewById(R.id.textViewBreakfastItemName);
//            textViewBreakfastItemName.setText(foodName);
//
//            TextView textViewBreakfastItemEnergy = (TextView) getActivity().findViewById(R.id.textViewBreakfastItemEnergy);
//            textViewBreakfastItemEnergy.setText(foodEnergy);

            cursorFd.moveToNext();
        }

        // Database close
        db.close();

    }// updateTable

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
        bundle.putString("mealName", parentName);
        fragment.setArguments(bundle);

        // Move user to correct layout
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

    } // addFood

    public interface OnFragmentInteractionListener {
    }
}