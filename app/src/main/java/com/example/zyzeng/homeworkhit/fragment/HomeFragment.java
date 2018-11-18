package com.example.zyzeng.homeworkhit.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.zyzeng.homeworkhit.R;
import com.example.zyzeng.homeworkhit.activity.ChatActivity;
import com.example.zyzeng.homeworkhit.activity.LoginActivity;
import com.example.zyzeng.homeworkhit.config.SessionManager;
import com.example.zyzeng.homeworkhit.database.MyDB;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private TextView nameView;
    private TextView emailView;
    private Button logoutButton;
    private Button chat;
    private MyDB sqLiteManager;
    private SessionManager sessionManager;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!sessionManager.isLoggedIn()) {
            logoutUser();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        chat = getActivity().findViewById(R.id.chat);
        nameView = getActivity().findViewById(R.id.nameView);
        emailView = getActivity().findViewById(R.id.emailView);
        logoutButton = getActivity().findViewById(R.id.logoutButton);

        if (!sessionManager.isLoggedIn()) {
            logoutUser();
        }
        nameView.setText(sqLiteManager.getUserDetails().get("name"));
        emailView.setText(sqLiteManager.getUserDetails().get("email"));
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(getActivity(),ChatActivity.class);
                startActivity(intent);
            }
        });
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sqLiteManager = new MyDB(getActivity());
        sessionManager = new SessionManager(getActivity());

        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    private void logoutUser() {
        //设置登陆状态为false
        sessionManager.setLogin(false);
        //数据库中删除用户信息
        sqLiteManager.deleteUsers();
        //跳转到登陆页面
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }
}
