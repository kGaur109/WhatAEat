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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;


public class CategoriesFragment extends Fragment {

    // Class Variables
    private View mainView;
    private Cursor categoriesCursor;

    // Fragment Variables required for making the fragment RUN
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public CategoriesFragment() {
        // Required empty public constructor
    }

    public static CategoriesFragment newInstance(String param1, String param2) {
        CategoriesFragment fragment = new CategoriesFragment();
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

    // On Activity Created
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toast.makeText(getActivity(), "Categories", Toast.LENGTH_SHORT).show();

        // Set title
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Categories");

        // populating the list of Categories
        populateList();

        setHasOptionsMenu(true);
    }

    // Sets main View variable to the view, so we can change view in fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_categories, container, false);
        return mainView;
    }

    // set Main View
    // changing view method in fragment
    private void setMainView(int id) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(id, null);
        ViewGroup rootView = (ViewGroup) getView();
        rootView.removeAllViews();
        rootView.addView(mainView);
    }

    // Populate List
    public void populateList() {
        // Database
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        // Get Categories
        String fields[] = new String[] {
                "_id",
                "category_name",
                "category_parent_id"
        };
        categoriesCursor = db.select("categories", fields, "category_parent_id", "0");

        // create an array
        ArrayList<String> values = new ArrayList<>();

        // convert categories to string
        int categoriesCount = categoriesCursor.getCount();
        for(int x=0; x<categoriesCount; x++) {
            values.add(categoriesCursor.getString(categoriesCursor.getColumnIndex("category_name")));

//            Toast.makeText(getActivity(),
//                    "Id: " + categoriesCursor.getString(0) + "\n" +
//                    "Name: " + categoriesCursor.getString(1), Toast.LENGTH_SHORT).show();
            categoriesCursor.moveToNext();
        }

//        categoriesCursor.close();

        // create adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()),
                android.R.layout.simple_list_item_1, values);

        // set adapter
        ListView lv = (ListView)getActivity().findViewById(R.id.listViewCategories);
        lv.setAdapter(adapter);

        // onClick
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listItemClicked(position);
            }
        });

        db.close();
    } // populateList

    // List Item Clicked
    public void listItemClicked(int listItemClickedId) {

        categoriesCursor.moveToPosition(listItemClickedId);

        // get ID and Name
        String id = categoriesCursor.getString(0);
        String name = categoriesCursor.getString(1);
        String parentID = categoriesCursor.getString(2);

        // Change Title
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle(name);

        // move to sub class
        populateListSub(id, name);
    }

    public void populateListSub(String parentID, String parentName) {
        // Database
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        // Get Food
        String[] fields = new String[] {
                "_id",
                "food_name"
        };
        final Cursor subCursor = db.select("food", fields, "food_category", parentName);

        // create an array
        ArrayList<String> values = new ArrayList<>();

        // convert categories to string
        int subCursorCount = subCursor.getCount();
        for(int x=0; x<subCursorCount; x++) {
            values.add(subCursor.getString(subCursor.getColumnIndex("food_name")));

            subCursor.moveToNext();
        }

        // create adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()),
                android.R.layout.simple_list_item_1, values);

        // set adapter
        ListView lvfood = (ListView)getActivity().findViewById(R.id.listViewCategories);
        lvfood.setAdapter(adapter);

        // setting onClick to null to make it non clickable
//        lvfood.setEnabled(false);
        lvfood.setOnItemClickListener(null);

        db.close();
    } // populateListSub


    public interface OnFragmentInteractionListener {
    }
}