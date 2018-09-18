package com.example.mzy.loadinganimations;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {

    String[] INDICATORS = new String[]{
            "BasketBallIndicator",
            "ZoomIndicator",
            "CollisionIndicator"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new RecyclerView.Adapter<IndictorHolder>() {

            @Override
            public IndictorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = getLayoutInflater().inflate(R.layout.item_indicator, parent, false);
                return new IndictorHolder(v);
            }

            @Override
            public void onBindViewHolder(IndictorHolder holder, int position) {
                holder.indicator.setIndicator(INDICATORS[position]);
            }

            @Override
            public int getItemCount() {
                return INDICATORS.length;
            }
        });
    }

    class IndictorHolder extends RecyclerView.ViewHolder {
        public LoadingIndicator indicator;

        public IndictorHolder(View itemView) {
            super(itemView);
            indicator = itemView.findViewById(R.id.loadingIndicator);
        }
    }

}
