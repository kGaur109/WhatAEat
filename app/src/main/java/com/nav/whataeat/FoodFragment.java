package com.nav.whataeat;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

//import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FoodFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodFragment extends Fragment {

    // Class Variables
    private View mainView;
    private Cursor foodCursor;

//    // Action buttons on toolbar
//    private MenuItem menuItemEdit;
//    private MenuItem menuItemDelete;
//
//    // Holder ofr buttons on toolbar
//    private String currentId;
//    private String currentName;

    // Fragment Variables required for making the fragment RUN
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public FoodFragment() {
        // Required empty public constructor
    }

    public static FoodFragment newInstance(String param1, String param2) {
        FoodFragment fragment = new FoodFragment();
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
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Food");
    }

    // On Activity Created
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toast.makeText(getActivity(), "Food", Toast.LENGTH_SHORT).show();

        // populating the list of Categories
        populateListFood();

        setHasOptionsMenu(true);
    }

    // Sets main View variable to the view, so we can change view in fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food, container, false);
    }

    // set Main View
    private void setMainView(int id) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(id, null);
        ViewGroup rootView = (ViewGroup) getView();
        rootView.removeAllViews();
        rootView.addView(mainView);
    }

//    // creating Options action toolbar
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//
//        // Inflate Menu
//        ((MainActivity)getActivity()).getMenuInflater().inflate(R.menu.menu_food, menu);
//
//        // Assign menu items to variables
//        menuItemEdit = menu.findItem(R.id.food_action_edit);
//        menuItemDelete = menu.findItem(R.id.food_action_delete);
//
//        // Hide as default
//        menuItemEdit.setVisible(false);
//        menuItemDelete.setVisible(false);
//    }

//    // when action icon clicked on
//    @Override
//    public boolean onOptionsItemSelected(MenuItem menuItem) {
//
//        int id = menuItem.getItemId();
//
//        if(id == R.id.food_action_add) {
//            Toast.makeText(getActivity(), "Go to Add Menu" , Toast.LENGTH_SHORT).show();
////            createNewFood();
//        }
//        else if(id == R.id.food_action_edit) {
//            Toast.makeText(getActivity(), "Go to Edit Menu" , Toast.LENGTH_SHORT).show();
////            createNewFood();
//        }
//        else if(id == R.id.food_action_delete) {
//            Toast.makeText(getActivity(), "Go to Remove Menu" , Toast.LENGTH_SHORT).show();
////            createNewFood();
//        }
//
//        return super.onOptionsItemSelected(menuItem);
//    }

    // Populate List
    public void populateListFood() {
        // Database
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        String fields[] = new String[] {
                "_id",
                "food_name",
                "food_category",
                "food_serving_size"
        };
        foodCursor = db.select("food", fields, "food_name");

        // Find ListView to populate
        ListView lvItems = (ListView) getActivity().findViewById(R.id.listViewFood);

        // Setup cursor adapter
        foodCursorAdaptor foodCursorAdaptor = new foodCursorAdaptor(getActivity(), foodCursor);

        // attach cursor adapter to the ListView
        lvItems.setAdapter(foodCursorAdaptor);

        db.close();
    } // populateList

//    // List Item Clicked
//    public void listItemClicked(int listItemClickedId) {
//
//    }

    public interface OnFragmentInteractionListener {
    }
}