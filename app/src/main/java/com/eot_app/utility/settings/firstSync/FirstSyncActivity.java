package com.eot_app.utility.settings.firstSync;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.eot_app.BuildConfig;
import com.eot_app.R;
import com.eot_app.home_screens.MainActivity;
import com.eot_app.login_next.Login2Activity;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.ChatApp_Preference;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.util_interfaces.Callback_AlertDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.concurrent.Callable;

public class FirstSyncActivity extends AppCompatActivity implements FirstSyncView {
    FirstSyncPi syncpi;
    ImageView sync_iv;
    ProgressBar syn_progress;
    TextView tv_sync_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_sync);
        tv_sync_msg = findViewById(R.id.textView);
        tv_sync_msg.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.sync_msg));

        syncpi = new FirstSyncPC(this);
        sync_iv = findViewById(R.id.sync_iv);
        syn_progress = findViewById(R.id.syn_progress);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.progress_anim);
        sync_iv.startAnimation(animation);
        syn_progress.setMax(13);

        //   startSyncForAttchment();

        syncpi.startSync();

    }


    private void startSyncForAttchment() {
        try {
            Log.e("", "");
            if (Float.valueOf(BuildConfig.VERSION_NAME) >= 2.55 && !ChatApp_Preference.getSharedprefInstance().getUserChatForGrp()) {
                ChatApp_Preference.getSharedprefInstance().setUserChatForGrp(true);
                App_preference.getSharedprefInstance().setUsersSyncTime("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void goHomePage() {
        if (!App_preference.getSharedprefInstance().getLoginRes().getExpireStatus().equals("0")) {
            startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        } else {
            Intent intent = new Intent(FirstSyncActivity.this, Login2Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            App_preference.getSharedprefInstance().setBlankTokenOnSessionExpire();
            finish();
        }
    }

    @Override
    public void errorMsg(String msg) {
//        Log.d("Sonam Status===", "Sonam Status===");
//        Log.d("sonam", "sonam" + App_preference.getSharedprefInstance().getFirstSyncState());
        showRetryDialog("Alert", msg, "back to login", "retry");
    }

    @Override
    public void progressStatus(int status_no) {
        syn_progress.setProgress(status_no + 1);
    }

    @Override
    public void showRetryDialog(String title, String msg, String lable1, String lable2) {
        try {
            final BottomSheetDialog dialog = new BottomSheetDialog(this);
            dialog.setContentView(R.layout.bottom_alert_dialog);
            TextView txt_lable1 = dialog.findViewById(R.id.lable1);
            TextView txt_lable2 = dialog.findViewById(R.id.lable2);
            TextView alert_msg = dialog.findViewById(R.id.alert_msg);
            alert_msg.setText(msg);
            txt_lable1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    finish();
                }
            });

            txt_lable2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //       Log.d("Sonam Status===", "Sonam Status===" + App_preference.getSharedprefInstance().getFirstSyncState() + "");
                    syncpi.retryCall();
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            });
            if (dialog != null) {
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUI(int i) {

    }

    @Override
    public void upateForcefully() {
        AppUtility.alertDialog(this, "Update!", AppConstant.updateAppMsg, AppConstant.update, "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {


//                open login screen
                Intent intent = new Intent(FirstSyncActivity.this, Login2Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);


                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }

                return null;
            }
        });
    }

    @Override
    public void updateNotForcefully() {
        AppUtility.alertDialog2(this, "Update!", AppConstant.updateAppMsg, AppConstant.update, AppConstant.cancel, new Callback_AlertDialog() {
            @Override
            public void onPossitiveCall() {

                Intent intent = new Intent(FirstSyncActivity.this, Login2Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }

            @Override
            public void onNegativeCall() {
                syncpi.OfflineDbSync();
            }
        });
    }


    @Override
    public void setSubscriptionExpire(String msg) {
        try {

            TextView dailog_title, dialog_msg;
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            LayoutInflater inflater = (this).getLayoutInflater();
            final View customLayout = inflater.inflate(R.layout.subscription_layout, null);
            alertDialog.setView(customLayout);
            alertDialog.setCancelable(false);

            dailog_title = customLayout.findViewById(R.id.dai_title);
            dialog_msg = customLayout.findViewById(R.id.dia_msg);
            dailog_title.setText(msg);
            dialog_msg.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.please_contact_admin));

            alertDialog.setPositiveButton(LanguageController.getInstance().getMobileMsgByKey(AppConstant.close),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(FirstSyncActivity.this, Login2Activity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            App_preference.getSharedprefInstance().setBlankTokenOnSessionExpire();
                            finish();
                        }
                    });

            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sessionExpiredFinishActivity() {
        finish();
    }
}
