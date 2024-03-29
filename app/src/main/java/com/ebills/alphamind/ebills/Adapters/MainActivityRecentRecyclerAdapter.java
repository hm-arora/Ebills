package com.ebills.alphamind.ebills.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebills.alphamind.ebills.ProductMainActivity;
import com.ebills.alphamind.ebills.R;
import com.ebills.alphamind.ebills.ShopPageActivity;
import com.ebills.alphamind.ebills.Storage.FBNotification.FBNotification;
import com.ebills.alphamind.ebills.utils.TextDrawable;

import org.json.JSONArray;
import org.json.JSONException;


public class MainActivityRecentRecyclerAdapter extends RecyclerView.Adapter<MainActivityRecentRecyclerAdapter.ViewHolder> {

    private JSONArray jsonArray;
    private Context ctx;
    boolean ok = false;

    public MainActivityRecentRecyclerAdapter(Context context, JSONArray jsonArray) {
        this.jsonArray = jsonArray;
        this.ctx = context;
    }

    public MainActivityRecentRecyclerAdapter(Context context, JSONArray jsonArray, boolean ok) {
        this.jsonArray = jsonArray;
        this.ctx = context;
        this.ok = ok;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView date, sName, priceName;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
            date = itemView.findViewById(R.id.date);
            sName = itemView.findViewById(R.id.shop_name);
            priceName = itemView.findViewById(R.id.PriceName);

            if (ok) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int pos = getAdapterPosition();

                        try {
                            //ProductAdapter
                            Intent i = new Intent(ctx, ShopPageActivity.class);
                            i.putExtra("products", String.valueOf(jsonArray.getJSONObject(pos).getJSONObject("invoice").getJSONArray("products")));
                            ctx.startActivity(i);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            } else {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int pos = getAdapterPosition();

                        try {
                            //ProductAdapter
                            Intent i = new Intent(ctx, ProductMainActivity.class);
                            i.putExtra("products", String.valueOf(jsonArray.getJSONObject(pos).getJSONObject("invoice").getJSONArray("products")));
                            FBNotification fbNotification = new FBNotification(ctx);
                            JSONArray jsonArray1 = new JSONArray(fbNotification.getNotification());
                            i.putExtra("html", jsonArray1.getJSONObject(pos).getString("HTML"));
                            i.putExtra("pdf", jsonArray1.getJSONObject(pos).getString("PDF"));
                            ctx.startActivity(i);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        }
    }


    @Override
    public MainActivityRecentRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainActivityRecentRecyclerAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_bills_card, parent, false));
    }

    @Override
    public void onBindViewHolder(final MainActivityRecentRecyclerAdapter.ViewHolder holder, int position) {

        int color = Color.parseColor(getRandomMaterialColor("400"));

        try {
            String shopName = jsonArray.getJSONObject(position).getJSONObject("seller").getString("name");
            String price1 = jsonArray.getJSONObject(position).getJSONObject("invoice").getString("amount");
            String price = String.valueOf(Float.parseFloat(price1) * -1);
            String date = jsonArray.getJSONObject(position).getJSONObject("invoice").getString("date");
            String year = date.substring(0, 4);
            String month = date.substring(4, 6);
            String dat = date.substring(6);
            date = dat + "/" + month + "/" + year;
            TextDrawable myDrawable = TextDrawable.builder().beginConfig()
                    .textColor(Color.WHITE)
                    .useFont(Typeface.DEFAULT)
                    .toUpperCase()
                    .endConfig()
                    .buildRound(shopName.substring(0, 1), color);
            holder.imageView.setImageDrawable(myDrawable);
            holder.sName.setText(shopName);
            holder.date.setText(date);
            holder.priceName.setText(price);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public String getRandomMaterialColor(String typeColor) {
        int returnColor = Color.GRAY;
        int arrayId = ctx.getResources().getIdentifier("colors_" + typeColor, "array", ctx.getPackageName());

        if (arrayId != 0) {
            TypedArray colors = ctx.getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return String.format("#%06X", (0xFFFFFF & returnColor));
    }

}

