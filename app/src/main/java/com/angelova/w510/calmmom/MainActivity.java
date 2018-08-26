package com.angelova.w510.calmmom;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.angelova.w510.calmmom.models.BabySize;
import com.angelova.w510.calmmom.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.melnykov.fab.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mExaminationsItem;
    private LinearLayout mHealthStateItem;
    private LinearLayout mTipsItem;
    private LinearLayout mKicksAndContractionsItem;

    private TextView mCurrentDateView;
    private TextView mCurrentWeekView;
    private TextView mBabySizeView;

    private FloatingActionButton mProfileBtn;

    private User mUser;
    private BabySize mCurrentBabySize;
    private int mCurrentPregnancyWeek;
    private FirebaseFirestore mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#324A5F"));
        }

        mUser = (User) getIntent().getSerializableExtra("user");
        mDb = FirebaseFirestore.getInstance();

        mCurrentDateView = (TextView) findViewById(R.id.current_date_view);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        mCurrentDateView.setText(sdf.format(Calendar.getInstance().getTime()));

        mCurrentWeekView = (TextView) findViewById(R.id.current_week_view);
        mCurrentPregnancyWeek = (int) (getDaysSinceDate(mUser.getFirstDayOfLastMenstruation()) / 7 + 1);
        mCurrentWeekView.setText(String.format("%d %s", mCurrentPregnancyWeek, getString(R.string.main_activity_weeks)));

        getSizes();
        mBabySizeView = (TextView) findViewById(R.id.baby_size_view);

        mExaminationsItem = (LinearLayout) findViewById(R.id.examinations_layout);

        mExaminationsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExaminationsActivity.class);
                intent.putExtra("email", getIntent().getStringExtra("email"));
                startActivity(intent);
                //finish();
            }
        });

        mHealthStateItem = (LinearLayout) findViewById(R.id.health_state_layout);

        mHealthStateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HealthStateActivity.class);
                intent.putExtra("email", getIntent().getStringExtra("email"));
                startActivity(intent);
            }
        });

        mTipsItem = (LinearLayout) findViewById(R.id.tip_layout);

        mTipsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TipsActivity.class);
                intent.putExtra("user", getIntent().getSerializableExtra("user"));
                //intent.putExtra("email", getIntent().getStringExtra("email"));
                startActivity(intent);
            }
        });

        mKicksAndContractionsItem = (LinearLayout) findViewById(R.id.kicks_contractions_layout);

        mKicksAndContractionsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, KicksAndContractionsActivity.class);
                intent.putExtra("user", getIntent().getSerializableExtra("user"));
                intent.putExtra("email", getIntent().getStringExtra("email"));
                startActivity(intent);
            }
        });

        mProfileBtn = (FloatingActionButton) findViewById(R.id.profile_btn);
        mProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private long getDaysSinceDate(String startDate) {
        long daysDiff = 0;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(startDate));
            Calendar cal1 = Calendar.getInstance();
            String formatted = sdf.format(cal1.getTime());
            cal1.setTime(sdf.parse(formatted));

            long msDiff = cal1.getTimeInMillis() - cal.getTimeInMillis();
            daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return daysDiff;
    }

    private void getSizes() {
        mDb.collection("sizes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ObjectMapper mapper = new ObjectMapper();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> sizes = document.getData();
                        mCurrentBabySize = mapper.convertValue(sizes.get(Integer.toString(mCurrentPregnancyWeek)), BabySize.class);
                    }
                    mBabySizeView.setText(String.format(getString(R.string.main_activity_size_text), Float.toString(mCurrentBabySize.getWeight()), Float.toString(mCurrentBabySize.getLength())));
                }
            }
        });
    }
}
