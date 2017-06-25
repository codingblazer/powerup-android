package powerup.systers.com;

import android.animation.Animator;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Random;


public class AvatarActivity extends AppCompatActivity {

    HashSet<String> mines;
    LinearLayout linearLayout;
    ImageView banner, imageView ;
    int round=0, score =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minesweeper_game);
        linearLayout = (LinearLayout) findViewById(R.id.board);
        Random random = new Random();
        mines = new HashSet<>();
        while(mines.size()!=4) {
            int n = Math.abs(random.nextInt() % 25);
            mines.add(PowerUpUtils.ID_REFERENCE + n);
            Log.e("sachin",""+n);
        }
        round = 1;
    }

    public void openMine(final View view) {
        imageView = (ImageView) view;
        imageView.setRotationY(0f);
        imageView.animate().setDuration(100).rotationY(90f).setListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animator) {
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                for (int id : PowerUpUtils.minesViews)
                    findViewById(id).setClickable(false);
                if(mines.contains(view.getResources().getResourceName(view.getId()))){
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.red_star));
                    openedRedMine(imageView);
                }
                else{
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.green_star));
                    openedGreenMine(imageView);
                }

                imageView.setRotationY(270f);
                imageView.animate().rotationY(360f).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {

                        uncoverAllMines(imageView);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });

            }
            @Override
            public void onAnimationCancel(Animator animator) {
            }
            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
    }

    public void openedRedMine(ImageView imageView){

        Toast.makeText(this,"+1",Toast.LENGTH_LONG).show();
//        showPointAnimation(-1);
        score --;
//        uncoverAllMines(imageView);
//        showBanner(1);

    }

    public void openedGreenMine(ImageView imageView){
        Toast.makeText(this,"-1",Toast.LENGTH_LONG).show();
//        showPointAnimation(1);
        score ++;

//        showBanner(0);

    }


    public void showPointAnimation(int score){
        //use thread for creating some delay before and after
    }

    public void uncoverAllMines(final ImageView imageView){

        for(final int id: PowerUpUtils.minesViews){
            final ImageView iv = (ImageView) findViewById(id);
            iv.setRotationY(0f);
            iv.animate().setDuration(100).setStartDelay(100).rotationY(90f).setListener(new Animator.AnimatorListener()
            {
                @Override
                public void onAnimationStart(Animator animator) {
                }
                @Override
                public void onAnimationEnd(Animator animation) {



                    iv.setRotationY(270f);
                    iv.animate().rotationY(360f).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {

                            showBanner(0);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                }
                @Override
                public void onAnimationCancel(Animator animator) {
                }
                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });
        }
        //add some delay after this and flip animation


    }

    public void showBanner(int result){
        banner = (ImageView) findViewById(R.id.banner);
        if (result == 1){
            banner.setImageDrawable(getResources().getDrawable(R.drawable.failure_banner));
        }
        else {
            banner.setImageDrawable(getResources().getDrawable(R.drawable.success_banner));
        }
        banner.setVisibility(View.VISIBLE);
        findViewById(R.id.continue_button).setVisibility(View.VISIBLE);
        //fade the background

    }

    public void continuePressed(View view){
        //add continue button animation of dancing
        //add button pressed state in XMl animation of material design
        //change the look of the continue button
        hideBanner();
        nextRound();


    }

    public void hideBanner(){

        banner.setVisibility(View.GONE);
        findViewById(R.id.continue_button).setVisibility(View.GONE);
        //restore the faded background
    }

    public void nextRound(){
        round ++;
        if (round == 6){
            explodeAnimation();
            showFinalScore();
        }
        else if (round <= 5){
            for (int id : PowerUpUtils.minesViews){
                ImageView iv = (ImageView) findViewById(id);
                iv.setImageDrawable(getResources().getDrawable(R.drawable.yellow_star));
                //add flip animation
                iv.setClickable(true);
            }

        }

    }

    public void explodeAnimation(){
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

            explode.setDuration(1000).setStartDelay(100);

            TransitionManager.beginDelayedTransition(linearLayout, explode);
            while (linearLayout.getChildCount()!=0){
                LinearLayout small = (LinearLayout) linearLayout.getChildAt(0);
                while (small.getChildCount()!=0)
                    small.removeViewAt(0);
                linearLayout.removeViewAt(0);
            }
        }
    }
    public void showFinalScore(){
        //show final score inside the mines big box
        findViewById(R.id.continue_button).setVisibility(View.VISIBLE);
    }











}


