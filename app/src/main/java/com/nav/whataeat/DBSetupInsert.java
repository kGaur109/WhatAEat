package com.nav.whataeat;

import android.content.Context;

public class DBSetupInsert {

    // Variables
    private final Context context;

    // Public Class
    public DBSetupInsert(Context ctx){
        this.context = ctx;
    }

    // Setup to insert to categories
    // to insert a category into table
    public void setupInsertToCategory(String values) {
        DBAdapter db = new DBAdapter(context);
        db.open();
        db.insert("categories",
                "category_id, category_name, category_parent_id",
                values);
        db.close();
    }

    public void insertAllCategories() {
        setupInsertToCategory("NULL, 'Breakfast', '0'");
        setupInsertToCategory("NULL, 'Lunch', '1'");
        setupInsertToCategory("NULL, 'Snacks', '2'");
        setupInsertToCategory("NULL, 'Dinner', '3'");
    }


    // Setup to Insert food into food table
    public void setupInsertToFood(String values) {
        DBAdapter db = new DBAdapter(context);
        db.open();
        db.insert("food",
                "food_id, food_name, food_serving_size, food_user_id, food_category", values);
        db.close();
    }

    // Insert All Food into food database
    public void insertAllFood() {
        setupInsertToFood("NULL, 'Rajma Masala', '207', NULL, 'Dinner'");
        setupInsertToFood("NULL, 'Rajma Chawal', '320', NULL, 'Lunch'");
        setupInsertToFood("NULL, 'Roti', '297', NULL, 'Dinner'");
        setupInsertToFood("NULL, 'Green Salad', '50', NULL, 'Lunch'");
        setupInsertToFood("NULL, 'Raita', '25', NULL, 'Dinner'");
        setupInsertToFood("NULL, 'Pav Bhaji', '136.0', NULL, 'Lunch'");
        setupInsertToFood("NULL, 'Samosa', '262', NULL, 'Misc.'");
        setupInsertToFood("NULL, 'Arhar Dal', '343', NULL, 'Dinner'");
        setupInsertToFood("NULL, 'Sooji Halwa', '285', NULL, 'Dinner'");
        setupInsertToFood("NULL, 'Gulab  Jamun', '380', NULL, 'Dinner'");
        setupInsertToFood("NULL, 'Bhindi Masala', '83', NULL, 'Lunch'");
        setupInsertToFood("NULL, 'Matar Mushroom', '106', NULL, 'Dinner'");
        setupInsertToFood("NULL, 'Aalu Capsicum', '133', NULL, 'Breakfast'");
        setupInsertToFood("NULL, 'Chana Dal', '164', NULL, 'Lunch'");
        setupInsertToFood("NULL, 'Paneer Makhani', '456', NULL, 'Dinner'");
        setupInsertToFood("NULL, 'Chole', '223', NULL, 'Lunch'");
        setupInsertToFood("NULL, 'Poha', '250', NULL, 'Breakfast'");
        setupInsertToFood("NULL, 'Coffee', '136', NULL, 'Breakfast'");
        setupInsertToFood("NULL, 'Tea', '40', NULL, 'Breakfast'");
        setupInsertToFood("NULL, 'Banana', '89', NULL, 'Breakfast'");
        setupInsertToFood("NULL, 'Boiled Egg', '155', NULL, 'Breakfast'");
        setupInsertToFood("NULL, 'Veg Korma', '120', NULL, 'Lunch'");
        setupInsertToFood("NULL, 'Corn Sandwich', '133', NULL, 'Breakfast'");
        setupInsertToFood("NULL, 'Veg Pakoras', '315', NULL, 'Lunch'");
        setupInsertToFood("NULL, 'Aloo Kathi Roll', '460', NULL, 'Misc.'");
        setupInsertToFood("NULL, 'Paneer Roll', '197', NULL, 'Misc.'");
        setupInsertToFood("NULL, 'Egg Roll', '196', NULL, 'Misc.'");
        setupInsertToFood("NULL, 'Paneer Egg Roll', '650', NULL, 'Misc.'");
        setupInsertToFood("NULL, 'Makhani Roll', '420', NULL, 'Misc.'");
        setupInsertToFood("NULL, 'Double Egg Roll', '315', NULL, 'Misc.'");
        setupInsertToFood("NULL, 'Chocolate Donut', '340', NULL, 'Misc.'");
        setupInsertToFood("NULL, 'Chole Bhature', '427', NULL, 'Misc.'");
        setupInsertToFood("NULL, 'Aloo Parantha', '196', NULL, 'Breakfast'");
        setupInsertToFood("NULL, 'Paneer Parantha', '234', NULL, 'Misc.'");
        setupInsertToFood("NULL, 'Plain Maggi', '188', NULL, 'Misc.'");
        setupInsertToFood("NULL, 'Cheese Maggi', '322', NULL, 'Misc.'");
        setupInsertToFood("NULL, 'Hot Chocolate', '77', NULL, 'Misc.'");
        setupInsertToFood("NULL, 'Vada Pav', '197', NULL, 'Misc.'");
        setupInsertToFood("NULL, 'Veg Hot Dog', '143', NULL, 'Misc.'");
        setupInsertToFood("NULL, 'Cheese Fries', '560', NULL, 'Misc.'");
        setupInsertToFood("NULL, 'Mayo Fries', '507', NULL, 'Misc.'");
        setupInsertToFood("NULL, 'White Pasta', '200', NULL, 'Misc.'");
        setupInsertToFood("NULL, 'Red Pasta', '250', NULL, 'Misc.'");
        setupInsertToFood("NULL, 'Strawberry Shake', '282', NULL, 'Misc.'");
        setupInsertToFood("NULL, 'Chocolate Shake', '590', NULL, 'Misc.'");
        setupInsertToFood("NULL, 'Aloo Burger', '367', NULL, 'Misc.'");
        setupInsertToFood("NULL, 'Cheese Burger', '303', NULL, 'Misc.'");
        setupInsertToFood("NULL, 'Fryms', '127', NULL, 'Lunch'");
        setupInsertToFood("NULL, 'Tomato Pappu', '224', NULL, 'Dinner'");
        setupInsertToFood("NULL, 'Matar Paneer', '451', NULL, 'Dinner'");
        setupInsertToFood("NULL, 'Semiyam Paysam', '235', NULL, 'Dinner'");
        setupInsertToFood("NULL, 'Chocos', '146', NULL, 'Breakfast'");
        setupInsertToFood("NULL, 'Plain Parantha', '126', NULL, 'Breakfast'");
        setupInsertToFood("NULL, 'Sweet Daliya', '123', NULL, 'Breakfast'");
        setupInsertToFood("NULL, 'Masala Idli', '65', NULL, 'Breakfast'");
        setupInsertToFood("NULL, 'Brown Bread', '73', NULL, 'Breakfast'");
        setupInsertToFood("NULL, 'White Bread', '32', NULL, 'Breakfast'");
    }

}