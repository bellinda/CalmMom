package com.angelova.w510.calmmom.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.models.Examination;
import com.angelova.w510.calmmom.models.ExaminationStatus;
import com.angelova.w510.calmmom.models.Measurement;
import com.angelova.w510.calmmom.models.Test;
import com.angelova.w510.calmmom.models.UserActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PregnancyUtils {

    public static HashMap<String, List<UserActivity>> getInitialUserActivities() {
        HashMap<String, List<UserActivity>> activities = new HashMap<>();
        for (int i = 1; i <= 40; i++) {
            activities.put(Integer.toString(i), new ArrayList<UserActivity>());
        }
        return activities;
    }

    public static List<Examination> getListWithMainExaminations(Context activity) {
        Resources bgResources = getLocalizedResources(activity, new Locale("bg"));
        Resources enResources = getLocalizedResources(activity, new Locale("en"));

        List<Examination> examinations = new ArrayList<>();

        List<Measurement> activities = Arrays.asList(new Measurement(bgResources.getString(R.string.first_examination_act6), enResources.getString(R.string.first_examination_act6)));

        Examination afterTheDelayEx = new Examination(enResources.getString(R.string.ten_days_exaination_title), bgResources.getString(R.string.ten_days_exaination_title), "", ExaminationStatus.FUTURE, new ArrayList<Test>(), activities);
        examinations.add(afterTheDelayEx);

        Examination seventhWeekEx = new Examination(enResources.getString(R.string.seventh_week_examination_title), bgResources.getString(R.string.seventh_week_examination_title), "", ExaminationStatus.FUTURE, new ArrayList<Test>(), activities);
        examinations.add(seventhWeekEx);

        List<Test> tests = Arrays.asList(new Test(bgResources.getString(R.string.first_examination_test1), enResources.getString(R.string.first_examination_test1)),
                new Test(bgResources.getString(R.string.first_examination_test2), enResources.getString(R.string.first_examination_test2)),
                new Test(bgResources.getString(R.string.first_examination_test3), enResources.getString(R.string.first_examination_test3)),
                new Test(bgResources.getString(R.string.first_examination_test4), enResources.getString(R.string.first_examination_test4)),
                new Test(bgResources.getString(R.string.first_examination_test5), enResources.getString(R.string.first_examination_test5)),
                new Test(bgResources.getString(R.string.first_examination_test6), enResources.getString(R.string.first_examination_test6)),
                new Test(bgResources.getString(R.string.first_examination_test7), enResources.getString(R.string.first_examination_test7)),
                new Test(bgResources.getString(R.string.first_examination_test8), enResources.getString(R.string.first_examination_test8)));
        activities = Arrays.asList(new Measurement(bgResources.getString(R.string.first_examination_act1), enResources.getString(R.string.first_examination_act1)),
                new Measurement(bgResources.getString(R.string.first_examination_act2), enResources.getString(R.string.first_examination_act2)),
                new Measurement(bgResources.getString(R.string.first_examination_act3), enResources.getString(R.string.first_examination_act3)),
                new Measurement(bgResources.getString(R.string.first_examination_act4), enResources.getString(R.string.first_examination_act4)),
                new Measurement(bgResources.getString(R.string.first_examination_act5), enResources.getString(R.string.first_examination_act5)),
                new Measurement(bgResources.getString(R.string.first_examination_act6), enResources.getString(R.string.first_examination_act6)),
                new Measurement(bgResources.getString(R.string.first_examination_act7), enResources.getString(R.string.first_examination_act7)));

        Examination firstEx = new Examination(enResources.getString(R.string.first_examination_title), bgResources.getString(R.string.first_examination_title), "", ExaminationStatus.FUTURE, tests, activities);
        examinations.add(firstEx);

        tests = Arrays.asList(new Test(bgResources.getString(R.string.second_examination_test1), enResources.getString(R.string.second_examination_test1)),
                new Test(bgResources.getString(R.string.second_examination_test2), enResources.getString(R.string.second_examination_test2)));
        activities = Arrays.asList(new Measurement(bgResources.getString(R.string.second_examination_act1), enResources.getString(R.string.second_examination_act1)),
                new Measurement(bgResources.getString(R.string.second_examination_act2), enResources.getString(R.string.second_examination_act2)),
                new Measurement(bgResources.getString(R.string.second_examination_act3), enResources.getString(R.string.second_examination_act3)),
                new Measurement(bgResources.getString(R.string.second_examination_act4), enResources.getString(R.string.second_examination_act4)),
                new Measurement(bgResources.getString(R.string.second_examination_act5), enResources.getString(R.string.second_examination_act5)),
                new Measurement(bgResources.getString(R.string.second_examination_act6), enResources.getString(R.string.second_examination_act6)));
        Examination secondEx = new Examination(enResources.getString(R.string.second_examination_title), bgResources.getString(R.string.second_examination_title), "", ExaminationStatus.FUTURE, tests, activities);
        examinations.add(secondEx);

        activities = Arrays.asList(new Measurement(bgResources.getString(R.string.third_examination_act1), enResources.getString(R.string.third_examination_act1)),
                new Measurement(bgResources.getString(R.string.third_examination_act2), enResources.getString(R.string.third_examination_act2)),
                new Measurement(bgResources.getString(R.string.third_examination_act3), enResources.getString(R.string.third_examination_act3)),
                new Measurement(bgResources.getString(R.string.third_examination_act4), enResources.getString(R.string.third_examination_act4)),
                new Measurement(bgResources.getString(R.string.third_examination_act5), enResources.getString(R.string.third_examination_act5)));
        Examination thirdEx = new Examination(enResources.getString(R.string.third_examination_title), bgResources.getString(R.string.third_examination_title), "", ExaminationStatus.FUTURE, new ArrayList<Test>(), activities);
        examinations.add(thirdEx);

        tests = Arrays.asList(new Test(bgResources.getString(R.string.twenty_first_week_test1), enResources.getString(R.string.twenty_first_week_test1)));
        Examination fetalMorphologyEx = new Examination(enResources.getString(R.string.twenty_first_week_title), bgResources.getString(R.string.twenty_first_week_title), "", ExaminationStatus.FUTURE, tests, new ArrayList<Measurement>());
        examinations.add(fetalMorphologyEx);

        tests = Arrays.asList(new Test(bgResources.getString(R.string.fourth_examination_test1), enResources.getString(R.string.fourth_examination_test1)));
        activities = Arrays.asList(new Measurement(bgResources.getString(R.string.fourth_examination_act1), enResources.getString(R.string.fourth_examination_act1)),
                new Measurement(bgResources.getString(R.string.fourth_examination_act2), enResources.getString(R.string.fourth_examination_act2)),
                new Measurement(bgResources.getString(R.string.fourth_examination_act3), enResources.getString(R.string.fourth_examination_act3)),
                new Measurement(bgResources.getString(R.string.fourth_examination_act4), enResources.getString(R.string.fourth_examination_act4)),
                new Measurement(bgResources.getString(R.string.fourth_examination_act5), enResources.getString(R.string.fourth_examination_act5)),
                new Measurement(bgResources.getString(R.string.fourth_examination_act6), enResources.getString(R.string.fourth_examination_act6)),
                new Measurement(bgResources.getString(R.string.fourth_examination_act7), enResources.getString(R.string.fourth_examination_act7)));
        Examination fourthEx = new Examination(enResources.getString(R.string.fourth_examination_title), bgResources.getString(R.string.fourth_examination_title), "", ExaminationStatus.FUTURE, tests, activities);
        examinations.add(fourthEx);

        activities = Arrays.asList(new Measurement(bgResources.getString(R.string.fifth_examination_act1), enResources.getString(R.string.fifth_examination_act1)),
                new Measurement(bgResources.getString(R.string.fifth_examination_act2), enResources.getString(R.string.fifth_examination_act2)),
                new Measurement(bgResources.getString(R.string.fifth_examination_act3), enResources.getString(R.string.fifth_examination_act3)),
                new Measurement(bgResources.getString(R.string.fifth_examination_act4), enResources.getString(R.string.fifth_examination_act4)),
                new Measurement(bgResources.getString(R.string.fifth_examination_act5), enResources.getString(R.string.fifth_examination_act5)),
                new Measurement(bgResources.getString(R.string.fifth_examination_act6), enResources.getString(R.string.fifth_examination_act6)),
                new Measurement(bgResources.getString(R.string.fifth_examination_act7), enResources.getString(R.string.fifth_examination_act7)));
        Examination fifthEx = new Examination(enResources.getString(R.string.fifth_examination_title), bgResources.getString(R.string.fifth_examination_title), "", ExaminationStatus.FUTURE, new ArrayList<Test>(), activities);
        examinations.add(fifthEx);

        tests = Arrays.asList(new Test(bgResources.getString(R.string.sixth_examination_test1), enResources.getString(R.string.sixth_examination_test1)));
        activities = Arrays.asList(new Measurement(bgResources.getString(R.string.sixth_examination_act1), enResources.getString(R.string.sixth_examination_act1)),
                new Measurement(bgResources.getString(R.string.sixth_examination_act2), enResources.getString(R.string.sixth_examination_act2)),
                new Measurement(bgResources.getString(R.string.sixth_examination_act3), enResources.getString(R.string.sixth_examination_act3)),
                new Measurement(bgResources.getString(R.string.sixth_examination_act4), enResources.getString(R.string.sixth_examination_act4)),
                new Measurement(bgResources.getString(R.string.sixth_examination_act5), enResources.getString(R.string.sixth_examination_act5)),
                new Measurement(bgResources.getString(R.string.sixth_examination_act6), enResources.getString(R.string.sixth_examination_act6)),
                new Measurement(bgResources.getString(R.string.sixth_examination_act7), enResources.getString(R.string.sixth_examination_act7)));
        Examination sixthEx = new Examination(enResources.getString(R.string.sixth_examination_title), bgResources.getString(R.string.sixth_examination_title), "", ExaminationStatus.FUTURE, tests, activities);
        examinations.add(sixthEx);

        tests = Arrays.asList(new Test(bgResources.getString(R.string.seventh_examination_test1), enResources.getString(R.string.seventh_examination_test1)));
        activities = Arrays.asList(new Measurement(bgResources.getString(R.string.seventh_examination_act1), enResources.getString(R.string.seventh_examination_act1)),
                new Measurement(bgResources.getString(R.string.seventh_examination_act2), enResources.getString(R.string.seventh_examination_act2)),
                new Measurement(bgResources.getString(R.string.seventh_examination_act3), enResources.getString(R.string.seventh_examination_act3)),
                new Measurement(bgResources.getString(R.string.seventh_examination_act4), enResources.getString(R.string.seventh_examination_act4)),
                new Measurement(bgResources.getString(R.string.seventh_examination_act5), enResources.getString(R.string.seventh_examination_act5)),
                new Measurement(bgResources.getString(R.string.seventh_examination_act6), enResources.getString(R.string.seventh_examination_act6)),
                new Measurement(bgResources.getString(R.string.seventh_examination_act7), enResources.getString(R.string.seventh_examination_act7)),
                new Measurement(bgResources.getString(R.string.seventh_examination_act8), enResources.getString(R.string.seventh_examination_act8)));
        Examination seventhEx = new Examination(enResources.getString(R.string.seventh_examination_title), bgResources.getString(R.string.seventh_examination_title), "", ExaminationStatus.FUTURE, tests, activities);
        examinations.add(seventhEx);

        tests = Arrays.asList(new Test(bgResources.getString(R.string.seventh_1_examination_test1), enResources.getString(R.string.seventh_1_examination_test1)));
        Examination seventh2Ex = new Examination(enResources.getString(R.string.seventh_1_examination_title), bgResources.getString(R.string.seventh_1_examination_title), "", ExaminationStatus.FUTURE, tests, activities);
        examinations.add(seventh2Ex);

        activities = Arrays.asList(new Measurement(bgResources.getString(R.string.eighth_examination_act1), enResources.getString(R.string.eighth_examination_act1)),
                new Measurement(bgResources.getString(R.string.eighth_examination_act2), enResources.getString(R.string.eighth_examination_act2)),
                new Measurement(bgResources.getString(R.string.eighth_examination_act3), enResources.getString(R.string.eighth_examination_act3)),
                new Measurement(bgResources.getString(R.string.eighth_examination_act4), enResources.getString(R.string.eighth_examination_act4)),
                new Measurement(bgResources.getString(R.string.eighth_examination_act5), enResources.getString(R.string.eighth_examination_act5)),
                new Measurement(bgResources.getString(R.string.eighth_examination_act6), enResources.getString(R.string.eighth_examination_act6)),
                new Measurement(bgResources.getString(R.string.eighth_examination_act7), enResources.getString(R.string.eighth_examination_act7)),
                new Measurement(bgResources.getString(R.string.eighth_examination_act8), enResources.getString(R.string.eighth_examination_act8)));
        Examination eighthEx = new Examination(enResources.getString(R.string.eight_examination_title), bgResources.getString(R.string.eight_examination_title), "", ExaminationStatus.FUTURE, new ArrayList<Test>(), activities);
        examinations.add(eighthEx);

        Examination eighth2Ex = new Examination(enResources.getString(R.string.ninth_examination_title), bgResources.getString(R.string.ninth_examination_title), "", ExaminationStatus.FUTURE, new ArrayList<Test>(), activities);
        examinations.add(eighth2Ex);

        return examinations;
    }

    @NonNull
    private static Resources getLocalizedResources(Context context, Locale desiredLocale) {
        Configuration conf = context.getResources().getConfiguration();
        conf = new Configuration(conf);
        conf.setLocale(desiredLocale);
        Context localizedContext = context.createConfigurationContext(conf);
        return localizedContext.getResources();
    }
}
