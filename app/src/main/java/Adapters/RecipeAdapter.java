package Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.femi.bakingapp.R;
import com.example.femi.bakingapp.RecipeDescriptionListActivity;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import Models.Recipe;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by femi on 9/6/17.
 */

public class RecipeAdapter extends ArrayAdapter<Recipe>  {

    private List<Recipe> recipies;
    private Context context;

    public RecipeAdapter(Context context, int resource, List<Recipe> objects) {
        super(context, resource, objects);
        this.recipies = objects;
        this.context = context;
        ButterKnife.bind((Activity) context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_recipe, parent, false);

            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (getItem(position).getImage().isEmpty()) {
            holder.recipe_image.setImageResource(R.drawable.recipe);
        } else{
            Picasso.with(getContext())
                    .load(getItem(position).getImage())
                    .placeholder(R.drawable.recipe)
                    .error(R.drawable.recipe)
                    .into(holder.recipe_image);
        }


        holder.recipe_name.setText(getItem(position).getName());

        convertView.
                setOnClickListener(onClickListener(getItem(position), String.valueOf(position +1)));

        return convertView;
    }

    private View.OnClickListener onClickListener(final Recipe recipe, final String position){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RecipeDescriptionListActivity.class);
                Gson gson = new Gson();
                String pass_recipe = gson.toJson(recipe);
                intent.putExtra("Recipe", pass_recipe);
                context.startActivity(intent);

            }
        };

    }

    static class ViewHolder {
        @BindView(R.id.recipe_image) ImageView recipe_image;
        @BindView(R.id.recipe_name) TextView recipe_name;

        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }

    }
}


