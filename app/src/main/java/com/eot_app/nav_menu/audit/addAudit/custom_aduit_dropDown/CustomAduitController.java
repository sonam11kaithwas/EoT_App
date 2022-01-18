package com.eot_app.nav_menu.audit.addAudit.custom_aduit_dropDown;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eot_app.R;
import com.eot_app.custom_dropDown.ItemListener;
import com.eot_app.nav_menu.audit.addAudit.AddAuditActivity;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.settings.setting_db.FieldWorker;

import java.util.ArrayList;
import java.util.List;


// * Created by Sonam-11 on 13/8/20.


public class CustomAduitController {

    //   private static CustomDPController INSTANCE;
    PopupWindow jobServicePopupWindow;
    CustomAduitAdpter categoryAdpter;
    ArrayList<FieldWorker> datastr = new ArrayList<>();


    public CustomAduitController() {
    }

    private int getintToPX(int i) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, EotApp.getAppinstance().getResources().getDisplayMetrics());
    }

    public void showSpinnerDropDown(final Context context, View view, final ArrayList<FieldWorker> datastr) {
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> idsname = new ArrayList<>();
        this.datastr = datastr;
        if (jobServicePopupWindow == null) {

            LayoutInflater inflater = (LayoutInflater) context.getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View customView = inflater.inflate(R.layout.catrgory_list, null);

            jobServicePopupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;

            jobServicePopupWindow.setHeight(getintToPX(180));
            jobServicePopupWindow.setWidth(width - 60);

            jobServicePopupWindow.setAnimationStyle(R.anim.bounce_down);

            jobServicePopupWindow.setOutsideTouchable(true);
            jobServicePopupWindow.setFocusable(true);

            if (Build.VERSION.SDK_INT >= 21) {
                jobServicePopupWindow.setElevation(5.0f);
            }


            final EditText edtSearch = customView.findViewById(R.id.edtSearch);
            RecyclerView recyclerView = customView.findViewById(R.id.recyclepop);
            final ImageView imvCross = customView.findViewById(R.id.imvCross);

            imvCross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    edtSearch.getText().clear();
                }
            });

            edtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    Log.e("", "");
                    if (charSequence.length() > 0) {
                        imvCross.setVisibility(View.VISIBLE);
                    } else imvCross.setVisibility(View.GONE);

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    filter(editable.toString());
                }
            });


            categoryAdpter = new CustomAduitAdpter(datastr, new ItemListener() {
                @Override
                public void onItemClick() {
                    Log.e("", "");
                    if (context instanceof AddAuditActivity)
                        ((AddAuditActivity) context).setSelectedJobServices(datastr);

                }
            });
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(EotApp.getAppinstance().getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(categoryAdpter);
        }
        jobServicePopupWindow.showAsDropDown(view, 0, 0, Gravity.CENTER);
    }

    public void clearJobServicePopUp() {
        jobServicePopupWindow = null;
    }

    private void filter(String text) {
        List<FieldWorker> filterdNames = new ArrayList<>();

        for (FieldWorker s : datastr) {
            if (s.getFnm().toLowerCase().contains(text.toLowerCase())) {
                filterdNames.add(s);
            }
        }
        categoryAdpter.filterList(filterdNames);

    }

}
