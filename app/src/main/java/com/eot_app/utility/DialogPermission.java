package com.eot_app.utility;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;

import com.eot_app.R;
import com.eot_app.utility.language_support.LanguageController;

/**
 * Created by Mahendra Dabi on 16/3/21.
 */
public class DialogPermission extends DialogFragment {

    private AppCompatButton button_trun_on;
    private AppCompatButton button_no_thanks;
    private AppCompatTextView tv_permission_msg, tv_permission_heading;

    private OnActionButton onActionButton;

    public void setOnActionButton(OnActionButton onActionButton) {
        this.onActionButton = onActionButton;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_Light_Dialog_Alert);
        setCancelable(false);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_location_permission, container, false);

        getView(view);
        return view;
    }

    private void getView(View view) {
        tv_permission_heading = view.findViewById(R.id.tv_permission_heading);
        tv_permission_msg = view.findViewById(R.id.tv_permission_msg);

        button_no_thanks = view.findViewById(R.id.btn_no_thanx);
        button_trun_on = view.findViewById(R.id.btn_turn_on);


        tv_permission_heading.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.location_permission));
        tv_permission_msg.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.location_msg));
        button_trun_on.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.turn_on));
        button_no_thanks.setText(LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_thanks));


        button_no_thanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onActionButton != null) {
                    onActionButton.onLocationPermissionChanged(false);
                    dismiss();
                }
            }
        });


        button_trun_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onActionButton != null) {
                    onActionButton.onLocationPermissionChanged(true);
                    dismiss();
                }
            }
        });

    }

    public interface OnActionButton {
        void onLocationPermissionChanged(boolean isAllowed);
    }
}
