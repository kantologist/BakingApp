package Models;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by femi on 9/6/17.
 */

public class Recipe{

    private int id;
    private String name;
    private List<Ingredient> ingredients;
    private List<Step> steps;
    private int servings;
    private String image;
    private boolean loaded;

    public Recipe(int id, String name, List<Ingredient> ingredients,
                  List<Step> steps, int servings, String image){

        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
        this.loaded = false;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public List<Step> getSteps() {

        return steps;
    }

    public List<Ingredient> getIngredients() {

        return ingredients;
    }

    public boolean getLoaded(){
        return this.loaded;
    }

    public void setLoaded(boolean loaded){
        this.loaded = loaded;
    }
}
