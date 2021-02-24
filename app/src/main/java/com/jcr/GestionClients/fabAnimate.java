package com.jcr.GestionClients;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.jcr.GestionClients.ui.Sheet.SheetFragment;

import static com.jcr.GestionClients.MainActivity.fab;

public class fabAnimate {

    public static void validate() {
        fab.animate()
                .setDuration(100)
                .scaleX(1.1f)
                .scaleY(1.1f)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        fab.animate()
                                .setDuration(100)
                                .scaleX(1.0f)
                                .scaleY(1.0f)
                                .start();
                    }
                })
                .start();
    }

    public static void showAdd (){
        show(R.drawable.ic_add);
    }
    public static void showEdit(){
        show(R.drawable.ic_edit);
    }
    public static void showDelete(){
        show(R.drawable.ic_delete);
    }

    public static void hide(){
        fab.animate()
                .setDuration(200)
                .translationY(300)
                .start();
    }

    private static void show(int drawableId) {

        fab.animate()
                .setDuration(200)
                .translationY(300)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        fab.setImageResource(drawableId);
                        fab.animate()
                                .setDuration(200)
                                .translationY(-50)
                                .withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        fab.animate()
                                                .setDuration(50)
                                                .translationY(0)
                                                .start();
                                    }
                                })
                                .start();
                    }
                });
    }
    public static void changeToEdit(){
        rotateInflate(R.drawable.ic_edit);
    }
    public static void changeToDel(){
        rotateInflate(R.drawable.ic_delete);
    }
    public static void changeToAdd(){
        rotateInflate(R.drawable.ic_add);
    }

    public static void rotateInflate(int drawableId){
        fab.animate()
                .rotationBy(180)
                .setDuration(100)
                .scaleX(1.1f)
                .scaleY(1.1f)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {

                        fab.setImageResource(drawableId);

                        //Shrink Animation
                        fab.animate()
                                .rotationBy(180)
                                .setDuration(100)
                                .scaleX(1)
                                .scaleY(1)
                                .start();

                    }
                })
                .start();

    }
}
