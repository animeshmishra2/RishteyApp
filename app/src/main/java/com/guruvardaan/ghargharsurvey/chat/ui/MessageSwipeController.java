package com.guruvardaan.ghargharsurvey.chat.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.rishtey.agentapp.R;

public class MessageSwipeController extends ItemTouchHelper.Callback {

    private Context context;
    private SwipeControllerActions swipeControllerActions;
    private Drawable imageDrawable;
    private Drawable shareRound;
    private RecyclerView.ViewHolder currentItemViewHolder;
    private View mView;
    private float dX = 0f;
    private float replyButtonProgress = 0;
    private long lastReplyButtonAnimationTime = 0;
    private boolean swipeBack = false;
    private boolean isVibrate = false;
    private boolean startTracking = false;

    public MessageSwipeController(Context context, SwipeControllerActions swipeControllerActions) {
        this.context = context;
        this.swipeControllerActions = swipeControllerActions;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        mView = viewHolder.itemView;
        imageDrawable = ContextCompat.getDrawable(context, R.drawable.ic_reply_black_24dp);
        shareRound = ContextCompat.getDrawable(context, R.drawable.ic_round_shape);
        return makeMovementFlags(0, ItemTouchHelper.RIGHT);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if (swipeBack) {
            swipeBack = false;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            setTouchListener(recyclerView, viewHolder);
        }

        if (mView.getTranslationX() < convertTodp(130) || dX < this.dX) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            this.dX = dX;
            startTracking = true;
        }
        currentItemViewHolder = viewHolder;
        drawReplyButton(c);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTouchListener(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        recyclerView.setOnTouchListener((v, event) -> {
            swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
            if (swipeBack) {
                if (Math.abs(mView.getTranslationX()) >= convertTodp(100)) {
                    swipeControllerActions.showReplyUI(viewHolder.getAdapterPosition());
                }
            }
            return false;
        });
    }

    private void drawReplyButton(Canvas canvas) {
        if (currentItemViewHolder == null) {
            return;
        }
        float translationX = mView.getTranslationX();
        long newTime = System.currentTimeMillis();
        long dt = Math.min(17, newTime - lastReplyButtonAnimationTime);
        lastReplyButtonAnimationTime = newTime;
        boolean showing = translationX >= convertTodp(30);
        if (showing) {
            if (replyButtonProgress < 1.0f) {
                replyButtonProgress += dt / 180.0f;
                if (replyButtonProgress > 1.0f) {
                    replyButtonProgress = 1.0f;
                } else {
                    mView.invalidate();
                }
            }
        } else if (translationX <= 0.0f) {
            replyButtonProgress = 0f;
            startTracking = false;
            isVibrate = false;
        } else {
            if (replyButtonProgress > 0.0f) {
                replyButtonProgress -= dt / 180.0f;
                if (replyButtonProgress < 0.1f) {
                    replyButtonProgress = 0f;
                } else {
                    mView.invalidate();
                }
            }
        }
        int alpha;
        float scale;
        if (showing) {
            scale = replyButtonProgress <= 0.8f ? 1.2f * (replyButtonProgress / 0.8f) :
                    1.2f - 0.2f * ((replyButtonProgress - 0.8f) / 0.2f);
            alpha = (int) Math.min(255f, 255 * (replyButtonProgress / 0.8f));
        } else {
            scale = replyButtonProgress;
            alpha = (int) Math.min(255f, 255 * replyButtonProgress);
        }
        shareRound.setAlpha(alpha);

        imageDrawable.setAlpha(alpha);
        if (startTracking) {
            if (!isVibrate && mView.getTranslationX() >= convertTodp(100)) {
                mView.performHapticFeedback(
                        HapticFeedbackConstants.KEYBOARD_TAP,
                        HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
                );
                isVibrate = true;
            }
        }

        int x = (int) (mView.getTranslationX() > convertTodp(130) ? convertTodp(130) / 2 : (mView.getTranslationX() / 2));
        float y = mView.getTop() + mView.getMeasuredHeight() / 2;
        shareRound.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.colorE), PorterDuff.Mode.MULTIPLY));

        shareRound.setBounds(
                (int) (x - convertTodp(18) * scale),
                (int) (y - convertTodp(18) * scale),
                (int) (x + convertTodp(18) * scale),
                (int) (y + convertTodp(18) * scale)
        );
        shareRound.draw(canvas);
        imageDrawable.setBounds(
                (int) (x - convertTodp(12) * scale),
                (int) (y - convertTodp(11) * scale),
                (int) (x + convertTodp(12) * scale),
                (int) (y + convertTodp(10) * scale)
        );
        imageDrawable.draw(canvas);
        shareRound.setAlpha(255);
        imageDrawable.setAlpha(255);
    }

    private int convertTodp(int pixel) {
        return AndroidUtils.dp(pixel, context);
    }
}
