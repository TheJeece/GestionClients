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

import com.jcr.GestionClients.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.jcr.GestionClients.MainActivity.CLIENT_ID;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.SheetHolder> {

    Context context;
    List<Client> list = new ArrayList<>();
    String FirstLetter=null;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    boolean deleteActivated;

    public ClientAdapter(Context context, List<Client> list, boolean deleteActivated) {
        this.context = context;
        this.list=list;
        this.deleteActivated = deleteActivated;
    }

    @Override
    public SheetHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cardview_client_group,parent,false);
        return new SheetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SheetHolder holder, int position) {
        final Client client = list.get(position);
        if (FirstLetter != null && FirstLetter.equals(client.getName().substring(0,1).toUpperCase())) {
            holder.group.setVisibility(View.INVISIBLE);
        }else{
            holder.group.setVisibility(View.VISIBLE);
            FirstLetter = client.getName().substring(0,1).toUpperCase();
            holder.group.setText(FirstLetter);

        }
        holder.item.setText(client.getName());
        holder.subItem.setText(client.getPhone());
        if (!deleteActivated) {
            holder.checkBox.setVisibility(View.GONE);

            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ClientModel clientModel = new ClientModel();
                    CLIENT_ID = clientModel.getKey(context, client.name);
                    NavController navController = Navigation.findNavController(v);
                    navController.navigate(R.id.action_nav_Clients_to_EditClient);
                }
            });
        } else {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(client.getSelected());
            holder.checkBox.setTag(list.get(position));
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Client client1 = (Client) holder.checkBox.getTag();
                    client1.setSelected(holder.checkBox.isChecked());
                    list.get(position).setSelected(holder.checkBox.isChecked());

                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class SheetHolder extends  RecyclerView.ViewHolder {

        TextView group, item, subItem;
        CheckBox checkBox;
        CardView cardview;

        public SheetHolder(View itemView) {
            super(itemView);
            group =itemView.findViewById(R.id.id_text_sheet_item_group);
            item =itemView.findViewById(R.id.id_text_sheet_item_mainItem);
            subItem =itemView.findViewById(R.id.id_text_sheet_item_subItem);
            checkBox=itemView.findViewById(R.id.id_check_sheet_item);
            cardview=itemView.findViewById(R.id.cardView);

        }
    }

    public List<Client> getClientsList() {
        return list;
    }

}
