package com.croissant.croissant.ActivityEvent;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

/**
 * Created by santiago on 16/06/2016.
 */
@TargetApi(Build.VERSION_CODES.M)
public class HidingScrollListener extends RecyclerView.OnScrollListener {

    RelativeLayout contentToHide;
    RecyclerView contentToShow;

    int oldy = 0;
    int differences = 0;
    int height = 0;
    int margin = 100;

    public HidingScrollListener(RelativeLayout contentHide, RecyclerView contentShow){
        this.contentToHide = contentHide;
        this.contentToShow = contentShow;
        height = contentToHide.getHeight();
        contentToShow.animate().translationY(height).setInterpolator(new DecelerateInterpolator(2)).start();
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        differences = difference(dy, oldy);
        super.onScrolled(recyclerView, dx, dy);
        if(differences >margin){
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) contentToHide.getLayoutParams();
            int fabBottomMargin = lp.bottomMargin;
            contentToHide.animate().translationY(-(height+fabBottomMargin))
            .setInterpolator(new DecelerateInterpolator(2)).start();
            contentToShow.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();

        }else if(differences <-margin){
            contentToHide.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            contentToShow.animate().translationY(height).setInterpolator(new DecelerateInterpolator(2)).start();
        }
        oldy = dy;
    }

    private int difference(int y, int yold){
        int dif = 0;
        if(yold<y) dif = y+yold;
        else dif = yold+y;
        return dif;
    }
}
