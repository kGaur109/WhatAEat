package com.nav.whataeat;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

public class foodCursorAdaptor extends CursorAdapter {

    // required constructor
    public foodCursorAdaptor(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it
    // you dont bind any data to the view at this point
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.fragment_food_list_item, parent, false);
    }

    // method for binding all data to a given view like setting the text on a TextView
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // find fields to populate in inflated template
        TextView textViewListName = (TextView) view.findViewById(R.id.textViewListName);
        TextView textViewListNumber = (TextView) view.findViewById(R.id.textViewListNumber);
        TextView textViewSubname = (TextView) view.findViewById(R.id.textViewSubname);

        // extract properties from the cursor
        String getCalories = cursor.getString(cursor.getColumnIndexOrThrow("food_calorie"));
        String getName = cursor.getString(cursor.getColumnIndexOrThrow("food_name"));
        String getCategory = cursor.getString(cursor.getColumnIndexOrThrow("food_category"));

        String subline = getCategory + ", Calories per 100gm";

        // populate fields with extracted properties
        textViewListName.setText(getName);
        textViewListNumber.setText(getCalories);
        textViewSubname.setText(subline);
    }
}
