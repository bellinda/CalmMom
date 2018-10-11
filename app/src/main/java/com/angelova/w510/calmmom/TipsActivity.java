package com.angelova.w510.calmmom;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.angelova.w510.calmmom.adapters.TipsAdapter;
import com.angelova.w510.calmmom.dialogs.LoadingDialog;
import com.angelova.w510.calmmom.models.Tip;
import com.angelova.w510.calmmom.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TipsActivity extends AppCompatActivity {

    private User mUser;
    private String mUserEmail;
    private List<Tip> mTips = new ArrayList<>();
    private TipsAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mLoader;

    private FirebaseFirestore mDb;
    int currentPregnancyWeek = 0;

    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#324A5F"));
        }

        mUser = (User) getIntent().getSerializableExtra("user");
        mUserEmail = getIntent().getStringExtra("email");
        currentPregnancyWeek = (int) (getDaysSinceDate(mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).getFirstDayOfLastMenstruation()) / 7 + 1);
        mDb = FirebaseFirestore.getInstance();

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPrefs.edit();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        mLoader = (ProgressBar) findViewById(R.id.loader);

        getTips();
    }

    private void getTips() {
        mDb.collection("tips").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ObjectMapper mapper = new ObjectMapper();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> tipsFromDB = document.getData();
                        for (String key : tipsFromDB.keySet()) {
                            Tip tip = mapper.convertValue(tipsFromDB.get(key), Tip.class);
                            if (Integer.parseInt(tip.getWeek()) <= currentPregnancyWeek) {
                                mTips.add(tip);
                            }
                        }
                    }
                    Collections.sort(mTips);
                    if (mUser.getCustomTips() != null) {
                        mTips.addAll(0, mUser.getCustomTips());
                    }
                    mAdapter = new TipsAdapter(mTips, TipsActivity.this);
                    mRecyclerView.setAdapter(mAdapter);
                    mLoader.setVisibility(View.GONE);
                }
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

    public void updateTipsInDb(List<Tip> tips) {
        final LoadingDialog dialog = new LoadingDialog(TipsActivity.this, getString(R.string.tips_loading_message));
        dialog.show();
        ObjectMapper m = new ObjectMapper();
        mUser.setCustomTips(tips);
        Map<String,Object> user = m.convertValue(mUser, Map.class);

        final DocumentReference userRef = mDb.collection("users").document(mUserEmail);
        userRef.update(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("DocumentSnapshot successfully updated!");
                        mEditor.putBoolean("shouldReload", true).commit();
                        dialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error updating document " + e);
                    }
                });
    }
}
