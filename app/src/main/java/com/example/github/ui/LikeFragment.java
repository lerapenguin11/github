package com.example.github.ui;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.github.R;
import com.example.github.ui.adapter.FavoriteAdapter;
import com.example.github.ui.db.FavoriteDB;
import com.example.github.ui.item_model.FavoriteList;

import java.util.ArrayList;

public class LikeFragment extends Fragment {
    private RecyclerView recyclerView;
    private FavoriteAdapter adapter;
    private ArrayList<FavoriteList> favoriteLists = new ArrayList<>();
    private FavoriteDB favoriteDB;
    private TextView clear_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_like, container, false);
        recyclerView = view.findViewById(R.id.rv_repository_like);
        favoriteDB = new FavoriteDB(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        clear_btn = view.findViewById(R.id.clear_text);

        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favoriteLists.clear();
                favoriteDB.deleteAllRow(); // this will delete all data from the table.
                adapter.notifyDataSetChanged();
            }
        });

        loadData();

        return view;
    }

    private void loadData() {
        if(favoriteLists!=null){
            favoriteLists.clear();
        }
        SQLiteDatabase db = favoriteDB.getReadableDatabase();
        Cursor cursor = favoriteDB.selectAllFavoriteList();
        try {
            while (cursor.moveToNext()){
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(FavoriteDB.ITEM_TITLE));
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(FavoriteDB.KEY_ID));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(FavoriteDB.ITEM_DISCRIPTION));
                @SuppressLint("Range") String update = cursor.getString(cursor.getColumnIndex(FavoriteDB.ITEM_UPDATE));
                @SuppressLint("Range") String starred = cursor.getString(cursor.getColumnIndex(FavoriteDB.ITEM_STARRED));
                FavoriteList favoriteList = new FavoriteList(id, title, description, starred, update);

                favoriteLists.add(favoriteList);
            }
        }finally {
            if(cursor!=null && cursor.isClosed())
                cursor.close();
            db.close();
        }
        adapter = new FavoriteAdapter(favoriteLists, getActivity());
        recyclerView.setAdapter(adapter);
    }
}