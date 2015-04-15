package com.shells.percolatecoffee.android.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shells.percolatecoffee.R;
import com.shells.percolatecoffee.api.models.CoffeeResource;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class CoffeeListAdapter extends RecyclerView.Adapter<CoffeeListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CoffeeResource> coffeeResources;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout content;
        TextView tvName;
        TextView tvDesc;
        ImageView imgCoffee;

        public ViewHolder(View view) {
            super(view);
            content = (LinearLayout) view.findViewById(R.id.feed_content);
            tvName = (TextView) view.findViewById(R.id.tv_coffee_name);
            tvDesc = (TextView) view.findViewById(R.id.tv_coffee_desc);
            imgCoffee = (ImageView) view.findViewById(R.id.img_coffee_image);
        }
    }

    public CoffeeListAdapter(Context context, ArrayList<CoffeeResource> objects) {
        this.context = context;
        coffeeResources = objects;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_coffee_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        CoffeeResource coffeeResource = coffeeResources.get(position);
        if (coffeeResource.image_url != null && !coffeeResource.image_url.equals("")) {
                Picasso.with(context)
                        .load(coffeeResource.image_url)
                        .into(viewHolder.imgCoffee);
        } else {
            viewHolder.imgCoffee.setVisibility(View.GONE);
        }
        viewHolder.tvName.setText(coffeeResource.name != null ? coffeeResource.name : "");
        viewHolder.tvDesc.setText(coffeeResource.desc != null ? coffeeResource.desc : "");
    }

    @Override
    public int getItemCount() {
        return coffeeResources.size();
    }
}