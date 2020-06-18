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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;


public class HomeFragment extends Fragment {

    // Class Variables
    private View mainView;
    private Cursor listCursor;

    private MenuItem menuItemEdit;

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
        Toast.makeText(getActivity(), "Home", Toast.LENGTH_SHORT).show();

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

        // ImageButton Listener
        ImageView imageViewAddBreakfast = (ImageView)getActivity().findViewById(R.id.imageViewAddBreakfast);
        imageViewAddBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFood(0, "Breakfast"); // 0 == Breakfast
            }
        });
    } // initializeHome

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