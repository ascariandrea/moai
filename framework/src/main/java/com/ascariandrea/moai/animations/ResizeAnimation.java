package com.ascariandrea.moai.animations;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ResizeAnimation extends Animation {
    final int startHeight;
    private final int targetWidth;
    final int targetHeight;
    View view;

    public ResizeAnimation(View view, int targetWidth, int targetHeight) {
        this.view = view;
        this.targetWidth = targetWidth;
        this.targetHeight = targetHeight;
        startHeight = view.getHeight();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {

        view.getLayoutParams().height = (int) (startHeight + (targetHeight - startHeight) * interpolatedTime);
        view.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}