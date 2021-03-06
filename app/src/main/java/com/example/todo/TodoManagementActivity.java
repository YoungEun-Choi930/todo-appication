package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TodoManagementActivity extends AppCompatActivity {
    private HashMap<String, Object> todoList;
    public ToDoAdapter toDoAdapter;
    public static TodoManagementActivity mContext;
    String sdate;
    MaterialCalendarView materialCalendarView;
    private long start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        start = System.currentTimeMillis();

        setContentView(R.layout.activity_todo);
        mContext=this;
        Toolbar myToolbar = (Toolbar) findViewById(R.id.todo_toolbar);
        setSupportActionBar(myToolbar);

        AlarmManagement.alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        AlarmManagement.notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        materialCalendarView.setSelectedDate(CalendarDay.today());
        materialCalendarView.setDynamicHeightEnabled(true);
        Button btn_subject = (Button) findViewById(R.id.btn_subject);
        Button btn_friend = (Button) findViewById(R.id.btn_friend);
        Button btn_alarm = (Button) findViewById(R.id.btn_alarm);
        Button btn_month = (Button) findViewById(R.id.btn_month);

        todoList = new HashMap<>();


        btn_subject.setOnClickListener((view) -> { // ?????????????????? ??????
            Intent intent = new Intent(getApplicationContext(), SubjectManagementActivity.class);
            startActivity(intent);

        });
        btn_friend.setOnClickListener((view) -> { // ?????????????????? ??????
            Intent intent = new Intent(getApplicationContext(), FriendsManagementActivity.class);
            startActivity(intent);

        });
        btn_alarm.setOnClickListener((view) -> { // ?????????????????? ??????
            Intent intent = new Intent(getApplicationContext(), AlarmManagementActivity.class);
            startActivity(intent);

        });
        btn_month.setOnClickListener((view) -> { // ?????????????????? ??????
            if(btn_month.getText()=="??????"){
                btn_month.setText("??????");
                materialCalendarView.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit();
            }
            else{
                btn_month.setText("??????");
                materialCalendarView.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();
            }
        });


        /* ------------------------ ?????? ????????? ?????? ?????? ???????????? ?????? ?????? -------------------------- */
        String stringdate = CalendarDay.today().toString().replace("CalendarDay{","").replace("}","");  //????????????

        String[] strdate = stringdate.split("-");               // ?????? ????????? 20210901 ??? ?????? ??????
        if(strdate[1].length() == 1) strdate[1] = "0" + strdate[1];
        if(strdate[2].length() == 1) strdate[2] = "0" + strdate[2];
        sdate = strdate[0] + strdate[1] + strdate[2];

        ToDoManagement toDoManagement = new ToDoManagement();
        todoList = toDoManagement.getToDoList(sdate);
       // todoList = getToDoList(sdate);      // to do list ????????????


        /* -------------------- ???????????? ????????? ???????????? ?????? ?????? ????????? to do ?????????----------------------- */
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                start = System.currentTimeMillis();
                String stringdate = date.toString().replace("CalendarDay{","").replace("}",""); //????????? ??????

                String[] strdate = stringdate.split("-");               // ?????? ????????? 20210901 ??? ?????? ??????
                if(strdate[1].length() == 1) strdate[1] = "0" + strdate[1];
                if(strdate[2].length() == 1) strdate[2] = "0" + strdate[2];
                sdate = strdate[0] + strdate[1] + strdate[2];

                ToDoManagement toDoManagement = new ToDoManagement();
                todoList = toDoManagement.getToDoList(sdate);
                //todoList = getToDoList(sdate);          // to do list ????????????

                toDoAdapter.setList(todoList);
                toDoAdapter.notifyDataSetChanged();     // ?????? ????????????

                long end = System.currentTimeMillis();

                System.out.println("--------------------------------- ?????? ?????? ?????? ????????? ?????? ??????:" + (end - start)/1000.0 +"---------------------------------");


            }
        });

        RecyclerView recyclerView = findViewById(R.id.rcy_todolist);
        toDoAdapter = new ToDoAdapter(this, todoList,0);
        recyclerView.setAdapter(toDoAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        toDoAdapter.notifyDataSetChanged();
        long end = System.currentTimeMillis();

        System.out.println("--------------------------------- ?????? ?????? ?????? ????????? ?????? ??????:" + (end - start)/1000.0 +"---------------------------------");


        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);


    }

    @Override
    protected void onResume() {
        super.onResume();
        ToDoManagement toDoManagement = new ToDoManagement();
        todoList = toDoManagement.getToDoList(sdate);
        toDoAdapter.setList(todoList);
        toDoAdapter.notifyDataSetChanged();
    }


}
