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
import static com.jcr.GestionClients.MainActivity.CLIENT_ID;
import static com.jcr.GestionClients.MainActivity.CAT_ID;
import static com.jcr.GestionClients.MainActivity.SHEET_ID;

import java.util.List;


public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CatHolder> {

    Context context;
    boolean deleteActivated;
    List<Category> categories;
    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;

    public CategoriesAdapter(Context context, List<Category> categories, boolean deleteActivated) {
        this.context = context;
        this.categories=categories;
        this.deleteActivated = deleteActivated;
    }

    @Override
    public CatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_item_big,parent,false);
        return new CatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatHolder holder, int position) {
        final Category category = categories.get(position);

        //Gestion de la checkbox
        if (!deleteActivated) {
            holder.checkBox.setVisibility(View.GONE);
        } else {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(category.isSelected());
            holder.checkBox.setTag(categories.get(position));
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Category category1 = (Category) holder.checkBox.getTag();
                    category1.setSelected(holder.checkBox.isChecked());
                    categories.get(position).setSelected(holder.checkBox.isChecked());
                }
            });
        }
        holder.name.setText(category.name);
        holder.image.setImageBitmap(category.image);


        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CAT_ID = category.getId();
                if (!deleteActivated) {
                    NavController navController = Navigation.findNavController(v);
                    navController.navigate(R.id.action_nav_ListeCategories_to_nav_ListePrestations);
                }else{
                    NavController navController = Navigation.findNavController(v);
                    navController.navigate(R.id.action_nav_Prestations_to_nav_EditCat);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class CatHolder extends  RecyclerView.ViewHolder {

        TextView name;
        CheckBox checkBox;
        CardView cardview;
        ImageView image;

        public CatHolder(View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.id_text_cat);
            checkBox=itemView.findViewById(R.id.id_cat_check);
            cardview=itemView.findViewById(R.id.id_cv_Category);
            image=itemView.findViewById(R.id.id_image_cat);
        }
    }

    public List<Category> getCategories() {
        return categories;
    }



}
