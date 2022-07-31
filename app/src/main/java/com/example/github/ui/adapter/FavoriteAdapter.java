package com.example.github.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.github.R;
import com.example.github.ui.db.FavoriteDB;
import com.example.github.ui.item_model.FavoriteList;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private List<FavoriteList> favoriteLists;
    private Context context;
    private FavoriteDB favoriteDB;

    public FavoriteAdapter(List<FavoriteList> favoriteLists, Context context) {
        this.favoriteLists = favoriteLists;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite,parent,false);
        favoriteDB = new FavoriteDB(context);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteList fl=favoriteLists.get(position);
        holder.title_repo.setText(fl.getRepoName());
        holder.description_repo.setText(fl.getDescription());
        holder.update_repo.setText(fl.getUpdate());
        holder.starred_repo.setText(fl.getStarred());

    }

    @Override
    public int getItemCount() {
        return favoriteLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title_repo, description_repo, update_repo, starred_repo;
        private Button fav_btn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title_repo = itemView.findViewById(R.id.repository_name_fav);
            description_repo = itemView.findViewById(R.id.repository_description_fav);
            update_repo = itemView.findViewById(R.id.update_fav);
            starred_repo = itemView.findViewById(R.id.starred_fav);
            fav_btn = itemView.findViewById(R.id.image_save_fav);

            fav_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final FavoriteList favoriteItem = favoriteLists.get(position);
                    favoriteDB.removeFav(favoriteItem.getId());
                    removeItem(position);
                }
            });
        }
    }

    private void removeItem(int position) {
        favoriteLists.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, favoriteLists.size());
    }
}
