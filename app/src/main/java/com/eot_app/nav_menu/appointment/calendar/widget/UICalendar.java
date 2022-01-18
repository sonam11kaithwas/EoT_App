package com.eot_app.nav_menu.appointment.calendar.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.eot_app.R;
import com.eot_app.nav_menu.appointment.calendar.data.Day;
import com.eot_app.nav_menu.appointment.calendar.view.ExpandIconView;
import com.eot_app.nav_menu.appointment.calendar.view.LockScrollView;
import com.eot_app.nav_menu.appointment.calendar.view.OnSwipeTouchListener;

@SuppressLint("ClickableViewAccessibility")
public abstract class UICalendar extends LinearLayout {


    // Day of Week
    public static final int SUNDAY = 0;
    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;
    // State
    public static final int STATE_EXPANDED = 0;
    public static final int STATE_COLLAPSED = 1;
    public static final int STATE_PROCESSING = 2;
    public ExpandIconView expandIconView;
    protected Context mContext;
    protected LayoutInflater mInflater;
    // UI
    protected LinearLayout mLayoutRoot;
    protected TextView mTxtTitle;
    protected TableLayout mTableHead;
    protected LockScrollView mScrollViewBody;
    protected LockScrollView scroll_view_title;
    protected TableLayout mTableBody;
    protected RelativeLayout mLayoutBtnGroupMonth;
    protected RelativeLayout mLayoutBtnGroupWeek;
    protected ImageView mBtnPrevMonth;
    protected ImageView mBtnNextMonth;
    protected ImageView mBtnPrevWeek;
    protected ImageView mBtnNextWeek;
    // Attributes
    private boolean mShowWeek = true;
    //private int mFirstDayOfWeek = SUNDAY;
    private int mFirstDayOfWeek = MONDAY;
    private int mState = STATE_COLLAPSED;

    private int mTextColor = Color.BLACK;
    private int mPrimaryColor = Color.WHITE;

    private int mTodayItemTextColor = Color.BLACK;
    private Drawable mTodayItemBackgroundDrawable =
            getResources().getDrawable(R.drawable.circle_black_stroke_background);
    private int mSelectedItemTextColor = Color.WHITE;
    private Drawable mSelectedItemBackgroundDrawable =
            getResources().getDrawable(R.drawable.circle_black_solid_background);

    private Drawable mButtonLeftDrawable =
            getResources().getDrawable(R.drawable.ic_arrow_right);
    private Drawable mButtonRightDrawable =
            getResources().getDrawable(R.drawable.ic_arrow_right);

    private Day mSelectedItem = null;

    private int mButtonLeftDrawableTintColor = Color.BLACK;
    private int mButtonRightDrawableTintColor = Color.BLACK;

    private int mExpandIconColor = Color.BLACK;
    private int mEventColor = Color.BLACK;

    public UICalendar(Context context) {
        this(context, null);
    }

    public UICalendar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UICalendar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
        TypedArray attributes = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.UICalendar, defStyleAttr, 0);
        setAttributes(attributes);
        attributes.recycle();
    }

    protected abstract void redraw();

    protected abstract void reload();

    protected void init(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);

        // load rootView from xml
        View rootView = mInflater.inflate(R.layout.widget_collapsible_calendarview, this, true);

        // init UI
        mLayoutRoot = rootView.findViewById(R.id.layout_root);
        mTxtTitle = rootView.findViewById(R.id.txt_title);

        mTableHead = rootView.findViewById(R.id.table_head);
        mScrollViewBody = rootView.findViewById(R.id.scroll_view_body);
        scroll_view_title = rootView.findViewById(R.id.scroll_view_title);
        mTableBody = rootView.findViewById(R.id.table_body);
        mLayoutBtnGroupMonth = rootView.findViewById(R.id.layout_btn_group_month);
        mLayoutBtnGroupWeek = rootView.findViewById(R.id.layout_btn_group_week);
        mBtnPrevMonth = rootView.findViewById(R.id.btn_prev_month);
        mBtnNextMonth = rootView.findViewById(R.id.btn_next_month);
        mBtnPrevWeek = rootView.findViewById(R.id.btn_prev_week);
        mBtnNextWeek = rootView.findViewById(R.id.btn_next_week);
        expandIconView = rootView.findViewById(R.id.expandIcon);

        mLayoutRoot.setOnTouchListener(getSwipe(context));
        mScrollViewBody.setOnTouchListener(getSwipe(context));
        mScrollViewBody.setOnSwipeTouchListener(getSwipe(context));
        scroll_view_title.setOnTouchListener(getSwipe(context));
        scroll_view_title.setOnSwipeTouchListener(getSwipe(context));


    }

    protected void setAttributes(TypedArray attrs) {
        // set attributes by the values from XML
        //setStyle(attrs.getInt(R.styleable.UICalendar_style, mStyle));
        setShowWeek(attrs.getBoolean(R.styleable.UICalendar_showWeek, mShowWeek));
        setFirstDayOfWeek(attrs.getInt(R.styleable.UICalendar_firstDayOfWeek, mFirstDayOfWeek));
        setState(attrs.getInt(R.styleable.UICalendar_state, mState));

        setTextColor(attrs.getColor(R.styleable.UICalendar_textColor, mTextColor));
        setPrimaryColor(attrs.getColor(R.styleable.UICalendar_primaryColor, mPrimaryColor));

        setEventColor(attrs.getColor(R.styleable.UICalendar_eventColor, mEventColor));


        setTodayItemTextColor(attrs.getColor(
                R.styleable.UICalendar_todayItem_textColor, mTodayItemTextColor));
        Drawable todayItemBackgroundDrawable =
                attrs.getDrawable(R.styleable.UICalendar_todayItem_background);
        if (todayItemBackgroundDrawable != null) {
            setTodayItemBackgroundDrawable(todayItemBackgroundDrawable);
        } else {
            setTodayItemBackgroundDrawable(mTodayItemBackgroundDrawable);
        }

        setSelectedItemTextColor(attrs.getColor(
                R.styleable.UICalendar_selectedItem_textColor, mSelectedItemTextColor));
        Drawable selectedItemBackgroundDrawable =
                attrs.getDrawable(R.styleable.UICalendar_selectedItem_background);
        if (selectedItemBackgroundDrawable != null) {
            setSelectedItemBackgroundDrawable(selectedItemBackgroundDrawable);
        } else {
            setSelectedItemBackgroundDrawable(mSelectedItemBackgroundDrawable);
        }

        Drawable buttonLeftDrawable =
                attrs.getDrawable(R.styleable.UICalendar_buttonLeft_drawable);
        if (buttonLeftDrawable != null) {
            setButtonLeftDrawable(buttonLeftDrawable);
        } else {
            setButtonLeftDrawable(mButtonLeftDrawable);
        }

        Drawable buttonRightDrawable =
                attrs.getDrawable(R.styleable.UICalendar_buttonRight_drawable);
        if (buttonRightDrawable != null) {
            setButtonRightDrawable(buttonRightDrawable);
        } else {
            setButtonRightDrawable(mButtonRightDrawable);
        }

        setButtonLeftDrawableTintColor(attrs.getColor(R.styleable.UICalendar_buttonLeft_drawableTintColor, mButtonLeftDrawableTintColor));
        setButtonRightDrawableTintColor(attrs.getColor(R.styleable.UICalendar_buttonRight_drawableTintColor, mButtonRightDrawableTintColor));
        setExpandIconColor(attrs.getColor(R.styleable.UICalendar_expandIconColor, mExpandIconColor));
        Day selectedItem = null;
    }

    public void setButtonLeftDrawableTintColor(int color) {
        this.mButtonLeftDrawableTintColor = color;
        mBtnPrevMonth.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        mBtnPrevWeek.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        redraw();
    }

    public void setButtonRightDrawableTintColor(int color) {
        this.mButtonRightDrawableTintColor = color;
        mBtnNextMonth.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        mBtnNextWeek.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        redraw();
    }

    public void setExpandIconColor(int color) {
        this.mExpandIconColor = color;
        expandIconView.setColor(color);
    }

    public boolean isShowWeek() {
        return mShowWeek;
    }

    public void setShowWeek(boolean showWeek) {
        this.mShowWeek = showWeek;

        if (showWeek) {
            mTableHead.setVisibility(VISIBLE);
        } else {
            mTableHead.setVisibility(GONE);
        }
    }

    public int getFirstDayOfWeek() {
        return mFirstDayOfWeek;
    }

    public void setFirstDayOfWeek(int firstDayOfWeek) {
        this.mFirstDayOfWeek = firstDayOfWeek;
        reload();
    }


    public int getState() {
        return mState;
    }

    public void setState(int state) {
        this.mState = state;

        if (mState == STATE_EXPANDED) {
            mLayoutBtnGroupMonth.setVisibility(VISIBLE);
            mLayoutBtnGroupWeek.setVisibility(GONE);
        }
        if (mState == STATE_COLLAPSED) {
            mLayoutBtnGroupMonth.setVisibility(GONE);
            mLayoutBtnGroupWeek.setVisibility(VISIBLE);
        }
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
        redraw();

        mTxtTitle.setTextColor(mTextColor);
    }

    public String getTitle() {
        return mTxtTitle.getText().toString();
    }

    public int getPrimaryColor() {
        return mPrimaryColor;
    }

    public void setPrimaryColor(int primaryColor) {
        this.mPrimaryColor = primaryColor;
        redraw();

        mLayoutRoot.setBackgroundColor(mPrimaryColor);
    }

    public int getEventColor() {
        return mEventColor;
    }

    private void setEventColor(int eventColor) {
        this.mEventColor = eventColor;
        redraw();

    }

    public int getTodayItemTextColor() {
        return mTodayItemTextColor;
    }

    public void setTodayItemTextColor(int todayItemTextColor) {
        this.mTodayItemTextColor = todayItemTextColor;
        redraw();
    }

    public Drawable getTodayItemBackgroundDrawable() {
        return mTodayItemBackgroundDrawable;
    }

    public void setTodayItemBackgroundDrawable(Drawable todayItemBackgroundDrawable) {
        this.mTodayItemBackgroundDrawable = todayItemBackgroundDrawable;
        redraw();
    }

    public int getSelectedItemTextColor() {
        return mSelectedItemTextColor;
    }

    public void setSelectedItemTextColor(int selectedItemTextColor) {
        this.mSelectedItemTextColor = selectedItemTextColor;
        redraw();
    }

    public Drawable getSelectedItemBackgroundDrawable() {
        return mSelectedItemBackgroundDrawable;
    }

    public void setSelectedItemBackgroundDrawable(Drawable selectedItemBackground) {
        this.mSelectedItemBackgroundDrawable = selectedItemBackground;
        redraw();
    }

    public Drawable getButtonLeftDrawable() {
        return mButtonLeftDrawable;
    }

    public void setButtonLeftDrawable(Drawable buttonLeftDrawable) {
        this.mButtonLeftDrawable = buttonLeftDrawable;
        mBtnPrevMonth.setImageDrawable(buttonLeftDrawable);
        mBtnPrevWeek.setImageDrawable(buttonLeftDrawable);
    }

    public Drawable getButtonRightDrawable() {
        return mButtonRightDrawable;
    }

    public void setButtonRightDrawable(Drawable buttonRightDrawable) {
        this.mButtonRightDrawable = buttonRightDrawable;
        mBtnNextMonth.setImageDrawable(buttonRightDrawable);
        mBtnNextWeek.setImageDrawable(buttonRightDrawable);
    }

    public Day getSelectedItem() {
        return mSelectedItem;
    }

    public void setSelectedItem(Day selectedItem) {
        this.mSelectedItem = selectedItem;
    }


    public OnSwipeTouchListener getSwipe(Context context) {
        return new OnSwipeTouchListener(context) {

            @Override
            public void onSwipeRight() {
                TranslateAnimation animate = new TranslateAnimation(-100, 0, 0, 0);
                animate.setDuration(100);//getWidth()
                animate.setFillAfter(true);
                mScrollViewBody.startAnimation(animate);

                if (getState() == STATE_COLLAPSED) {
                    mBtnPrevWeek.performClick();
                } else if (getState() == STATE_EXPANDED) {
                    mBtnPrevMonth.performClick();
                }
            }

            @Override
            public void onSwipeLeft() {
                TranslateAnimation animate = new TranslateAnimation(150, 0, 0, 0);
                animate.setDuration(100);//getWidth()
                animate.setFillAfter(true);
                mScrollViewBody.startAnimation(animate);

                if (getState() == STATE_COLLAPSED) {
                    mBtnNextWeek.performClick();
                } else if (getState() == STATE_EXPANDED) {
                    mBtnNextMonth.performClick();
                }
            }

            @Override
            public void onSwipeTop() {
              /*  if (getState() == STATE_EXPANDED)
                    expandIconView.performClick();*/
            }

            @Override
            public void onSwipeBottom() {
               /* if (getState() == STATE_COLLAPSED)
                    expandIconView.performClick();*/
            }
        };
    }


}
