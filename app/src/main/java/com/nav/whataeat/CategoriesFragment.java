package com.nav.whataeat;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoriesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Variables
    Cursor categoriesCursor;

    public CategoriesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoriesFragment.
     */
    // TODO: Rename and change types and number of parameters
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

        // Set title
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Categories");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toast.makeText(getActivity(), "Categories", Toast.LENGTH_SHORT).show();

        // populating the list of Categories
        populateList();
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

        // Get Categories
        String[] fields = new String[] {
                "_id",
                "food_name"
        };
        Cursor subCursor = db.select("food", fields, "food_category", parentName);

        // create an array
        ArrayList<String> values = new ArrayList<>();

        // convert categories to string
        int subCursorCountCount = subCursor.getCount();
        for(int x=0; x<subCursorCountCount; x++) {
            values.add(subCursor.getString(subCursor.getColumnIndex("food_name")));

            subCursor.moveToNext();
        }


        // create adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()),
                android.R.layout.simple_list_item_1, values);

        // set adapter
        ListView lv = (ListView)getActivity().findViewById(R.id.listViewCategories);
        lv.setAdapter(adapter);

        // onClick
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                listItemClicked(position);
//            }
//        });

        db.close();
    } // populateListSub

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    public interface OnFragmentInteractionListener {
    }
}