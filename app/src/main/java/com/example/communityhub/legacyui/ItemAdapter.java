package com.example.communityhub.legacyui;
import com.example.communityhub.R;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private final Context context;
    private final List<Item> itemList;
    private final DBHelper dbHelper;

    public ItemAdapter(Context context, List<Item> itemList, DBHelper dbHelper) {
        this.context = context;
        this.itemList = itemList;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = itemList.get(position);

        holder.title.setText(item.getTitle());
        holder.category.setText(item.getCategory());
        holder.favoriteToggle.setChecked(item.isFavorite());

        if (item.getImageUri() != null && !item.getImageUri().isEmpty()) {
            holder.image.setImageURI(Uri.parse(item.getImageUri()));
        } else {
            holder.image.setImageResource(R.drawable.ic_baseline_image_24);
        }

        // Toggle favorite
        holder.favoriteToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            dbHelper.updateFavorite(item.getId(), isChecked);
            item.setFavorite(isChecked);
        });

        // Click to open details
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ItemDetailActivity.class);
            intent.putExtra("id", item.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, category;
        ToggleButton favoriteToggle;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.itemImage);
            title = itemView.findViewById(R.id.itemTitle);
            category = itemView.findViewById(R.id.itemCategory);
            favoriteToggle = itemView.findViewById(R.id.toggleFav);
        }
    }
}
