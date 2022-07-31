package com.example.github.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.github.R;
import com.example.github.ui.DetailedView;
import com.example.github.ui.db.FavoriteDB;
import com.example.github.ui.item_model.RepositorySearchItem;

import java.util.ArrayList;

public class RepositoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<RepositorySearchItem> repositorySearchItems;
    private Context context;
    private ArrayList<RepositorySearchItem> repositorySearchItemsCopy;
    private FavoriteDB favoritesDB;
    final int VIEW_TYPE_LOADING = 0, VIEW_TYPE_ITEM = 1;
    private OnLoadMore mOnLoadMore;
    private boolean isLoading;
    private RecyclerView recyclerView;

    private int totalItemCount;
    private int lastVisibleItem;
    private int visibleThreshold = 5;

    public RepositoryAdapter(ArrayList<RepositorySearchItem> repositorySearchItems, Context context,
                             RecyclerView recyclerView) {
        this.repositorySearchItems = repositorySearchItems;
        this.context = context;
        this.repositorySearchItemsCopy = new ArrayList<>();
        repositorySearchItemsCopy.addAll(repositorySearchItems);
        this.recyclerView = recyclerView;

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                totalItemCount = layoutManager.getItemCount();

                if(!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)){
                    if(mOnLoadMore != null){
                        mOnLoadMore.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        favoritesDB = new FavoriteDB(context);
        SharedPreferences preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        boolean firstStart = preferences.getBoolean("firstStart", true);

        if(firstStart){
            createTableOnFirstStart();
        }

        if(viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repository, parent, false);
            return new ViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingHolder(view);
        }
    }

    private void createTableOnFirstStart() {
        favoritesDB.insertEmpty();

        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int getViewType = holder.getItemViewType();

        if(getViewType == VIEW_TYPE_ITEM) {
        final RepositorySearchItem currentItem = repositorySearchItems.get(position);
        ViewHolder hold = (ViewHolder) holder;

        hold.title_repo.setText(currentItem.getTitle_repo());
        hold.description_repo.setText(currentItem.getDescription_repo());
        hold.update_repo.setText(currentItem.getUpdate_repo());
        hold.starred_repo.setText(currentItem.getStarred_repo());

        readCursorData(currentItem, hold);
        }else if(getViewType == VIEW_TYPE_LOADING){
            LoadingHolder loadingHolder = (LoadingHolder) holder;
            loadingHolder.progressBar.setIndeterminate(true);
        }


    }

    private void readCursorData(RepositorySearchItem repositorySearchItem,
                                ViewHolder viewHolder) {
        Cursor cursor = favoritesDB.readData(repositorySearchItem.getId());
        SQLiteDatabase database = favoritesDB.getReadableDatabase();
        try {
            while (cursor.moveToNext()) {
                String item_fav_status = cursor.getString(cursor.getColumnIndexOrThrow(FavoriteDB.FAVORITE_STATUS));
                repositorySearchItem.setFavoriteStatus(item_fav_status);

                if (item_fav_status != null && item_fav_status.equals("1")) {
                    viewHolder.fav_btn.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                } else if (item_fav_status != null && item_fav_status.equals("0")) {
                    viewHolder.fav_btn.setBackgroundResource(R.drawable.ic_bookmark);
                }
            }
        }finally {
                if (cursor != null && cursor.isClosed())
                    cursor.close();
                database.close();
        }

    }

    @Override
    public int getItemCount() {
        return repositorySearchItems.size();
    }

    public void filter(CharSequence charSequence) {
        ArrayList<RepositorySearchItem> tempArrayList = new ArrayList<>();
        if(!TextUtils.isEmpty(charSequence)){
            for(RepositorySearchItem item : repositorySearchItems){
                if(item.getTitle_repo().toLowerCase().contains(charSequence)){
                    tempArrayList.add(item);
                }
            }
        }
        else{
            tempArrayList.addAll(repositorySearchItemsCopy);
        }

        repositorySearchItems.clear();
        repositorySearchItems.addAll(tempArrayList);
        notifyDataSetChanged();
        tempArrayList.clear();
    }

    @Override
    public int getItemViewType(int position) {
        return repositorySearchItems.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setOnLoadMore(OnLoadMore onLoadMore){
        mOnLoadMore = onLoadMore;

    }

    public void setIsLoading(boolean param){
        isLoading = param;
    }

    public class LoadingHolder extends RecyclerView.ViewHolder{
        private ProgressBar progressBar;

        public LoadingHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    public interface OnLoadMore{
        public void onLoadMore();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title_repo, description_repo, update_repo, starred_repo;
        private Button fav_btn;
        private CardView detailed_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title_repo = itemView.findViewById(R.id.repository_name);
            description_repo = itemView.findViewById(R.id.repository_description);
            update_repo = itemView.findViewById(R.id.update);
            starred_repo = itemView.findViewById(R.id.starred);
            fav_btn = itemView.findViewById(R.id.image_save);
            detailed_view = itemView.findViewById(R.id.card_repo);

            fav_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();
                    RepositorySearchItem repoItem = repositorySearchItems.get(position);

                    if(repoItem.getFavoriteStatus().equals("0")){
                        repoItem.setFavoriteStatus("1");
                        favoritesDB.insertIntoDatabase(repoItem.getTitle_repo(), repoItem.getDescription_repo(),
                                repoItem.getUpdate_repo(), repoItem.getId(), repoItem.getStarred_repo(),
                                repoItem.getFavoriteStatus());
                        fav_btn.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                    }
                    else if(repoItem.getFavoriteStatus().equals("1")){
                        repoItem.setFavoriteStatus("0");
                        favoritesDB.removeFav(repoItem.getId());
                        fav_btn.setBackgroundResource(R.drawable.ic_bookmark);
                    }
                    else{
                        favoritesDB.removeFav(repoItem.getId());
                        repoItem.setFavoriteStatus("0");
                    }
                }
            });

            detailed_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment = new DetailedView();
                    FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.body_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
        }
    }
}
