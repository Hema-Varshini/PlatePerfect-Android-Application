package edu.northeastern.numad24su_plateperfect;

public class rvChildModelClass {
    private int Cook_time;
    private String Cuisine;
    private String Image_Link;
    private String Name;
    private int Prep_time;
    private double Rating;
    private String Tagline;
    private String Type;
    private String Description;

    // No-argument constructor required for Firebase
    public rvChildModelClass() {
    }

    public rvChildModelClass(int Cook_time, String Cuisine, String Image_Link, String Name, int Prep_time, double Rating, String Tagline, String Type,String Description) {
        this.Cook_time = Cook_time;
        this.Cuisine = Cuisine;
        this.Image_Link = Image_Link;
        this.Name = Name;
        this.Prep_time = Prep_time;
        this.Rating = Rating;
        this.Tagline = Tagline;
        this.Type = Type;
        this.Description=Description;
    }

    public int getCook_time() {
        return Cook_time;
    }

    public void setCook_time(int Cook_time) {
        this.Cook_time = Cook_time;
    }

    public String getCuisine() {
        return Cuisine;
    }

    public void setCuisine(String Cuisine) {
        this.Cuisine = Cuisine;
    }

    public String getImage_Link() {
        return Image_Link;
    }

    public void setImage_Link(String Image_Link) {
        this.Image_Link = Image_Link;
    }
    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getName() {
        return Name;
    }
    public String getDescription() {
        return Description;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getPrep_time() {
        return Prep_time;
    }

    public void setPrep_time(int Prep_time) {
        this.Prep_time = Prep_time;
    }

    public double getRating() {
        return Rating;
    }

    public void setRating(double Rating) {
        this.Rating = Rating;
    }

    public String getTagline() {
        return Tagline;
    }

    public void setTagline(String Tagline) {
        this.Tagline = Tagline;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }
}
