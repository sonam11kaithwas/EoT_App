package com.eot_app.nav_menu.appointment.calendar.data;

public class Event {
    private final int mYear;
    private final int mMonth;
    private final int mDay;
    private int mColor;

    public Event(int year, int month, int day) {
        this.mYear = year;
        this.mMonth = month;
        this.mDay = day;
    }

    public Event(int year, int month, int day, int color) {
        this.mYear = year;
        this.mMonth = month;
        this.mDay = day;
        this.mColor = color;
    }

    public int getMonth() {
        return mMonth;
    }

    public int getYear() {
        return mYear;
    }

    public int getDay() {
        return mDay;
    }

    public int getColor() {
        return mColor;
    }

    public void setmColor(int mColor) {
        this.mColor = mColor;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        return this.getYear() + " " + this.getMonth() + " " + this.getDay() == ((Event) obj).getYear() + " " + getMonth() + " " + getDay();
    }
}
