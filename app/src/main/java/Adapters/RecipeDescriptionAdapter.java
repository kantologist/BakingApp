package Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.femi.bakingapp.FetchThumbnails;
import com.example.femi.bakingapp.R;
import com.example.femi.bakingapp.RecipeDescriptionDetailActivity;
import com.example.femi.bakingapp.RecipeDescriptionDetailFragment;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

import Models.Step;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by femi on 9/7/17.
 */

public class RecipeDescriptionAdapter
    extends RecyclerView.Adapter<RecipeDescriptionAdapter.ViewHolder> {

        private final List<Step> mValues;
        private boolean mTwoPane;
        private Context context;
        private int orientation;

        public RecipeDescriptionAdapter(Context context,List<Step> items) {
            this.context=context;
            mValues = items;
            Timber.plant(new Timber.DebugTree());
            mTwoPane = context.getResources().getBoolean(R.bool.mTwoPane);
            orientation = context.getResources().getConfiguration().orientation;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipedescription_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            if(!mValues.get(position).getThumbnailURL().isEmpty()){
                Picasso.with(context)
                        .load(mValues.get(position).getThumbnailURL())
                        .into(holder.step_image);
            } else if (mValues.get(position).getImageBitmap() == null) {
                holder.step_image.setImageResource(R.drawable.recipe);
            } else{
                try{
                    Timber.d("gotten bitmap");
                    holder.step_image.setImageBitmap(mValues.get(position).getImageBitmap());
                } catch (Throwable t){

                    Picasso.with(context)
                            .load(R.drawable.recipe)
                            .placeholder(R.drawable.recipe)
                            .error(R.drawable.recipe)
                            .into(holder.step_image);
                }

            }
            holder.step_dec.setText(mValues.get(position).getShortDescription());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane && orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        Bundle arguments = new Bundle();
                        Gson gson = new Gson();
                        String step = gson.toJson(holder.mItem);
                        arguments.putString(RecipeDescriptionDetailFragment.ARG_ITEM_ID, step);
                        RecipeDescriptionDetailFragment fragment = new RecipeDescriptionDetailFragment();
                        fragment.setArguments(arguments);
                        ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                                .replace(R.id.recipedescription_detail_container, fragment)
                                .commit();
                    } else {
                        Gson gson = new Gson();
                        String step = gson.toJson(holder.mItem);
                        Context context = v.getContext();
                        Intent intent = new Intent(context, RecipeDescriptionDetailActivity.class);
                        intent.putExtra(RecipeDescriptionDetailFragment.ARG_ITEM_ID, step);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public Step mItem;
            private View mView;
            @BindView(R.id.step_image) ImageView step_image;
            @BindView(R.id.step_description) TextView step_dec;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                ButterKnife.bind(this, view);
            }
        }
    }
