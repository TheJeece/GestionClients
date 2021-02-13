package com.jcr.GestionClients.ui.Prestations;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.jcr.GestionClients.R;

import org.w3c.dom.Text;

import java.util.List;

public class PrestationsAdapter extends RecyclerView.Adapter<PrestationsAdapter.PrestaHolder> {

    Context context;
    boolean deleteActivated;
    List<Prestation> prestations;


    public PrestationsAdapter(Context context, List<Prestation> prestations, boolean deleteActivated) {
        this.context = context;
        this.prestations=prestations;
        this.deleteActivated = deleteActivated;
    }

    @Override
    public PrestaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_item,parent,false);
        return new PrestaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrestaHolder holder, int position) {
        final Prestation prestation = prestations.get(position);

        //Gestion de la checkbox
        if (!deleteActivated) {
            holder.checkBox.setVisibility(View.GONE);
        } else {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(prestation.isSelected());
            holder.checkBox.setTag(prestations.get(position));
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Prestation prestation1 = (Prestation) holder.checkBox.getTag();
                    prestation1.setSelected(holder.checkBox.isChecked());
                    prestations.get(position).setSelected(holder.checkBox.isChecked());
                }
            });
        }

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrestationsFragment.PrestaKey = prestation.getId();
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_nav_ListePrestations_to_nav_EditPresta);
            }
        });

        holder.title.setText(prestation.getName());
        holder.price.setText(String.valueOf(prestation.getPrice()));
    }

    @Override
    public int getItemCount() {
        return prestations.size();
    }

    public static class PrestaHolder extends  RecyclerView.ViewHolder {
        TextView title;
        TextView price;
        CheckBox checkBox;
        CardView cardview;

        public PrestaHolder(View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.id_title);
            price=itemView.findViewById(R.id.id_price);
            checkBox=itemView.findViewById(R.id.id_check);
            cardview=itemView.findViewById(R.id.cardView);
        }
    }


    public List<Prestation> getPrestations() {
        return prestations;
    }

}
