package com.example.github.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.airbnb.lottie.LottieAnimationView;
import com.example.github.R;
import com.example.github.ui.adapter.RepositoryAdapter;
import com.example.github.ui.item_model.RepositorySearchItem;

import java.util.ArrayList;
import java.util.logging.Handler;

public class SearchFragment extends Fragment {
    private LottieAnimationView lottieAnimationView;
    private ArrayList<RepositorySearchItem> repositorySearchItems = new ArrayList<>();
    private RecyclerView recyclerView;
    private RepositoryAdapter adapter;
    private EditText search;
    private ProgressBar progressBar;
    boolean isLoading = false;
    private CardView cardView;


    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_search, container, false);
        lottieAnimationView = view.findViewById(R.id.lottie_github);
        lottieAnimationView.animate();

        recyclerView = view.findViewById(R.id.rv_repository_search);

        progressBar = view.findViewById(R.id.progressBar);
        populateDate();
        getAllRepoSearch();

        search = view.findViewById(R.id.edit_search);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        /*adapter.setOnLoadMore(new RepositoryAdapter.OnLoadMore() {
            @Override
            public void onLoadMore() {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        //repositorySearchItems.add(null);
                        adapter.notifyItemInserted(repositorySearchItems.size() - 1);

                    }
                });

                *//*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        repositorySearchItems.remove(repositorySearchItems.size() - 1);
                        adapter.notifyItemRemoved(repositorySearchItems.size());
                        int index = repositorySearchItems.size();
                        for(int i = index; i < index + 10; i++){
                            repositorySearchItems.add(new RepositorySearchItem("0", "Jonovono/c", "Give folders or directories comments and view them easy.",
                                    "on 2 Oct 2021", "3", "0"));
                        }
                        adapter.setIsLoading(false);
                    }
                }, 1000);*//*
            }
        });*/


        return view;
    }



    private void populateDate() {
        int i = 0;
        while(i < 10){
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(i);
            String strI = sb.toString();
            repositorySearchItems.add(new RepositorySearchItem(strI, "Jonovono/c", "Give folders or directories comments and view them easy.",
                    "on 2 Oct 2021", "3", "0"));
            i++;
        }
    }

    private void getAllRepoSearch() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new RepositoryAdapter(repositorySearchItems, getActivity(), recyclerView));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}