package com.zinzin.loltft;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zinzin.loltft.adapter.DetailUnitAdapter;
import com.zinzin.loltft.adapter.ItemIconAdapter;
import com.zinzin.loltft.adapter.MiniIconAdapter;
import com.zinzin.loltft.model.Detail;
import com.zinzin.loltft.model.Type;
import com.zinzin.loltft.utils.Utils;
import com.zinzin.loltft.view.CustomLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    public static String TAG = DetailActivity.class.getSimpleName();
    private ImageView ivFullUnit, ivSkillUnit;
    private Detail detail = new Detail();
    private TextView tvStat;
    private TextView tvName, tvSubName, tvDes, tvSkillName, tvSkillDes, tvSkillTag;
    private RecyclerView rcvType, rcvItem, rcvMiniIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initView();
    }


    private void initView() {
        Utils.hideSoftKeyboard(this);
        ivFullUnit = findViewById(R.id.iv_units_full);
        ivSkillUnit = findViewById(R.id.iv_skill);
        tvName = findViewById(R.id.tv_name);
        tvSubName = findViewById(R.id.tv_sub_name);
        tvDes = findViewById(R.id.tv_des);
        tvStat = findViewById(R.id.tv_stat);
        tvSkillName = findViewById(R.id.tv_name_skill);
        tvSkillDes = findViewById(R.id.tv_des_skill);
        tvSkillTag = findViewById(R.id.tv_tag_skill);
        rcvMiniIcon = findViewById(R.id.rcv_mini);
        rcvType = findViewById(R.id.rcv_detail_unit);
        rcvItem = findViewById(R.id.rcv_item_unit);
        setData(new OnDataReceiveCallback() {
            @Override
            public void onDataReceived() {
                if (detail != null) {
                    setupUI(detail);
                } else {
                    Toast.makeText(DetailActivity.this, "Something went wrong!! please back android try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        setUpRcv();
    }

    private void setData(final OnDataReceiveCallback callback) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("tft_db").child("detailList").child(getIntent().getStringExtra("name"));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    detail = dataSnapshot.getValue(Detail.class);
                }
                callback.onDataReceived();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void setupUI(Detail detail) {
        tvName.setText(detail.getName());
        tvSubName.setText(detail.getNick_name());
        Glide.with(getApplicationContext()).load(detail.getUrl()).apply(RequestOptions.circleCropTransform()).into(ivFullUnit);
        tvDes.setText(detail.getDes());
        tvStat.setText(detail.getStat());
        Glide.with(getApplicationContext()).load(detail.getSkill_url()).apply(RequestOptions.circleCropTransform()).into(ivSkillUnit);
        tvSkillDes.setText(detail.getSkill_des());
        tvSkillName.setText(detail.getSkill_name());
        tvSkillTag.setText(detail.getSkill_damage());
        setUpRcv(detail);
    }

    private void setUpRcv(Detail detail) {

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcvMiniIcon.setLayoutManager(layoutManager);
        MiniIconAdapter miniIconAdapter = new MiniIconAdapter(this, detail.getType());
        rcvMiniIcon.setAdapter(miniIconAdapter);

        LinearLayoutManager layoutManager2
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvType.setLayoutManager(layoutManager2);
        DetailUnitAdapter detailUnitAdapter = new DetailUnitAdapter(this, detail.getType());
        rcvType.setAdapter(detailUnitAdapter);

        GridLayoutManager adapterManager = new GridLayoutManager(this, 6);
        rcvItem.setLayoutManager(adapterManager);
        ItemIconAdapter itemIconAdapter = new ItemIconAdapter(this, detail.getItems());
        rcvItem.setAdapter(itemIconAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
