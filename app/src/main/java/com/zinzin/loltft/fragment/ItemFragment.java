package com.zinzin.loltft.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zinzin.loltft.R;
import com.zinzin.loltft.adapter.ItemAdapter;
import com.zinzin.loltft.model.Item;
import com.zinzin.loltft.model.Origin;
import com.zinzin.loltft.model.Unit;
import com.zinzin.loltft.view.CustomLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class ItemFragment extends Fragment {
    public static String TAG = ItemFragment.class.getSimpleName();
    private List<Item> itemList = new ArrayList<>();
    private RecyclerView rcvItem;
    private EditText edtSearch;
    private ItemAdapter itemAdapter;
    public static ItemFragment newInstance() {
        return new ItemFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);
        initView(view);
        setUpRcvItem();
        return view;
    }
    private void initView(View view) {
        rcvItem = view.findViewById(R.id.rcv_items);
        edtSearch = view.findViewById(R.id.edt_search);
    }

    private void setUpRcvItem() {
        LinearLayoutManager layoutManager
                = new CustomLayoutManager(getActivity());
        rcvItem.setLayoutManager(layoutManager);
        ViewCompat.setNestedScrollingEnabled(rcvItem, false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        },100);
    }

    public void getData() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("tft_db").child("itemList");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Item item = ds.getValue(Item.class);
                        itemList.add(item);
                    }
                }
                itemAdapter = new ItemAdapter(getActivity(), itemList);
                rcvItem.setAdapter(itemAdapter);
                setUpEditText();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void setUpEditText() {
        edtSearch.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }

    private void filter(String text) {
        List<Item> itemBase = new ArrayList<>(itemList);
        List<Item> listItemFilter = new ArrayList<>();
        for (Item item : itemBase) {
            //filter theo tên
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                listItemFilter.add(item);
            }
        }
        if (text.equals("")) {
            listItemFilter.clear();
            itemAdapter.updateList(itemBase);
        } else {
            itemAdapter.updateList(listItemFilter);
        }
    }
}