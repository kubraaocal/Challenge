package com.example.challenge.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.challenge.model.RecyclerModel;

import java.util.ArrayList;
import java.util.List;

public class FavDB extends SQLiteOpenHelper {
    private static int DB_VERSION = 1;
    private static String DATABASE_NAME = "CatDB";
    private static String TABLE_NAME = "favoriteTable";
    public static String KEY_ID = "id";
    public static String ITEM_TITLE = "itemTitle";
    public static String ITEM_IMAGE = "itemImage";
    public static String FAVORITE_STATUS = "fStatus";
    // dont forget write this spaces
    private static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + KEY_ID + " TEXT," + ITEM_TITLE+ " TEXT,"
            + ITEM_IMAGE + " TEXT," + FAVORITE_STATUS+" TEXT)";

    List<RecyclerModel> catList;

    public FavDB(Context context) { super(context,DATABASE_NAME,null,DB_VERSION);}
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    public boolean addCat(String id,String name, String url,boolean status){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("id",id);
        values.put("itemTitle",name);
        values.put("itemImage",url);
        values.put("fStatus",status);
        long result=db.insert(TABLE_NAME,null,values);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }

    public List<RecyclerModel> getAllList(){
        catList=new ArrayList<>();
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor  cursor = db.rawQuery("select * from "+TABLE_NAME,null);
        while (cursor.moveToNext()){
            int index1 =cursor.getColumnIndex(KEY_ID);
            String getId= cursor.getString(index1);
            int index2 =cursor.getColumnIndex(ITEM_TITLE);
            String getName= cursor.getString(index2);
            int index3 =cursor.getColumnIndex(ITEM_IMAGE);
            String getImage= cursor.getString(index3);
            int index4 =cursor.getColumnIndex(FAVORITE_STATUS);
            String getFavStatus= cursor.getString(index4);

            RecyclerModel model=new RecyclerModel(getId,getName,getImage);
            catList.add(model);
        }
        return catList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE if exists "+TABLE_NAME);
    }

}
