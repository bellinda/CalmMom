package com.angelova.w510.calmmom.models;

import android.support.annotation.NonNull;

import com.angelova.w510.calmmom.utils.DateTimeUtils;

import org.apache.commons.lang3.LocaleUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by W510 on 26.8.2018 г..
 */

public class Kick implements Serializable, Comparable<Kick> {
    private String date;
    private String duration;
    private int count;

    public Kick (String date, String duration, int count) {
        this.date = date;
        this.duration = duration;
        this.count = count;
    }

    public Kick () {}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int compareTo(@NonNull Kick kick) {
        SimpleDateFormat sdfEn = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        SimpleDateFormat sdf2En = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
        SimpleDateFormat sdfBg = new SimpleDateFormat("dd MMM yyyy", LocaleUtils.toLocale("bg"));
        SimpleDateFormat sdf2Bg = new SimpleDateFormat("dd.MM.yyyy", LocaleUtils.toLocale("bg"));

        try {
            Date currentItemDate;
            Date otherItemDate;
            if (DateTimeUtils.isDateInEn(this.getDate())) {
                currentItemDate = sdf2En.parse(sdf2En.format(sdfEn.parse(this.getDate())));
            } else {
                currentItemDate = sdf2Bg.parse(sdf2Bg.format(sdfBg.parse(this.getDate())));
            }
            if (DateTimeUtils.isDateInEn(kick.getDate())) {
                otherItemDate = sdf2En.parse(sdf2En.format(sdfEn.parse(kick.getDate())));
            } else {
                otherItemDate = sdf2Bg.parse(sdf2Bg.format(sdfBg.parse(kick.getDate())));
            }

            if (currentItemDate.after(otherItemDate)) {
                return -1;
            } else if (currentItemDate.before(otherItemDate)) {
                return 1;
            }
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return 0;
    }
}
