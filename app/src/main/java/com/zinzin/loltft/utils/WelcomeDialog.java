package com.zinzin.loltft.utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zinzin.loltft.R;

public class WelcomeDialog extends Dialog {
    private static final String TAG = WelcomeDialog.class.getSimpleName();
    private Activity mActivity;
    private int show_ads = 1;
    private LinearLayout llAds, llAdsShow;
    private DialogCallBack dialogClickCallBack;

    public WelcomeDialog(Activity activity, DialogCallBack dialogClickCallBack) {
        super(activity);
        this.mActivity = activity;
        this.dialogClickCallBack = dialogClickCallBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_ads);
        llAds = findViewById(R.id.ll_ads);
        llAdsShow = findViewById(R.id.ll_ads_show);
        YoYo.with(Techniques.Bounce)
                .duration(1000)
                .repeat(100000)
                .playOn(llAds);
        llAdsShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(dialogClickCallBack != null) dialogClickCallBack.onClickOpen();
            }
        });
    }

    public interface DialogCallBack {
        void onClickOpen();
    }
}

