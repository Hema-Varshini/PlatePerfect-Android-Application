package edu.northeastern.numad24su_plateperfect;

public class rvChildModelClass {
    String Link;
    String recipe;
    public rvChildModelClass(){

    }
    public rvChildModelClass(String image,String recipe){
        this.Link =image;
        this.recipe = recipe;
    }
    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        this.Link = link;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }


}
