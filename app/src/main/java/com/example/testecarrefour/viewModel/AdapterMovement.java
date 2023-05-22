package com.example.testecarrefour.viewModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testecarrefour.R;
import com.example.testecarrefour.model.Movement;
import com.example.testecarrefour.ui.HomeActivity;

import java.util.List;

public class AdapterMovement extends RecyclerView.Adapter<AdapterMovement.MyViewHolder> {

    private List<Movement> movements;
    HomeActivity context;

    public AdapterMovement(List<Movement> movements, HomeActivity context) {
        this.movements = movements;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemList = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_movement, parent, false);
        return new MyViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Movement movement = movements.get(position);
        holder.textCategory.setText(movement.getCategory());
        holder.textValue.setText(String.valueOf(movement.getValue()));
        holder.textValue.setTextColor(context.getResources().getColor(R.color.blue_carrefour));
        holder.textDescription.setText(movement.getDescription());

        if (movement.getType() == "E" || movement.getType().equals("E")){
            holder.textValue.setText(" - " + movement.getValue());
            holder.textValue.setTextColor(context.getResources().getColor(R.color.red_carrefour));
        }

    }

    @Override
    public int getItemCount() {
        return movements.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textCategory, textDescription, textValue;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textCategory = itemView.findViewById(R.id.text_category_adapter);
            textDescription = itemView.findViewById(R.id.text_description_adapter);
            textValue = itemView.findViewById(R.id.text_value_adapter);
        }
    }
}
