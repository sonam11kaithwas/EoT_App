package com.eot_app.home_screens;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.eot_app.R;
import com.eot_app.login_next.Login2Activity;
import com.eot_app.services.GetKillEvent_ToDestryNotication;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.settings.firstSync.FirstSyncActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class SplashActivity extends AppCompatActivity {
    Observer observer = new Observer() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(Object o) {
            if (App_preference.getSharedprefInstance().getLoginRes() != null) {
                if (App_preference.getSharedprefInstance().getLoginRes().getToken().isEmpty()) {
                    Intent intent = new Intent(SplashActivity.this, Login2Activity.class);
                    startActivity(intent);
                    finish();
                } else {
                    LoginSuccessFully();
                }
            } else {
                Intent intent = new Intent(SplashActivity.this, Login2Activity.class);
                startActivity(intent);
                finish();
            }
        }

        private void LoginSuccessFully() {
            startService(new Intent(SplashActivity.this, GetKillEvent_ToDestryNotication.class));
            startActivity(new Intent(SplashActivity.this, FirstSyncActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    };
    private TextView login_header;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        /** some settings update in firestore because firebase store timestamp **/


        try {
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    //.setTimestampsInSnapshotsEnabled(true)
                    .setPersistenceEnabled(true)
                    .build();
            firestore.setFirestoreSettings(settings);
        } catch (Exception ex) {
            ex.getMessage();
        }


        /**Launch root/top activity**/
        if (!isTaskRoot()) {
            final Intent intent = getIntent();
            final String intentAction = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intentAction != null && intentAction.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }
        initializelables();
    }

    private void initializelables() {
        login_header = findViewById(R.id.login_header);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        Observable.timer(3, TimeUnit.SECONDS).subscribe(observer);
        super.onResume();
    }

}

