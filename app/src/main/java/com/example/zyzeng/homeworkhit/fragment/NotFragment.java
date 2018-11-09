package com.example.zyzeng.homeworkhit.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.zyzeng.homeworkhit.database.MyDB;
import com.example.zyzeng.homeworkhit.activity.EditActivity;
import com.example.zyzeng.homeworkhit.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.zyzeng.homeworkhit.database.MyDB.COLUMN_NAME_CONTENT;
import static com.example.zyzeng.homeworkhit.database.MyDB.COLUMN_NAME_DATE;
import static com.example.zyzeng.homeworkhit.database.MyDB.COLUMN_NAME_SUBJECT;
import static com.example.zyzeng.homeworkhit.database.MyDB.TABLE_NAME_HW;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotFragment extends Fragment {

    private View view;
    private FloatingActionButton fab;
    private Button button;
    private Calendar calendar = Calendar.getInstance();
    private SQLiteDatabase database;
    private MyDB DB;
    private ArrayList list;
    private ListView listView;
//    private String[] name = new String[]{"图论","英语","程序设计"};
//    private String[] time = new String[]{"2018.11.4","2018.11.5","2018.11.7"};
    public NotFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        DB = new MyDB(getActivity());
        database = DB.getReadableDatabase();
        return inflater.inflate(R.layout.fragment_not, container, false);
    }

    //坑，注意这里fragment中的点击事件要写在onActivityCreated里
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fab = getActivity().findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditActivity.CLICK_WAY = 0;
                Intent intent = new Intent(getActivity(),EditActivity.class);
                intent.putExtra("from",1);
                startActivityForResult(intent,1);
            }
        });
//        List<Map<String,Object>> listItem = new ArrayList<>();
//        for (int i = 0; i < name.length; i++) {
//            Map<String,Object> showItem = new HashMap<>();
//            showItem.put("name",name[i]);
//            showItem.put("time",time[i]);  //利用HashMap存储一个名字和时间，前面的key对应后面的adapter中的参数
//            listItem.add(showItem);        //用ArrayList将一对名字和时间存进去
//        }
        ListUpdate(); //打开界面的时候更新一次listView

        //listView的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EditActivity.CLICK_WAY = 1;
                ListView listView = (ListView)adapterView;
                HashMap<String,String>map =(HashMap<String, String>) listView.getItemAtPosition(i);
                String sub = map.get("subject");
                Cursor cursor = database.query(TABLE_NAME_HW,null,"subject =?",new String[]{sub},
                        null,null,null);
                if (cursor.moveToFirst()){
                    String subject = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_SUBJECT));
                    String day = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DATE));
                    String content = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_CONTENT));
                    Bundle bundle = new Bundle();
                    bundle.putString("subject",subject);
                    bundle.putString("day",day);
                    bundle.putString("content",content);
                    bundle.putInt("itemID",i);
                    Intent intent = new Intent(getActivity(),EditActivity.class);
                    intent.putExtras(bundle);
                    intent.putExtra("from",2);
                    startActivityForResult(intent,3);
                }
                cursor.close();
            }
        });
        //listView的长点击事件执行删除
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListView listView = (ListView)adapterView;
                HashMap<String,String>map =(HashMap<String, String>) listView.getItemAtPosition(i);
                String sub = map.get("subject");
                String con = map.get("content");
                String date = map.get("date");
                OkFragment okFragment = new OkFragment();
                Bundle bundle = new Bundle();
                bundle.putString("sub",sub);
                bundle.putString("con",con);
                bundle.putString("date",date);
                okFragment.setArguments(bundle);
                String sqlDel = "delete from hw where subject="+"'"+sub+"'";
                database.execSQL(sqlDel);
                ListUpdate();
                return true;
            }
        });
    }
    //得到数据
    private List<Map<String, Object>> getData(){
        Cursor cursor = database.query(TABLE_NAME_HW,null,null,null,null,null,null);
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
    //更新列表
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
    //更新界面
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==2)
            ListUpdate();
    }
}
