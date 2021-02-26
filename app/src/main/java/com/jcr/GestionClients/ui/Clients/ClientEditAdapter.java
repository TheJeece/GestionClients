package com.jcr.GestionClients.ui.Clients;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.jcr.GestionClients.MainActivity;
import com.jcr.GestionClients.R;
import com.jcr.GestionClients.ui.Sheet.Sheet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.jcr.GestionClients.MainActivity.CLIENT_ID;
import static com.jcr.GestionClients.MainActivity.SHEET_ID;

public class ClientEditAdapter extends RecyclerView.Adapter<ClientEditAdapter.SheetHolder> {

    Context context;
    List<Sheet> list = new ArrayList<>();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public ClientEditAdapter(Context context, List<Sheet> list) {
        this.context = context;
        this.list=list;
    }

    @Override
    public SheetHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_item_small,parent,false);

        return new SheetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SheetHolder holder, int position) {
        final Sheet sheet = list.get(position);

        holder.checkBox.setVisibility(View.GONE);
        holder.date.setText(simpleDateFormat.format(sheet.getDate()));
        holder.price.setText(String.valueOf(sheet.getPrice()));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHEET_ID =sheet.getID();
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_nav_AddClient_to_nav_SheetAdd);
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class SheetHolder extends  RecyclerView.ViewHolder {

        TextView    date;
        TextView    price;
        CheckBox    checkBox;
        CardView    cardView;

        public SheetHolder(View itemView) {
            super(itemView);
            date =itemView.findViewById(R.id.id_title);
            price = itemView.findViewById(R.id.id_price);
            checkBox=itemView.findViewById(R.id.id_check);
            cardView=itemView.findViewById(R.id.cardView);
        }
    }

    public List<Sheet> getSheetsList() {
        return list;
    }

}
