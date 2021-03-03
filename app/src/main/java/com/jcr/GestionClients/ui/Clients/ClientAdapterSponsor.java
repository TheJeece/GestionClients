package com.jcr.GestionClients.ui.Clients;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.jcr.GestionClients.R;
import com.jcr.GestionClients.ui.Sheet.Sheet;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.jcr.GestionClients.MainActivity.CLIENT_ID;
import static com.jcr.GestionClients.MainActivity.CLIENT_ID_NAV;

public class ClientAdapterSponsor extends RecyclerView.Adapter<ClientAdapterSponsor.listHolder> {

    Context context;
    List<String[]> list = new ArrayList<>();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public ClientAdapterSponsor(Context context, List list) {
        this.context = context;
        this.list=list;
    }

    @Override
    public listHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cardview_small,parent,false);

        return new listHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull listHolder holder, int position) {

        holder.checkBox.setVisibility(View.GONE);
        holder.price.setVisibility(View.VISIBLE);
        Log.i("Adapter", "onBindViewHolder: " + list.get(position)[0] + " " + list.get(position)[1]);
        if (!list.get(position)[1].equals("1")) {
            holder.price.setText(list.get(position)[1]);
        } else {
            holder.price.setVisibility(View.GONE);
        }

        holder.euro.setVisibility(View.GONE);

        holder.tvName.setVisibility(View.VISIBLE);
        holder.tvName.setText(list.get(position)[0]);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientModel clientModel = new ClientModel();
                CLIENT_ID_NAV = clientModel.getKey(context, list.get(position)[0]);
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_nav_AddClient_self);

            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class listHolder extends  RecyclerView.ViewHolder {

        TextView    tvName;
        TextView    price;
        TextView    euro;
        CheckBox    checkBox;
        CardView    cardView;

        public listHolder(View itemView) {
            super(itemView);
            tvName      = itemView.findViewById(R.id.id_title);
            price       = itemView.findViewById(R.id.id_price);
            euro        = itemView.findViewById(R.id.id_euro);
            checkBox    = itemView.findViewById(R.id.id_check);
            cardView    = itemView.findViewById(R.id.cardView);
        }
    }

    public List<String[]> getSponsoredOfClient() {
        return list;
    }

}
