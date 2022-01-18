package com.eot_app.utility;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.ImageViewCompat;

import com.eot_app.R;

public class CustomFilterButton extends LinearLayout {
    boolean isSeleted = false;
    private TextView textView;
    private ImageView img_icon;
    private ImageView img_close;
    private String text;
    private int res_icon;
    private int res_close;
    private int text_color;
    private int icon_color;
    private Context mContext;


    public CustomFilterButton(Context context) {
        super(context);
        initViews(context, null);
    }

    public CustomFilterButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
    }

    public CustomFilterButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context, attrs);
    }

    public CustomFilterButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context, attrs);
    }

    private void initViews(Context context, AttributeSet attr) {
        TypedArray a = context.obtainStyledAttributes(attr, R.styleable.CustomFilterButton);
        try {
            text = a.getString(R.styleable.CustomFilterButton_cfb_text);
            res_icon = a.getResourceId(R.styleable.CustomFilterButton_cfb_icon, R.drawable.ic_new_task);
            res_close = a.getResourceId(R.styleable.CustomFilterButton_cfb_icon_close, R.drawable.ic_close_white_24dp);
            text_color = a.getColor(R.styleable.CustomFilterButton_cfb_text_color, getResources().getColor(R.color.txt_color));
            //icon_color = a.getColor(R.styleable.CustomFilterButton_cfb_icon_color, Color.BLACK);
        } finally {
            a.recycle();
        }

        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_custom_filter_button, this, true);

        textView = view.findViewById(R.id.textView);
        img_icon = view.findViewById(R.id.img_icon);
        img_close = view.findViewById(R.id.img_close);

        setDefaults();
    }

    private void setDefaults() {
        setPadding(30, 10, 30, 10);
        setElevation(5f);
        setSeleted(false);
        //  textView.setTextColor(text_color);

        if (!TextUtils.isEmpty(text))
            textView.setText(text);

        img_icon.setImageResource(res_icon);
        img_close.setImageResource(res_close);

    }

    public boolean isSeleted() {
        return isSeleted;
    }

    public void setSeleted(boolean seleted) {
        setBackgroundResource(seleted ? R.drawable.filter_seleted_bg : R.drawable.custome_search_bg);
        this.isSeleted = seleted;
        if (seleted) {
            img_close.setVisibility(VISIBLE);
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            DrawableCompat.setTint(img_icon.getDrawable(), ContextCompat.getColor(mContext, R.color.white));
        } else {
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.txt_color));
            img_close.setVisibility(GONE);
            // DrawableCompat.setTint(img_icon.getDrawable(), ContextCompat.getColor(mContext, R.color.white));
            ImageViewCompat.setImageTintList(img_icon, null);

        }
    }

    public void setText(String text) {
        this.text = text;
        if (!TextUtils.isEmpty(this.text))
            textView.setText(text);

    }
}
