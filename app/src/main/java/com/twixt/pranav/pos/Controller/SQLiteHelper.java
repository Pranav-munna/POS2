package com.twixt.pranav.pos.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pranav on 12/4/2017.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "POS_SQLiteDatabase.db";
    Context con;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        context.openOrCreateDatabase(DATABASE_NAME, context.MODE_PRIVATE, null);
        con = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table CATEGORIES (POSITION VARCHAR,ID VARCHAR, NAME VARCHAR, COLOR VARCHAR);");
        db.execSQL("create table CART (CART_COUNT INTEGER,ITEM_ID INTEGER, ITEM_NAME VARCHAR, QUANTITY FLOAT, PRICE VARCHAR(1000), SHAPE VARCHAR, COLOR VARCHAR, IMAGES VARCHAR(250),SELLINGTYPE VARCHAR," +
                "MODIFIER VARCHAR,TAX VARCHAR);");
        db.execSQL("create table PAYMENT_TYPE (PAYMENT_COUNT INTEGER,PAYMENT_ID VARCHAR, PAYMENT_NAME VARCHAR, PAYMENTTYPE VARCHAR);");
        db.execSQL("create table LANGUAGE (ID INTEGER, RECEIPTS VARCHAR, REFUND VARCHAR, SALES VARCHAR, SETTINGS VARCHAR, CART VARCHAR, PAYMENT VARCHAR, CATEGORIES VARCHAR, FAVORITES VARCHAR, CASHIER VARCHAR," +
                " TOTAL VARCHAR, AMOUNT VARCHAR, TOTALAMOUNT VARCHAR, SELECTPAYMENT VARCHAR, INCLUSIVEOFTAXES VARCHAR, PASSCODE VARCHAR, CANCEL VARCHAR, ENTERYOURPIN VARCHAR, QUANTITY VARCHAR, OK VARCHAR, " +
                "REMANINGAMOUNT VARCHAR, PAY VARCHAR, SPLIT VARCHAR, CHARGE VARCHAR," +
                "RS VARCHAR,CHECK_NETWORK VARCHAR,FORGOT_PASSWORD VARCHAR,ENTER_ALL_FIELDS VARCHAR,INVALID_EMAIL VARCHAR,PASSWORD_ERROR VARCHAR,NO_ITEMS VARCHAR,OFFLINE_ERROR VARCHAR,EMAIL VARCHAR," +
                "LOGIN VARCHAR,APP_ID VARCHAR,KEY_ VARCHAR,ACTIVATE VARCHAR,ACTIVATION VARCHAR," +
                "INVALID_ACTIVATION VARCHAR,LOGOUT VARCHAR,NO_SUCH_CATEGORY VARCHAR,PAYED VARCHAR,SELECT_PAYMENT_METHOD VARCHAR,ENTEER_AMOUNT VARCHAR,AMOUNT_TOO_HEIGH VARCHAR,REFUND_UNAVAILABLE VARCHAR," +
                "SELECTED VARCHAR,ADDED VARCHAR,SELECT_PAYMENT_TOAST VARCHAR,APPLIED VARCHAR,INVALID_PIN VARCHAR,PAYMENT_TYPE VARCHAR,OFFERS_APPLIED VARCHAR," +
                "SEND VARCHAR,PAYLATER VARCHAR,SENDRECIPTS VARCHAR,ORDERS VARCHAR,ITEMS VARCHAR,WAIT VARCHAR,LOADING VARCHAR," +
                "NEWSALE VARCHAR,ALLCATEGORIES VARCHAR, AL VARCHAR,PAYLATERID VARCHAR,PRINT VARCHAR,VIW VARCHAR,PRINTTEST VARCHAR," +
                "MODIFIERS VARCHAR,DELETE_MODIFIERS VARCHAR,DISCOUNT VARCHAR,INVOICE VARCHAR,DATE VARCHAR,ITEM VARCHAR,PRICE VARCHAR,REFUNDED VARCHAR,NEXT VARCHAR,NO_RECIEPTS VARCHAR,R_REFUNDED VARCHAR," +
                "CASH VARCHAR,CHANGE VARCHAR,DESELECTPAYMENT VARCHAR,SELECTUSER VARCHAR,INVALIDEMAIL VARCHAR,REFUNDEDDISCOUNT VARCHAR);");


        db.execSQL("create table ITEMS (SELLINGTYPE VARCHAR,CATID VARCHAR,POSITION VARCHAR,ID VARCHAR,NAME VARCHAR, PRICE VARCHAR, SHAPE VARCHAR, COLOR VARCHAR, IMAGE VARCHAR, COUNT VARCHAR, TAX VARCHAR);");
        db.execSQL("create table PAY (TOTAL_AMOUNT VARCHAR,DISCOUNT VARCHAR,DISCOUNT_AMOUNT VARCHAR,PAY_AMOUNT VARCHAR, DISCOUNT_TYPE VARCHAR, PAYMENT_TYPE VARCHAR, ITEMS VARCHAR(1000));");
        db.execSQL("create table PAY_SPLIT (TOTAL_AMOUNT VARCHAR,DISCOUNT VARCHAR,DISCOUNT_AMOUNT VARCHAR,PAY_AMOUNT VARCHAR, DISCOUNT_TYPE VARCHAR, PAYMENT_TYPE VARCHAR, ITEMS VARCHAR(1000), SPLIT_DETAILS VARCHAR(1000));");
        db.execSQL("create table DISCOUNT_TYPE (LABEL VARCHAR, DISCOUNT VARCHAR, DISCOUNT_TYPE VARCHAR);");
        db.execSQL("create table PAY_LATER (TOTAL_AMOUNT VARCHAR,DISCOUNT VARCHAR,DISCOUNT_AMOUNT VARCHAR,PAY_AMOUNT VARCHAR, DISCOUNT_TYPE VARCHAR, PAYMENT_TYPE VARCHAR, ITEMS VARCHAR(1000), MAIL VARCHAR, PRINT TEXT);");
        db.execSQL("create table MODIFIERS (ITEM_ID VARCHAR,MODIFIER_ID VARCHAR, MODIFIER_NAME VARCHAR, OPTION_ID VARCHAR, OPTION_NAME VARCHAR, MODIFIER_PRICE VARCHAR);");


        db.execSQL("create table USERS (USERID VARCHAR,USERNAME VARCHAR,ROLE VARCHAR, USERIMAGE VARCHAR, PIN VARCHAR,ADDRESS VARCHAR,PHONE VARCHAR);");

        db.execSQL("INSERT INTO LANGUAGE  VALUES (1 , 'Receipts' , 'Refund' , 'Sales' , 'settings' , 'Cart' , 'Payment' , 'categories' , 'favorites' , 'Cashier' ," +
                " 'Total' , 'Amount' , 'Total Amount' , 'Select Payment' , 'Inclusive of Taxes' , 'Passcode' , 'Cancel' , 'Enter Your Pin', 'quantity', 'ok', " +
                "'Remaning Amount', 'Pay', 'Split', 'Charge'," +
                "'kr.','check network', 'Forgot password','enter all fields','invalid email or password', ' invalid email or password', ' no items added..!!', 'No Network..!!  you have no permission to work in offline..!!', ' Email'," +
                " ' login', ' appid', ' key', ' activate', ' activation'," +
                "'Invalid activation','Logout','No Such Category..!!','Payed','Select Payment Method','Enter Amount','Amount Heigh','Refund Unavailable'," +
                "'Selected','added','select payment type','Applied','Invalid Pin','Payment Types','Offers Applied','send' ,'paylater. ','sendreciept' ,'orders' ,'items' ,'wait ','loading '," +
                "'newsale' ,'allcategories ', 'all' ,'paylaterid','print','view','print test' " +
                ",'Modifier','Delete Modifier','Discount','Invoice','Date' ,'Item' ,'Price' ,'Refunded' ,'Next','No Reciepts','Refunded'," +
                "'Cash','Change','Deselect Paymentmethod First','Select user','Invalid Email','Refunded Discount' );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CATEGORIES");
        db.execSQL("DROP TABLE IF EXISTS CART");
        db.execSQL("DROP TABLE IF EXISTS PAYMENT_TYPE");
        db.execSQL("DROP TABLE IF EXISTS LANGUAGE");
        db.execSQL("DROP TABLE IF EXISTS ITEMS");
        db.execSQL("DROP TABLE IF EXISTS PAY");
        db.execSQL("DROP TABLE IF EXISTS PAY_LATER");
        db.execSQL("DROP TABLE IF EXISTS PAY_SPLIT");
        db.execSQL("DROP TABLE IF EXISTS DISCOUNT_TYPE");
        db.execSQL("DROP TABLE IF EXISTS MODIFIERS");
        db.execSQL("DROP TABLE IF EXISTS USERS");
        onCreate(db);
    }

    //region INSERT

    public Boolean insertUsers(String userId, String name, String role,
                               String image, String pin, String address,
                               String phone) {

        SQLiteDatabase database2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("USERID", userId);
        contentValues.put("USERNAME", name);
        contentValues.put("ROLE", role);
        contentValues.put("USERIMAGE", image);
        contentValues.put("PIN", pin);
        contentValues.put("ADDRESS", address);
        contentValues.put("PHONE", phone);

        database2.insert("USERS", null, contentValues);

        database2.close();
        return true;
    }

    public Boolean insertDiscount(String label, String discount, String discountType) {

        SQLiteDatabase database2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("LABEL", label);
        contentValues.put("DISCOUNT", discount);
        contentValues.put("DISCOUNT_TYPE", discountType);

        database2.insert("DISCOUNT_TYPE", null, contentValues);

        database2.close();
        return true;
    }


    public Boolean insertModifiers(String itemId, String modifier_id, String modifier_name, String option_id, String option_name, String modifier_price) {

        SQLiteDatabase database2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("ITEM_ID", itemId);
        contentValues.put("MODIFIER_ID", modifier_id);
        contentValues.put("MODIFIER_NAME", modifier_name);
        contentValues.put("OPTION_ID", option_id);
        contentValues.put("OPTION_NAME", option_name);
        contentValues.put("MODIFIER_PRICE", modifier_price);

        database2.insert("MODIFIERS", null, contentValues);

        database2.close();
        return true;
    }


    public Boolean insertPaySplit(String total_amount, String discount,
                                  String discount_amount, String pay_amount,
                                  String discount_type, String payment_type,
                                  String items, String split_details) {

        SQLiteDatabase database2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("TOTAL_AMOUNT", total_amount);
        contentValues.put("DISCOUNT", discount);
        contentValues.put("DISCOUNT_AMOUNT", discount_amount);
        contentValues.put("PAY_AMOUNT", pay_amount);
        contentValues.put("DISCOUNT_TYPE", discount_type);
        contentValues.put("PAYMENT_TYPE", payment_type);
        contentValues.put("ITEMS", items);
        contentValues.put("SPLIT_DETAILS", split_details);

        database2.insert("PAY_SPLIT", null, contentValues);

        database2.close();
        return true;
    }

    public Boolean insertPay(String total_amount, String discount,
                             String discount_amount, String pay_amount,
                             String discount_type, String payment_type,
                             String items) {

        SQLiteDatabase database2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("TOTAL_AMOUNT", total_amount);
        contentValues.put("DISCOUNT", discount);
        contentValues.put("DISCOUNT_AMOUNT", discount_amount);
        contentValues.put("PAY_AMOUNT", pay_amount);
        contentValues.put("DISCOUNT_TYPE", discount_type);
        contentValues.put("PAYMENT_TYPE", payment_type);
        contentValues.put("ITEMS", items);

        database2.insert("PAY", null, contentValues);

        database2.close();
        return true;
    }

    public Boolean insertPayLater(String total_amount, String discount,
                                  String discount_amount, String pay_amount,
                                  String discount_type, String payment_type,
                                  String items, String mail, String print) {

        SQLiteDatabase database2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("TOTAL_AMOUNT", total_amount);
        contentValues.put("DISCOUNT", discount);
        contentValues.put("DISCOUNT_AMOUNT", discount_amount);
        contentValues.put("PAY_AMOUNT", pay_amount);
        contentValues.put("DISCOUNT_TYPE", discount_type);
        contentValues.put("PAYMENT_TYPE", payment_type);
        contentValues.put("ITEMS", items);
        contentValues.put("MAIL", mail);
        contentValues.put("PRINT", print);

        database2.insert("PAY_LATER", null, contentValues);

        database2.close();
        return true;
    }


    public Boolean insertItems(String position, String id,
                               String name, String price,
                               String shape, String color,
                               String image, String catId,
                               String selling_type,
                               String tax) {

//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        SQLiteDatabase database2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Cursor cursor = database.rawQuery("SELECT ID FROM ITEMS where ID= " + id + ";", null);
        if (cursor.getCount() == 0) {
            contentValues.put("CATID", catId);
            contentValues.put("POSITION", position);
            contentValues.put("ID", id);
            contentValues.put("NAME", name);
            contentValues.put("PRICE", price);
            contentValues.put("SHAPE", shape);
            contentValues.put("COLOR", color);
            contentValues.put("IMAGE", image);
            contentValues.put("SELLINGTYPE", selling_type);
            contentValues.put("COUNT", "0");
            contentValues.put("TAX", tax);

            database2.insert("ITEMS", null, contentValues);
        } else {
            contentValues.put("CATID", catId);
            contentValues.put("POSITION", position);
            contentValues.put("ID", id);
            contentValues.put("NAME", name);
            contentValues.put("PRICE", price);
            contentValues.put("SHAPE", shape);
            contentValues.put("COLOR", color);
            contentValues.put("IMAGE", image);
            contentValues.put("SELLINGTYPE", selling_type);
            contentValues.put("COUNT", "0");
            contentValues.put("TAX", tax);
            database2.update("ITEMS", contentValues, "ID = " + id, null);
        }

        database2.close();
        database.close();
        return true;
    }

    public Boolean insertCategories(String position, String id,
                                    String name, String color) {


//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        SQLiteDatabase database2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Cursor cursor = database.rawQuery("SELECT ID FROM CATEGORIES where ID= " + id + ";", null);
        if (cursor.getCount() == 0) {
            contentValues.put("POSITION", position);
            contentValues.put("ID", id);
            contentValues.put("NAME", name);
            contentValues.put("COLOR", color);
            database2.insert("CATEGORIES", null, contentValues);
        } else {
            ContentValues cValues = new ContentValues();
            cValues.put("POSITION", position);
            cValues.put("ID", id);
            cValues.put("NAME", name);
            cValues.put("COLOR", color);
            database2.update("CATEGORIES", cValues, "ID = " + id, null);
        }

        database2.close();
        database.close();
        return true;
    }


    public Boolean insertPaymentTypes(String id, String name, String payment_type) {

        int count = new SharedPreferenceHelper(con).getInt(new SharedPreferenceHelper(con).getPAY_COUNT(), 0);
        SQLiteDatabase database2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("PAYMENT_ID", id);
        contentValues.put("PAYMENT_NAME", name);
        contentValues.put("PAYMENTTYPE", payment_type);
        contentValues.put("PAYMENT_COUNT", count++);

        new SharedPreferenceHelper(con).putInt(new SharedPreferenceHelper(con).getPAY_COUNT(), count);
        database2.insert("PAYMENT_TYPE", null, contentValues);

        database2.close();
        return true;
    }

    public Boolean insetCartData(int item_id, String item_name,
                                 Double quantity, String price,
                                 String shape, String color,
                                 String image, String selling_type,
                                 String modifier, String count, String tax) {


        int cnt = new SharedPreferenceHelper(con).getInt(new SharedPreferenceHelper(con).getCART_COUNT(), 0);
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        SQLiteDatabase database2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Cursor cursor = database.rawQuery("SELECT QUANTITY FROM CART WHERE ITEM_ID= " + item_id + ";", null);
//        long cnt = DatabaseUtils.queryNumEntries(database, "CART");
        if (cursor.getCount() == 0) {
            contentValues.put("ITEM_ID", item_id);
            contentValues.put("ITEM_NAME", item_name);
            contentValues.put("QUANTITY", quantity);
            contentValues.put("PRICE", price + " ");
            contentValues.put("SHAPE", shape);
            contentValues.put("COLOR", color);
            contentValues.put("IMAGES", image);
            contentValues.put("SELLINGTYPE", selling_type);
            contentValues.put("CART_COUNT", cnt++);
            contentValues.put("MODIFIER", modifier);
            contentValues.put("TAX", tax);


            new SharedPreferenceHelper(con).putInt(new SharedPreferenceHelper(con).getCART_COUNT(), cnt);
            database2.insert("CART", null, contentValues);
        } else {
            database2.execSQL("UPDATE CART SET QUANTITY = QUANTITY + " + quantity + " WHERE ITEM_ID =" + item_id + ";");
//            database2.execSQL("UPDATE CART SET PRICE = PRICE * QUANTITY WHERE ITEM_ID =" + item_id + ";");
        }


        ContentValues cv = new ContentValues();
        cv.put("COUNT", count);
        database2.update("ITEMS", cv, "ID = " + item_id, null);


        database2.close();
        database.close();
        return true;
    }

    //endregion

    //region update



    public Boolean updateLanguage(String receipts, String refund, String sales, String settings, String cart, String payment, String categories, String favorites,
                                  String cashier, String total, String amount, String total_amount, String select_payment, String inclusive_of_taxes, String passcode, String cancel,
                                  String enter_your_pin, String quantity, String ok, String remaning_amount, String pay, String split, String charge, String rs,
                                  String check_network, String forgot_password, String enter_all_fields, String invalid_email, String password_error, String no_items,
                                  String offline_error, String email, String login, String appid, String key, String activate, String activation,
                                  String invalid_activation, String logout, String no_such_category, String payed, String select_payment_method,
                                  String enter_amount, String amount_too_heigh, String refund_unavailable, String selected, String added, String select_payment_toast,
                                  String applied, String invalidpin, String paymenttype, String offersapplied,
                                  String send, String paylater, String sendreceipts, String orders, String items,
                                  String wait, String loading, String newsale, String allcategories, String all, String paylaterid, String print,
                                  String view, String printtest, String modifier, String delete_modifier, String discount, String invoice,
                                  String date, String item, String price, String refunded, String next, String no_reciepts, String refundedReciept,
                                  String cash, String change, String deselectpayment, String selectuser, String invalidemail, String refunddiscount
    ) {


        SQLiteDatabase database2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("RECEIPTS", receipts);
        contentValues.put("REFUND", refund);
        contentValues.put("SALES", sales);
        contentValues.put("SETTINGS", settings);
        contentValues.put("CART", cart);
        contentValues.put("PAYMENT", payment);
        contentValues.put("CATEGORIES", categories);
        contentValues.put("FAVORITES", favorites);
        contentValues.put("CASHIER", cashier);
        contentValues.put("TOTAL", total);
        contentValues.put("AMOUNT", amount);
        contentValues.put("TOTALAMOUNT", total_amount);
        contentValues.put("SELECTPAYMENT", select_payment);
        contentValues.put("INCLUSIVEOFTAXES", inclusive_of_taxes);
        contentValues.put("PASSCODE", passcode);
        contentValues.put("CANCEL", cancel);
        contentValues.put("ENTERYOURPIN", enter_your_pin);
        contentValues.put("QUANTITY", quantity);
        contentValues.put("OK", ok);
        contentValues.put("REMANINGAMOUNT", remaning_amount);
        contentValues.put("PAY", pay);
        contentValues.put("SPLIT", split);
        contentValues.put("CHARGE", charge);
        contentValues.put("RS", rs);
        contentValues.put("CHECK_NETWORK", check_network);
        contentValues.put("FORGOT_PASSWORD", forgot_password);
        contentValues.put("ENTER_ALL_FIELDS", enter_all_fields);
        contentValues.put("INVALID_EMAIL", invalid_email);
        contentValues.put("PASSWORD_ERROR", password_error);
        contentValues.put("NO_ITEMS", no_items);
        contentValues.put("OFFLINE_ERROR", offline_error);
        contentValues.put("EMAIL", email);
        contentValues.put("LOGIN", login);
        contentValues.put("APP_ID", appid);
        contentValues.put("KEY_", key);
        contentValues.put("ACTIVATE", activate);
        contentValues.put("ACTIVATION", activation);

        contentValues.put("INVALID_ACTIVATION", invalid_activation);
        contentValues.put("LOGOUT", logout);
        contentValues.put("NO_SUCH_CATEGORY", no_such_category);
        contentValues.put("PAYED", payed);

        contentValues.put("SELECT_PAYMENT_METHOD", select_payment_method);
        contentValues.put("ENTEER_AMOUNT", enter_amount);
        contentValues.put("AMOUNT_TOO_HEIGH", amount_too_heigh);
        contentValues.put("REFUND_UNAVAILABLE", refund_unavailable);
        contentValues.put("SELECTED", selected);
        contentValues.put("ADDED", added);
        contentValues.put("SELECT_PAYMENT_TOAST", select_payment_toast);
        contentValues.put("APPLIED", applied);
        contentValues.put("INVALID_PIN", invalidpin);
        contentValues.put("PAYMENT_TYPE", paymenttype);
        contentValues.put("OFFERS_APPLIED", offersapplied);

        contentValues.put("SEND ", send);
        contentValues.put("PAYLATER ", paylater);
        contentValues.put("SENDRECIPTS ", sendreceipts);
        contentValues.put("ORDERS ", orders);
        contentValues.put("ITEMS ", items);
        contentValues.put("WAIT ", wait);
        contentValues.put("LOADING", loading);
        contentValues.put("NEWSALE ", newsale);
        contentValues.put("ALLCATEGORIES", allcategories);
        contentValues.put("AL ", all);
        contentValues.put("PAYLATERID", paylaterid);
        contentValues.put("PRINT", print);
        contentValues.put("VIW", view);
        contentValues.put("PRINTTEST", printtest);


        contentValues.put("MODIFIERS", modifier);
        contentValues.put("DELETE_MODIFIERS", delete_modifier);
        contentValues.put("DISCOUNT", discount);
        contentValues.put("INVOICE", invoice);
        contentValues.put("DATE", date);
        contentValues.put("ITEM", item);
        contentValues.put("PRICE", price);
        contentValues.put("REFUNDED", refunded);
        contentValues.put("NEXT", next);
        contentValues.put("NO_RECIEPTS", no_reciepts);
        contentValues.put("R_REFUNDED", refundedReciept);



        contentValues.put("CASH", cash);
        contentValues.put("CHANGE", change);
        contentValues.put("DESELECTPAYMENT", deselectpayment);
        contentValues.put("SELECTUSER", selectuser);
        contentValues.put("INVALIDEMAIL", invalidemail);
        contentValues.put("REFUNDEDDISCOUNT", refunddiscount);


        database2.update("LANGUAGE", contentValues, "ID = 1", null);

        database2.close();
        return true;
    }

    public Boolean updateQuantity(String quantity, String id) {


        SQLiteDatabase database2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("QUANTITY", quantity);
        database2.update("CART", contentValues, "ITEM_ID = " + id, null);

        database2.close();
        return true;
    }

    public Boolean updateModifiers(String modifier, String item_id) {

        SQLiteDatabase database2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("MODIFIER", modifier);
        database2.update("CART", contentValues, "ITEM_ID = " + item_id, null);

        database2.close();
        return true;
    }

    //endregion

    //region get data

    public Cursor discountTypes(int place) {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM DISCOUNT_TYPE LIMIT " + place + ", 1", null);
//        database.close();
        return cursor;
    }

    public Cursor getAllItems(int place) {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM ITEMS ORDER BY COUNT DESC LIMIT " + place + ", 1 ", null);

//        database.close();
        return cursor;
    }

    public Cursor discountTypes(String pos) {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM DISCOUNT_TYPE WHERE DISCOUNT_TYPE ='" + pos + "'", null);
//        database.close();
        return cursor;
    }

    public Cursor discountTypesString(String label) {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM DISCOUNT_TYPE WHERE LABEL = '" + label + "'", null);
//        database.close();
        return cursor;
    }

    public Cursor getUserDetails(String username) {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM USERS WHERE USERNAME = '" + username + "'", null);
//        database.close();
        return cursor;
    }

    public Cursor cartdatas(int id) {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM CART LIMIT " + id + ",1", null);
//        Cursor cursor = database.rawQuery("SELECT * FROM CATEGORIES LIMIT " + place + ", 1", null);
//        database.close();
        return cursor;
    }

    public Cursor catdata(String name) {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM CATEGORIES WHERE NAME = '" + name + "'", null);

//        database.close();
        return cursor;
    }

    public Cursor paymentTypedatas(int id) {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM PAYMENT_TYPE WHERE PAYMENT_COUNT=" + id + "", null);

//        database.close();
        return cursor;
    }

    public Cursor getLanguage() {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM LANGUAGE WHERE ID = 1", null);
//        database.close();
        return cursor;
    }

    public Cursor getUsers(int place) {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM USERS LIMIT " + place + ", 1", null);
//        database.close();
        return cursor;
    }

    public Cursor getcategories(int place) {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM CATEGORIES LIMIT " + place + ", 1", null);

//        database.close();
        return cursor;
    }

    public Cursor getModifier(int place, String id) {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM MODIFIERS WHERE ITEM_ID ='" + id + "' LIMIT " + place + ", 1", null);
//        database.close();
        return cursor;
    }

    public Cursor getitem(int id) {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM CART WHERE ITEM_ID = " + id + "", null);
//        database.close();
        return cursor;
    }

    public Cursor getPendingPay(int place) {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM PAY LIMIT " + place + ", 1", null);
//        database.close();
        return cursor;
    }

    public Cursor getPendingPaylater(int place) {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM PAY_LATER LIMIT " + place + ", 1", null);
//        database.close();
        return cursor;
    }

    public Cursor getPendingPaySplit(int place) {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM PAY_SPLIT LIMIT " + place + ", 1", null);
//        database.close();
        return cursor;
    }

    //endregion

    //region size

    public Cursor getModifierSize(int item_id) {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
//        Cursor cursor = database.query("ITEMS ", null, null, null, null, null, null, null);
        Cursor cursor = database.rawQuery("SELECT * FROM MODIFIERS WHERE ITEM_ID ='" + item_id + "'", null);
//        database.close();
        return cursor;
    }

    public Cursor getUserSize() {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.query("USERS", null, null, null, null, null, null, null);
//        database.close();
        return cursor;
    }

    public Cursor getDiscountSize() {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.query("DISCOUNT_TYPE", null, null, null, null, null, null, null);
//        database.close();
        return cursor;
    }

    public Cursor getPendingPaySize() {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.query("PAY", null, null, null, null, null, null, null);
//        database.close();
        return cursor;
    }

    public Cursor getPendingPayLaterSize() {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.query("PAY_LATER", null, null, null, null, null, null, null);
//        database.close();
        return cursor;
    }

    public Cursor getPendingPaySplitSize() {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.query("PAY_SPLIT", null, null, null, null, null, null, null);
//        database.close();
        return cursor;
    }

    public Cursor getItems(int place, String id) {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM ITEMS WHERE CATID =" + id + " LIMIT " + place + ", 1", null);
//        database.close();
        return cursor;
    }

    public Cursor getItems(int place) {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM ITEMS  LIMIT " + place + ", 1", null);
//        database.close();
        return cursor;
    }

    public Cursor getcategoriessize() {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.query("CATEGORIES ", null, null, null, null, null, null, null);
//        database.close();
        return cursor;
    }

    public Cursor getPaymentTypes() {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.query("PAYMENT_TYPE ", null, null, null, null, null, null, null);
//        database.close();
        return cursor;
    }

    public Cursor getItemssize(String id) {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
//        Cursor cursor = database.query("ITEMS ", null, null, null, null, null, null, null);
        Cursor cursor = database.rawQuery("SELECT * FROM ITEMS WHERE CATID ='" + id + "'", null);
//        database.close();
        return cursor;
    }

    public Cursor getItemssize() {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM ITEMS ", null);
//        database.close();
        return cursor;
    }

//endregion

    //region delete

    public Boolean deleteDiscount() {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM DISCOUNT_TYPE");
//        database.close();
        return true;
    }

    public Boolean deleteModifiers() {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM MODIFIERS");
//        database.close();
        return true;
    }

    public Boolean deleteCart() {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM CART");
//        database.close();
        return true;
    }

    public Boolean deleteCartItem(int id) {
//        SQLiteDatabase database = this.getReadableDatabase();


        SQLiteDatabase database2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        database2.execSQL("DELETE FROM CART WHERE CART_COUNT = " + id + "");
        int i = id;
        while (i + 1 < new SharedPreferenceHelper(con).getInt(new SharedPreferenceHelper(con).getCART_COUNT(), 0)) {

            contentValues.put("CART_COUNT", i);
            database2.update("CART", contentValues, "ITEM_ID = " + (i + 1), null);
            i++;

        }
        new SharedPreferenceHelper(con).putInt(new SharedPreferenceHelper(con).getCART_COUNT(), (new SharedPreferenceHelper(con).getInt(new SharedPreferenceHelper(con).getCART_COUNT(), 0)) - 1);
//        database2.close();
        return true;
    }

    public Boolean deletePay() {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM PAY");
//        database.close();
        return true;
    }

    public Boolean deletePayLater() {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM PAY_LATER");
//        database.close();
        return true;
    }

    public Boolean deletePaySplit() {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM PAY_SPLIT");
//        database.close();
        return true;
    }

    public Boolean deleteCato() {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM CATEGORIES");
//        database.close();
        return true;
    }

    public Boolean deleteItems(/*String id*/) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM ITEMS ");//WHERE CATID ='" + id + "'
//        database.close();
        return true;
    }

    public Boolean deleteUsers(/*String id*/) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM USERS ");//WHERE CATID ='" + id + "'
//        database.close();
        return true;
    }

    /*public Boolean deleteLAn() {
        SQLiteDatabase database = this.getReadableDatabase();
        database.execSQL("DELETE FROM LANGUAGE_HOME");
        database.close();
        return true;
    }*/

    public Boolean deletePaymentType() {
//        SQLiteDatabase database = this.getReadableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM PAYMENT_TYPE");
//        database.close();
        return true;
    }

//endregion

}
