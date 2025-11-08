package com.example.communityhub.legacyui;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.communityhub.R;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ItemDetailActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private TextView tvTitle, tvCategory, tvDesc;
    private ImageView imgDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        dbHelper = new DBHelper(this);

        tvTitle = findViewById(R.id.tvTitle);
        tvCategory = findViewById(R.id.tvCategory);
        tvDesc = findViewById(R.id.tvDesc);
        imgDetail = findViewById(R.id.imgDetail);

        long id = getIntent().getLongExtra("id", -1);
        if (id != -1) {
            loadItemDetails(id);
        }
    }

    private void loadItemDetails(long id) {
        List<Item> items = dbHelper.getAllItems();
        for (Item it : items) {
            if (it.getId() == id) {
                tvTitle.setText(it.getTitle());
                tvCategory.setText(it.getCategory());
                tvDesc.setText(it.getDescription());

                if (it.getImageUri() != null && !it.getImageUri().isEmpty()) {
                    imgDetail.setImageURI(Uri.parse(it.getImageUri()));
                } else {
                    imgDetail.setImageResource(R.drawable.ic_baseline_image_24);
                }
                break;
            }
        }
    }
}
