package com.example.incomes_and_other;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class IncomesListFragment extends Fragment {

    DBHelper dbHelperINC;
    ArrayList<Income> incomes = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelperINC = new DBHelper(this.getContext(), DBHelper.STR_INC);
        loadDb();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_incomes_list, container, false);

        // Add the following lines to create RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.list_INC);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new IncomesListAdapter(incomes));
        return view;
    }

    @SuppressLint("Recycle")
    private void loadDb(){
        SQLiteDatabase sqlLoad = dbHelperINC.getWritableDatabase();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date datef = new Date();
        Cursor cursor = sqlLoad.query(DBHelper.STR_INC, null, "date > date(" + formatter.format(datef) + ", '-7 days')", null, null, null, null);
                                                                                                               /* менять      ^     на '-1 months' or '-3 months' or '-1 years'
                                                                                                               * для этого передать флаг при выборе фильтра и менять recyclerview этой выборкой
                                                                                                               * сделать конструктор, вызывающийся из incomes activity*/
        if (cursor.moveToFirst()){
            int date = cursor.getColumnIndex(DBHelper.KEY_DATE);
            int summa = cursor.getColumnIndex(DBHelper.KEY_SUMMA);
            int type = cursor.getColumnIndex(DBHelper.KEY_TYPE);
            do {
                incomes.add(new Income(cursor.getString(date), cursor.getInt(summa), cursor.getString(type)));
//                    Log.d("HEYFROMSPISOK", "DATE = " + cursor.getString(date) + " SUMMA = " +
//                            cursor.getInt(summa) + " TYPE = " + cursor.getString(type));
            } while (cursor.moveToNext());
        } else {
            Log.d("DBLOGINC", "NODATA");
        }
    }
}