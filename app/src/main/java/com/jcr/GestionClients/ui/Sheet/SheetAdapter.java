package com.jcr.GestionClients.ui.Sheet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.jcr.GestionClients.MainActivity;
import com.jcr.GestionClients.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static com.jcr.GestionClients.MainActivity.CLIENT_ID;
import static com.jcr.GestionClients.MainActivity.CAT_ID;
import static com.jcr.GestionClients.MainActivity.SHEET_ID;
import static com.jcr.GestionClients.MainActivity.PRESTA_ID;

public class SheetAdapter extends RecyclerView.Adapter<SheetAdapter.SheetHolder> {

    Context         context;
    List<Sheet>     list = new ArrayList<>();
    Date            currentDate=null;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    boolean         deleteActivated;
    long            sum;
    SheetModel      sheetModel;

    public SheetAdapter(Context context, List<Sheet> list, boolean deleteActivated) {
        this.context = context;
        this.list=list;
        this.deleteActivated = deleteActivated;
    }

    @Override
    public SheetHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_item_group,parent,false);
        return new SheetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SheetHolder holder, int position) {
        final Sheet sheet = list.get(position);

        //Gestion du groupe date
        if (currentDate != null && currentDate.equals(sheet.getDate())) {
            holder.date.setVisibility(View.GONE);
            holder.groupPrice.setVisibility(View.GONE);
            holder.euro.setVisibility(View.GONE);

        }else{
            holder.euro.setVisibility(View.VISIBLE);
            holder.groupPrice.setVisibility(View.VISIBLE);
            holder.date.setVisibility(View.VISIBLE);
            currentDate = sheet.getDate();
            holder.date.setText(simpleDateFormat.format(currentDate));
            holder.groupPrice.setText(String.valueOf(sumGroupPrices(currentDate)));
        }
        holder.name.setText(sheet.getName());
        holder.price.setText(String.valueOf(sheet.getPrice()));

        //Gestion de la checkbox
        if (!deleteActivated) {
            holder.checkBox.setVisibility(View.GONE);
        } else {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(sheet.isSelected());
            holder.checkBox.setTag(list.get(position));
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Sheet sheet1 = (Sheet) holder.checkBox.getTag();
                    sheet1.setSelected(holder.checkBox.isChecked());
                    list.get(position).setSelected(holder.checkBox.isChecked());
                }
            });
        }

        //Gestion de l'icone payé
        holder.paid.setVisibility(View.GONE);
        holder.notPaid.setVisibility(View.GONE);
        if(sheet.isPaid) {
            holder.paid.setVisibility(View.VISIBLE);
        } else {
            holder.notPaid.setVisibility(View.VISIBLE);
        }

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHEET_ID = sheet.getID();
                Navigation
                        .findNavController(v)
                        .navigate(R.id.action_nav_sheet_to_nav_sheetAdd);
            }
        });

        //Gestion du nombre de prestations
        String str="";
        if (sheet.getSheetDetails().size()==0) {
            str = "Pas de prestation";
        } else if (sheet.getSheetDetails().size()==1) {
            str = sheet.getSheetDetails().get(0).getPrestation();
        } else {
            str = sheet.getSheetDetails().size() + " prestations";
        }
        holder.presta.setText(str);

        //Gestion de l'icone parrainage
        holder.sponsor.setVisibility(View.GONE);
        if (!sheet.getSponsor().equals("")) {
            holder.sponsor.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class SheetHolder extends  RecyclerView.ViewHolder {

        TextView date, name, presta, price,groupPrice, euro;
        CheckBox checkBox;
        CardView cardview;
        ImageView paid,notPaid,sponsor;

        public SheetHolder(View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.id_text_sheet_item_group);
            name=itemView.findViewById(R.id.id_text_sheet_item_mainItem);
            presta=itemView.findViewById(R.id.id_text_sheet_item_subItem);
            price=itemView.findViewById(R.id.id_text_sheet_item_price);
            checkBox=itemView.findViewById(R.id.id_check_sheet_item);
            cardview=itemView.findViewById(R.id.cardView);
            groupPrice = itemView.findViewById(R.id.id_text_sheet_groupItem);
            euro = itemView.findViewById(R.id.id_text_euro);
            paid = itemView.findViewById(R.id.im_paid);
            notPaid = itemView.findViewById(R.id.im_not_paid);
            sponsor = itemView.findViewById(R.id.im_sponsor);
        }
    }

    public List<Sheet> getSheetsList() {
        return list;
    }

    public long sumGroupPrices (Date date) {
        long sum = 0;
        for (int i=0;i<list.size();i++) {
            if (date.equals(list.get(i).getDate())) {
                sum = sum + list.get(i).getPrice();
            }
        }
        return sum;
    }

}
