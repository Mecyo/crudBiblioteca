package br.com.mecyo.biblioteca.Util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Emerson Santos on 30/07/18.
 */

public class DbGateway {

    private static DbGateway gw;
    private SQLiteDatabase db;

    private DbGateway(Context ctx){
        DbHelper helper = new DbHelper(ctx);
        db = helper.getWritableDatabase();
    }

    public static DbGateway getInstance(Context ctx){
        if(gw == null)
            gw = new DbGateway(ctx);
        return gw;
    }

    public SQLiteDatabase getDatabase(){
        return this.db;
    }
}
