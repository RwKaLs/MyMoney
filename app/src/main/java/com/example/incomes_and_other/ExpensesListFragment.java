package com.example.incomes_and_other;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class ExpensesListFragment extends Fragment {

    DBHelper dbHelperEXP;
    ArrayList<Expense> expenses = new ArrayList<>();
    int filter_type;

    public ExpensesListFragment(int filter_type){
        this.filter_type = filter_type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelperEXP = new DBHelper(this.getContext(), DBHelper.STR_EXP);
        loadDb();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expenses_list, container, false);

        // Add the following lines to create RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.list_EXP);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new ExpensesListAdapter(expenses));
        return view;
    }

    @SuppressLint("Recycle")
    private void loadDb(){
        SQLiteDatabase sqlLoad = dbHelperEXP.getWritableDatabase();
        Cursor cursor = sqlLoad.query(DBHelper.STR_EXP, null, null, null, null, null, null);
        switch (this.filter_type){
            case 0:
                cursor = sqlLoad.query(DBHelper.STR_EXP, null, null, null, null, null, null);
                break;
            case 1:
                cursor = sqlLoad.query(DBHelper.STR_EXP, null, "date > date('now', '-7 days')", null, null, null, null);
                break;
            case 2:
                cursor = sqlLoad.query(DBHelper.STR_EXP, null, "date > date('now', '-1 months')", null, null, null, null);
                break;
            case 3:
                cursor = sqlLoad.query(DBHelper.STR_EXP, null, "date > date('now', '-3 months')", null, null, null, null);
                break;
        }
        if (cursor.moveToFirst()){
            int date = cursor.getColumnIndex(DBHelper.KEY_DATE);
            int summa = cursor.getColumnIndex(DBHelper.KEY_SUMMA);
            int type = cursor.getColumnIndex(DBHelper.KEY_TYPE);
            do {
                expenses.add(new Expense(cursor.getString(date), cursor.getInt(summa), cursor.getString(type)));
/*                    Log.d("HEYFROMSPISOK", "DATE = " + cursor.getString(date) + " SUMMA = " +
*                            cursor.getInt(summa) + " TYPE = " + cursor.getString(type));*/
            } while (cursor.moveToNext());
        } else {
            Log.d("DBLOGEXP", "NODATA");
        }
    }
}