package com.jcr.GestionClients;

import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.jcr.GestionClients.ui.Sheet.SheetFragment;

import static com.jcr.GestionClients.MainActivity.fab;

public class fabAnimate {

    private static final int      DURATION_LONG = 200;
    private static final int      DURATION_SHORT = 100;
    private static final int      TRANSLATION_OUT = 300;
    private static final int      TRANSLATION_OVERSHOOT = -50;


    public static void validate() {
        fab.animate()
                .setDuration(DURATION_SHORT)
                .scaleX(1.1f)
                .scaleY(1.1f)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        fab.animate()
                                .setDuration(DURATION_SHORT)
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
                .setDuration(DURATION_LONG)
                .scaleX(0.0f)
                .scaleY(0.0f)
                .start();
    }

    private static void show(int drawableId) {

        fab.animate()
                .setDuration(DURATION_LONG)
                .translationX(TRANSLATION_OUT)
                .scaleY(0)
                .scaleX(0)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        fab.setImageResource(drawableId);
                        fab.setVisibility(View.VISIBLE);
                        fab.animate()
                                .setDuration(DURATION_LONG)
                                .scaleX(1.0f)
                                .scaleY(1.0f)
                                .translationX(TRANSLATION_OVERSHOOT)
                                .withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        fab.animate()
                                                .setDuration(DURATION_SHORT)
                                                .translationX(0)
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
                .setDuration(DURATION_LONG)
                .scaleX(1.1f)
                .scaleY(1.1f)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {

                        fab.setImageResource(drawableId);

                        //Shrink Animation
                        fab.animate()
                                .rotationBy(180)
                                .setDuration(DURATION_SHORT)
                                .scaleX(1)
                                .scaleY(1)
                                .start();

                    }
                })
                .start();

    }
}
