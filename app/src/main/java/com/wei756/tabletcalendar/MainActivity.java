package com.wei756.tabletcalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    int[] monthDate = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    String[] weekName = {"일", "월", "화", "수", "목", "금", "토"};

    RecyclerView mRecyclerView;
    CalendarAdapter mAdapter;

    int year, month, date; // 년월일
    int hour, minute, second; // 시분초
    int day; // 요일

    Map<String, CalendarEvent> eventMap = new HashMap<>();

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent i) {
            int level = i.getIntExtra("level", 0);
            TextView tvBattery = (TextView) findViewById(R.id.tv_battery_status);
            tvBattery.setText(level + "%");
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // fullscreen
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // battery status
        registerReceiver(mBatInfoReceiver, new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED));

        // recyclerview
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_calendar);

        ArrayList<Item> list = new ArrayList<>();
        mAdapter = new CalendarAdapter(list, this, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        // google calendar sync
        Button btnGoogleCalendar = (Button) findViewById(R.id.btn_googlecalendar);
        btnGoogleCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GoogleCalendarActivity.class);
                intent.putExtra("year", year);
                intent.putExtra("type", GoogleCalendarActivity.TYPE_HOLIDAY);

                startActivityForResult(intent, 100);
            }
        });


        // 달력 업데이트
        updateCalendar();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) { // 공휴일 등 이벤트 목록 로드
            String type = data.getStringExtra("type");
            int year = data.getIntExtra("year", 0);
            ArrayList<String> events = data.getStringArrayListExtra("events");
            for (String eventStr : events) {
                String[] eventStrArray = eventStr.split("\n");
                String name = eventStrArray[0];
                String date = eventStrArray[1];
                date = date.substring(2, date.length() - 1);
                Log.e("events", "name:" + name);
                Log.e("events", "date:" + date);
                String[] dateArray = date.split("-");
                eventMap.put(date,
                        new CalendarEvent()
                                .setDate(
                                        Integer.parseInt(dateArray[0]),
                                        Integer.parseInt(dateArray[1]),
                                        Integer.parseInt(dateArray[2]))
                                .setName(name)
                                .setType(CalendarEvent.HOLIDAY));
            }
            Log.e("MainActivity", eventMap.size() + "개의 이벤트를 불러왔습니다.");
            updateCalendar();
        }
    }

    void updateCalendar() {
        ArrayList<Item> list = new ArrayList<>();

        // time update
        updateCurrentTime();
        printCurrentTime();
        //date = 1;
        //month = 1;
        //year = 2020;
        //day = 3;
        int firstDay = (day - date + 1 + 35) % 7; // 위 공백
        for (int i = 0; i < firstDay; i++)
            list.add(new Date().setDate(0));
        for (int i = 1; i <= monthDate[month - 1]; i++) { // 실제 날짜
            Date date = new Date();

            date.setDate(i);
            date.setToday(this.date == i);

            if (eventMap.containsKey(this.year + "-" + this.month + "-" + i)) { // 해당 날짜 이벤트가 있을 경우
                date.putEvent(eventMap.get(this.year + "-" + this.month + "-" + i));

            }

            list.add(date);
        }
        int lastday = (7 - (list.size() % 7)) % 7;
        for (int i = 0; i < lastday; i++) // 아래 공백
            list.add(new Date().setDate(0));
        mAdapter.setListWith(list, this);
        // 월
        TextView tvMonth = (TextView) findViewById(R.id.tv_month);
        tvMonth.setText(month + "월");


        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mAdapter.height = mRecyclerView.getHeight();
                Log.e("CalendarAdapter", "height: " + mAdapter.height);
                mAdapter.updateLayout(MainActivity.this);
            }
        });
    }

    void updateCurrentTime() {
        Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        date = calendar.get(Calendar.DATE);
        day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);
        second = calendar.get(Calendar.SECOND);
    }

    void printCurrentTime() {
        Log.e("Time", year + "/" + month + "/" + date + "(" + weekName[day] + ") " + hour + ":" + minute + ":" + second);
    }
}
