package com.tarena.cookbook.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/12/17
 */
public class CooksDBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "cooks.db";
    public static final int VERSION = 1;
    /**
     * 是否添加了收藏：0不是，1是
     */
    public static final String MY_LIKE = "my_like";
    /**
     * 是否是观看历史浏览记录：0不是，1是
     */
    public static final String HISTORY_LOOK = "history";
    public static final String TABLE_TEXT = "cooks_info";
    public static final String ID = "_id";
    public static final String TITLE = "title";
    /**
     * 关于菜的功效、标签
     */
    public static final String TAGS = "tags";
    /**
     * 菜品的简介
     */
    public static final String IMTRO = "imtro";
    /**
     * 菜品所需材料
     */
    public static final String INGREDIENTS = "ingredients";
    /**
     * 辅助材料
     */
    public static final String BURDEN = "burden";
    /**
     * 图片
     */
    public static final String ALBUMS = "albums";
    public static final String CREATE_TEXT_TABLE = "create table " + TABLE_TEXT + "(" +
            ID + " integer primary key," +
            TITLE + "," +
            TAGS + "," +
            IMTRO + "," +
            INGREDIENTS + "," +
            BURDEN + "," +
            ALBUMS + "," +
            HISTORY_LOOK + " integer default 0," +
            MY_LIKE + " integer default 0)";


    public static final String TABLE_IMGS = "cooks_imgs";
    public static final String IMG = "img";
    public static final String STEP = "step";
    public static String CREATE_IMGS_TABLE = "create table " + TABLE_IMGS + "(" +
            ID + " integer," +
            IMG + "," +
            STEP + ")";

    public CooksDBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TEXT_TABLE);
        db.execSQL(CREATE_IMGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
