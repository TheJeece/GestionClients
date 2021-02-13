package com.jcr.GestionClients.ui.Sheet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jcr.GestionClients.R;

import java.util.ArrayList;
import java.util.List;

public class SheetDetailAdapter extends RecyclerView.Adapter<SheetDetailAdapter.SheetHolder> {

    Context context;
    List<SheetDetail> list = new ArrayList<>();
    String currentCategory ="";
    boolean deleteActivated;


    public SheetDetailAdapter(Context context, List<SheetDetail> list, boolean deleteActivated) {
        this.context = context;
        this.list=list;
        this.deleteActivated = deleteActivated;
    }

    @Override
    public SheetHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_group,parent,false);

        return new SheetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SheetHolder holder, int position) {
        final SheetDetail sheetDetail = list.get(position);

        //Gestion des suppressions
        if (deleteActivated) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(sheetDetail.isSelected());
            holder.checkBox.setTag(list.get(position));
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SheetDetail sheetDetail1 = (SheetDetail) holder.checkBox.getTag();
                    sheetDetail1.setSelected(holder.checkBox.isChecked());
                    list.get(position).setSelected(holder.checkBox.isChecked());
                }
            });
        } else {
            holder.checkBox.setVisibility(View.GONE);
        }

        //Gestion des prix par categorie
        if (currentCategory.equals(sheetDetail.getCategory())) {
            holder.category.setVisibility(View.GONE);
            holder.totalPrice.setVisibility(View.GONE);
        }else{
            holder.category.setVisibility(View.VISIBLE);
            holder.totalPrice.setVisibility(View.VISIBLE);
            currentCategory = sheetDetail.getCategory();
            holder.category.setText(currentCategory);
            holder.totalPrice.setText(sumCatPrice(sheetDetail.getCategory()) + " €" );
        }

        holder.prestation.setText(sheetDetail.getPrestation());
        holder.price.setText(sheetDetail.getPrice() +" €");

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class SheetHolder extends  RecyclerView.ViewHolder {

        TextView category, prestation, price, totalPrice;
        CheckBox checkBox;

        public SheetHolder(View itemView) {
            super(itemView);
            category=itemView.findViewById(R.id.id_list_group);
            prestation=itemView.findViewById(R.id.id_list_item1);
            totalPrice = itemView.findViewById(R.id.id_list_item3);
            price=itemView.findViewById(R.id.id_list_item2);
            checkBox=itemView.findViewById(R.id.id_check_list);
        }
    }

    public List<SheetDetail> getSheetsList() {
        return list;
    }

    public long sumCatPrice (String Category) {
        long sum = 0;
        for (int i=0;i<list.size();i++) {
            if (Category.equals(list.get(i).getCategory())) {
                sum = sum + list.get(i).getPrice();
            }
        }
        return sum;
    }
}
