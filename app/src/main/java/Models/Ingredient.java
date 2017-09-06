package Models;

/**
 * Created by femi on 9/6/17.
 */

public class Ingredient {

    private int quatity;
    private String measure;
    private String ingredient;

    public Ingredient(int quatity, String measure, String ingredient) {
        this.quatity = quatity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public int getQuatity() {
        return quatity;
    }

    public void setQuatity(int quatity) {
        this.quatity = quatity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }
}
