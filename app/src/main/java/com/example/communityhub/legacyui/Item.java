package com.example.communityhub.legacyui;

public class Item {
    private long id;
    private String title;
    private String category;
    private String description;
    private String imageUri;
    private boolean favorite;

    // Empty constructor
    public Item() {}

    // Full constructor
    public Item(long id, String title, String category, String description, String imageUri, boolean favorite) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.imageUri = imageUri;
        this.favorite = favorite;
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUri() { return imageUri; }
    public void setImageUri(String imageUri) { this.imageUri = imageUri; }

    public boolean isFavorite() { return favorite; }
    public void setFavorite(boolean favorite) { this.favorite = favorite; }
}
