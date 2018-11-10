package com.example.zyzeng.homeworkhit.fragment;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class OkFragment extends Fragment {
    private ListView listView;
    private MyDB DB;
    private SQLiteDatabase database;
    private ArrayList list;
    private int id;
    private FloatingActionButton fab;

    public OkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        DB = new MyDB(getActivity(),"database",null,1);
        database = DB.getReadableDatabase();

        return inflater.inflate(R.layout.fragment_ok, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListUpdate();
        initView();
    }

    private void initView(){
        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.setHeaderTitle("contextMenu");
                contextMenu.add(1,1,1,"删除");
                contextMenu.add(1,2,1,"进入聊天室");
            }
        });
        fab = getActivity().findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListUpdate();
            }
        });
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        View view = menuInfo.targetView;
        TextView textView = view.findViewById(R.id.line1);
        String sub = textView.getText().toString();
        String whereClause = "subject=?";
        String[] whereArgs = {sub};
        switch (item.getItemId()){
            case 1:
                database.delete(TABLE_NAME_FINISH,whereClause,whereArgs);
                System.out.println(sub);
                ListUpdate();
        }
        return super.onContextItemSelected(item);
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

    protected void ListUpdate(){
        SimpleAdapter myAdapter = new SimpleAdapter(
                getActivity(),
                getData(),
                R.layout.list_item_card,
                new String[]{COLUMN_NAME_SUBJECT,COLUMN_NAME_DATE},
                new int[]{R.id.line1,R.id.line2}
        );
        listView = getActivity().findViewById(R.id.okListView);
        listView.setAdapter(myAdapter);
    }

}
