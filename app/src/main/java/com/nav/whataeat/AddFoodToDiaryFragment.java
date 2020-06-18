package com.nav.whataeat;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;


public class AddFoodToDiaryFragment extends Fragment {
    // Class Variables
    private View mainView;
    private Cursor listCursorFood;

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

//        long rowID = 1;
//        String fieldUser[] = new String[] {
//                "_id",
//                "user_activity_level"
//        };
//        Cursor cUser = db.select("users", fieldUser, "_id", rowID);

//        int intFoodID = Integer.parseInt(foodID);

        String fields[] = new String[] {
                "_id",
                "food_name",
                "food_category",
                "food_calorie",
        };
        Cursor cursorFood = db.select("food", fields, "food_name", foodName);

        // find fields to populate in inflated template
        TextView textViewFoodName = (TextView) getActivity().findViewById(R.id.textViewFoodName);
        TextView textViewFoodCalorie = (TextView) getActivity().findViewById(R.id.textViewFoodCalorie);
        TextView textViewFoodCategory = (TextView) getActivity().findViewById(R.id.textViewFoodCategory);

        // extract properties from the cursor
        String getName = cursorFood.getString(1);
        String getCategory = cursorFood.getString(2);
        String getCalories = cursorFood.getString(3);

        String subline = getCalories + "  per serving/100 gms";

        // populate fields with extracted properties
        textViewFoodName.setText(getName);
        textViewFoodCalorie.setText(subline);
        textViewFoodCategory.setText(getCategory);

        // Database close
        db.close();
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