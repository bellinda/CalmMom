package com.angelova.w510.calmmom.utils;

import android.content.Context;

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

public class PregnancyUtils {

    public static HashMap<String, List<UserActivity>> getInitialUserActivities() {
        HashMap<String, List<UserActivity>> activities = new HashMap<>();
        for (int i = 1; i <= 40; i++) {
            activities.put(Integer.toString(i), new ArrayList<UserActivity>());
        }
        return activities;
    }

    public static List<Examination> getListWithMainExaminations(Context activity) {
        List<Examination> examinations = new ArrayList<>();

        List<Measurement> activities = Arrays.asList(new Measurement(activity.getString(R.string.first_examination_act6)));

        Examination afterTheDelayEx = new Examination(activity.getString(R.string.ten_days_exaination_title), "", ExaminationStatus.CURRENT, new ArrayList<Test>(), activities);
        examinations.add(afterTheDelayEx);

        Examination seventhWeekEx = new Examination(activity.getString(R.string.seventh_week_examination_title), "", ExaminationStatus.FUTURE, new ArrayList<Test>(), activities);
        examinations.add(seventhWeekEx);

        List<Test> tests = Arrays.asList(new Test(activity.getString(R.string.first_examination_test1)), new Test(activity.getString(R.string.first_examination_test2)),
                new Test(activity.getString(R.string.first_examination_test3)), new Test(activity.getString(R.string.first_examination_test4)),
                new Test(activity.getString(R.string.first_examination_test5)), new Test(activity.getString(R.string.first_examination_test6)),
                new Test(activity.getString(R.string.first_examination_test7)), new Test(activity.getString(R.string.first_examination_test8)));
        activities = Arrays.asList(new Measurement(activity.getString(R.string.first_examination_act1)), new Measurement(activity.getString(R.string.first_examination_act2)),
                new Measurement(activity.getString(R.string.first_examination_act3)), new Measurement(activity.getString(R.string.first_examination_act4)),
                new Measurement(activity.getString(R.string.first_examination_act5)), new Measurement(activity.getString(R.string.first_examination_act6)), new Measurement(activity.getString(R.string.first_examination_act7)));

        Examination firstEx = new Examination("12th week", "", ExaminationStatus.FUTURE, tests, activities);
        examinations.add(firstEx);

        tests = Arrays.asList(new Test(activity.getString(R.string.second_examination_test1)), new Test(activity.getString(R.string.second_examination_test2)));
        activities = Arrays.asList(new Measurement(activity.getString(R.string.second_examination_act1)), new Measurement(activity.getString(R.string.second_examination_act2)),
                new Measurement(activity.getString(R.string.second_examination_act3)), new Measurement(activity.getString(R.string.second_examination_act4)), new Measurement(activity.getString(R.string.second_examination_act5)),
                new Measurement(activity.getString(R.string.second_examination_act6)));
        Examination secondEx = new Examination("16th week", "", ExaminationStatus.FUTURE, tests, activities);
        examinations.add(secondEx);

        activities = Arrays.asList(new Measurement(activity.getString(R.string.third_examination_act1)), new Measurement(activity.getString(R.string.third_examination_act2)), new Measurement(activity.getString(R.string.third_examination_act3)),
                new Measurement(activity.getString(R.string.third_examination_act4)), new Measurement(activity.getString(R.string.third_examination_act5)));
        Examination thirdEx = new Examination("20th week", "", ExaminationStatus.FUTURE, new ArrayList<Test>(), activities);
        examinations.add(thirdEx);

        tests = Arrays.asList(new Test(activity.getString(R.string.twenty_first_week_test1)));
        Examination fetalMorphologyEx = new Examination("21st week - Fetal morphology", "", ExaminationStatus.FUTURE, tests, new ArrayList<Measurement>());
        examinations.add(fetalMorphologyEx);

        tests = Arrays.asList(new Test(activity.getString(R.string.fourth_examination_test1)));
        activities = Arrays.asList(new Measurement(activity.getString(R.string.fourth_examination_act1)), new Measurement(activity.getString(R.string.fourth_examination_act2)), new Measurement(activity.getString(R.string.fourth_examination_act3)),
                new Measurement(activity.getString(R.string.fourth_examination_act4)), new Measurement(activity.getString(R.string.fourth_examination_act5)), new Measurement(activity.getString(R.string.fourth_examination_act6)),
                new Measurement(activity.getString(R.string.fourth_examination_act7)));
        Examination fourthEx = new Examination("24th week", "", ExaminationStatus.FUTURE, tests, activities);
        examinations.add(fourthEx);

        activities = Arrays.asList(new Measurement(activity.getString(R.string.fifth_examination_act1)), new Measurement(activity.getString(R.string.fifth_examination_act2)), new Measurement(activity.getString(R.string.fifth_examination_act3)),
                new Measurement(activity.getString(R.string.fifth_examination_act4)), new Measurement(activity.getString(R.string.fifth_examination_act5)), new Measurement(activity.getString(R.string.fifth_examination_act6)),
                new Measurement(activity.getString(R.string.fifth_examination_act7)));
        Examination fifthEx = new Examination("28th week", "", ExaminationStatus.FUTURE, new ArrayList<Test>(), activities);
        examinations.add(fifthEx);

        tests = Arrays.asList(new Test(activity.getString(R.string.sixth_examination_test1)));
        activities = Arrays.asList(new Measurement(activity.getString(R.string.sixth_examination_act1)), new Measurement(activity.getString(R.string.sixth_examination_act2)), new Measurement(activity.getString(R.string.sixth_examination_act3)),
                new Measurement(activity.getString(R.string.sixth_examination_act4)), new Measurement(activity.getString(R.string.sixth_examination_act5)), new Measurement(activity.getString(R.string.sixth_examination_act6)),
                new Measurement(activity.getString(R.string.sixth_examination_act7)));
        Examination sixthEx = new Examination("32nd week", "", ExaminationStatus.FUTURE, tests, activities);
        examinations.add(sixthEx);

        tests = Arrays.asList(new Test(activity.getString(R.string.seventh_examination_test1)));
        activities = Arrays.asList(new Measurement(activity.getString(R.string.seventh_examination_act1)), new Measurement(activity.getString(R.string.seventh_examination_act2)),
                new Measurement(activity.getString(R.string.seventh_examination_act3)), new Measurement(activity.getString(R.string.seventh_examination_act4)), new Measurement(activity.getString(R.string.seventh_examination_act5)),
                new Measurement(activity.getString(R.string.seventh_examination_act6)), new Measurement(activity.getString(R.string.seventh_examination_act7)), new Measurement(activity.getString(R.string.seventh_examination_act8)));
        Examination seventhEx = new Examination("34th week", "", ExaminationStatus.FUTURE, tests, activities);
        examinations.add(seventhEx);

        tests = Arrays.asList(new Test(activity.getString(R.string.seventh_1_examination_test1)));
        Examination seventh2Ex = new Examination("36th week", "", ExaminationStatus.FUTURE, tests, activities);
        examinations.add(seventh2Ex);

        activities = Arrays.asList(new Measurement(activity.getString(R.string.eighth_examination_act1)), new Measurement(activity.getString(R.string.eighth_examination_act2)),
                new Measurement(activity.getString(R.string.eighth_examination_act3)), new Measurement(activity.getString(R.string.eighth_examination_act4)), new Measurement(activity.getString(R.string.eighth_examination_act5)),
                new Measurement(activity.getString(R.string.eighth_examination_act6)), new Measurement(activity.getString(R.string.eighth_examination_act7)), new Measurement(activity.getString(R.string.eighth_examination_act8)));
        Examination eighthEx = new Examination("38th week", "", ExaminationStatus.FUTURE, new ArrayList<Test>(), activities);
        examinations.add(eighthEx);

        Examination eighth2Ex = new Examination("39th week", "", ExaminationStatus.FUTURE, new ArrayList<Test>(), activities);
        examinations.add(eighth2Ex);

        return examinations;
    }
}
