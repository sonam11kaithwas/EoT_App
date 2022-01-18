package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_payment_pkg.payment_dialog;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.eot_app.R;

public class Payment_MyDialog extends Dialog {
    public myOnClickListener myListener;
    private ImageView img;


    public Payment_MyDialog(Context context, myOnClickListener myclick) {
        super(context);
        this.myListener = myclick;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.payment_dialog_alert);

        img = findViewById(R.id.img);
        ObjectAnimator rotate = ObjectAnimator.ofFloat(img, "rotation", 180f, 0f);
        rotate.setDuration(500);
        rotate.start();


//        TranslateAnimation animate = new TranslateAnimation(0, 50, 0, 0);
//        animate.setDuration(500);
//        animate.setFillAfter(true);
//        img.startAnimation(animate);

        Button btn = findViewById(R.id.buttonOk);
        btn.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                myListener.onButtonClick(); // I am giving the click to the
            }
        });
    }

    // This is my interface //
    public interface myOnClickListener {
        void onButtonClick();
    }
}
