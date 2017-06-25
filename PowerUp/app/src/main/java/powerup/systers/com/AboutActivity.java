package powerup.systers.com;

import android.animation.Animator;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.HashSet;
import java.util.Random;


public class AboutActivity extends AppCompatActivity {

    HashSet<String> mines;
    LinearLayout linearLayout;
    int score = 0;
    int maxSelectionCount = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int methodFailurePercentage = 18;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minesweeper_game);
        linearLayout = (LinearLayout) findViewById(R.id.board);
        Random random = new Random();
        mines = new HashSet<>();
        while(mines.size()!= calculateRedMines(methodFailurePercentage))
            mines.add(PowerUpUtils.ID_REFERENCE+Math.abs(random.nextInt()%25));
    }

    private int calculateRedMines(int methodFailurePercentage) {
        int numCells = 25;
        return (methodFailurePercentage * 25) /100;
    }


    private void openedRedMine(ImageView imageView) {

        ImageView banner = (ImageView) findViewById(R.id.banner);
        banner.setImageDrawable(getResources().getDrawable(R.drawable.failure_banner));
        banner.setVisibility(View.VISIBLE);
        findViewById(R.id.continue_button).setVisibility(View.VISIBLE);
    }



    private void explode(ImageView imageView){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Rect viewRect = new Rect();
            imageView.getGlobalVisibleRect(viewRect);
            // create Explode transition with epicenter
            Explode explode = new Explode();
            explode.setEpicenterCallback(new Transition.EpicenterCallback() {
                @Override
                public Rect onGetEpicenter(Transition transition) {
                    return viewRect;
                }
            });
            explode.canRemoveViews();

            explode.setDuration(1000);

            TransitionManager.beginDelayedTransition(linearLayout, explode);
            while (linearLayout.getChildCount() != 0) {
                LinearLayout small = (LinearLayout) linearLayout.getChildAt(0);
                while (small.getChildCount() != 0)
                    small.removeViewAt(0);
                linearLayout.removeViewAt(0);
            }
        }
    }

    private void openedGreenMine(){

        //if all green unlocked without unlocking any red - then what to do
        // score effect + score animation + effect on adjacent mines (after discussing with team)
    }


    }




