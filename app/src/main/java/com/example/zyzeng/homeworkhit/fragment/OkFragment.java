package com.example.zyzeng.homeworkhit.fragment;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.zyzeng.homeworkhit.R;
import com.example.zyzeng.homeworkhit.database.MyDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.zyzeng.homeworkhit.database.MyDB.COLUMN_NAME_CONTENT;
import static com.example.zyzeng.homeworkhit.database.MyDB.COLUMN_NAME_DATE;
import static com.example.zyzeng.homeworkhit.database.MyDB.COLUMN_NAME_SUBJECT;
import static com.example.zyzeng.homeworkhit.database.MyDB.TABLE_NAME_FINISH;
import static com.example.zyzeng.homeworkhit.database.MyDB.TABLE_NAME_HW;


/**
 * A simple {@link Fragment} subclass.
 */
public class OkFragment extends Fragment {
    private ListView listView;
    private MyDB DB;
    private SQLiteDatabase database;
    private ArrayList list;

    public OkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        DB = new MyDB(getActivity());
        database = DB.getReadableDatabase();

        return inflater.inflate(R.layout.fragment_ok, container, false);
    }

    private void ListUpdate(){
        SimpleAdapter myAdapter = new SimpleAdapter(
                getActivity(),
                getData(),
                R.layout.list_item_card,
                new String[]{COLUMN_NAME_SUBJECT,COLUMN_NAME_DATE},
                new int[]{R.id.line1,R.id.line2}
        );
        listView = getActivity().findViewById(R.id.listview);
        listView.setAdapter(myAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        String sub = bundle.getString("sub");
        String date = bundle.getString("date");
        String con = bundle.getString("con");
    }

    private List<Map<String, Object>> getData(){
        Cursor cursor = database.query(TABLE_NAME_FINISH,null,null,null,null,null,null);
        list = new ArrayList<Map<String,Object>>();
        while (cursor.moveToNext()){
            String subject = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_SUBJECT));
            String day = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DATE));
            String content = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_CONTENT));
            Map<String,Object> map = new HashMap<>();
            map.put(COLUMN_NAME_SUBJECT,subject);
            map.put(COLUMN_NAME_DATE,day);
            list.add(map);
        }
        return list;
    }
}
