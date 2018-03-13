package com.ebills.alphamind.ebills.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebills.alphamind.ebills.R;
import com.ebills.alphamind.ebills.Storage.Recent.RecentBillStore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class mainActivityAllBillsRecyclerAdapter extends RecyclerView.Adapter<mainActivityAllBillsRecyclerAdapter.ViewHolder> {

    JSONArray jsonArray;
    Context ctx;

    public mainActivityAllBillsRecyclerAdapter(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
        this.ctx = ctx;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView pName , sName , priceName;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.ClipArt);
            pName = itemView.findViewById(R.id.ProductName);
            sName = itemView.findViewById(R.id.ShopName);
            priceName = itemView.findViewById(R.id.PriceName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        SaveInRecent(pName.getText().toString() , sName.getText().toString() , priceName.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    public void SaveInRecent(String pName, String sName, String priceName) throws JSONException {
        RecentBillStore recentBillStore = new RecentBillStore(ctx);
        recentBillStore.saveBill(pName,sName,priceName);
    }


    @Override
    public mainActivityAllBillsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new mainActivityAllBillsRecyclerAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_fragments_card, parent, false));
    }

    @Override
    public void onBindViewHolder(final mainActivityAllBillsRecyclerAdapter.ViewHolder holder, int position) {

        for (int i = 0; i<jsonArray.length() ; i++){
            try {
                holder.pName.setText(jsonArray.getJSONObject(i).getString("product_name"));
                holder.sName.setText(jsonArray.getJSONObject(i).getString("shop_name"));
                holder.priceName.setText(jsonArray.getJSONObject(i).getString("price_name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }
}