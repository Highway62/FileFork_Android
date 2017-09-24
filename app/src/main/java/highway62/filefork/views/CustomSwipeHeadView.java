package highway62.filefork.views;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.reginald.swiperefresh.CustomSwipeRefreshLayout;

import highway62.filefork.R;

/**
 * Created by Highway62 on 01/06/2016.
 */
public class CustomSwipeHeadView extends LinearLayout implements CustomSwipeRefreshLayout.CustomSwipeRefreshHeadLayout {

    private static final boolean DEBUG = false;

    private static final SparseArray<String> STATE_MAP = new SparseArray<>();
    private ViewGroup mContainer;
    private TextView mMainTextView;
    private TextView mSubTextView;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private int mState = -1;

    {
        STATE_MAP.put(0, "STATE_NORMAL");
        STATE_MAP.put(1, "STATE_READY");
        STATE_MAP.put(2, "STATE_REFRESHING");
        STATE_MAP.put(3, "STATE_COMPLETE");
    }


    public CustomSwipeHeadView(Context context){
        super(context);
        setupLayout();
    }

    private void setupLayout() {
        ViewGroup.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContainer = (LinearLayout) LayoutInflater.from(getContext()).inflate(com.reginald.swiperefresh.R.layout.default_swiperefresh_head_layout, null);
        addView(mContainer, lp);
        setGravity(Gravity.BOTTOM);
        mImageView = (ImageView) findViewById(com.reginald.swiperefresh.R.id.default_header_arrow);
        mImageView.setVisibility(View.INVISIBLE);
        mMainTextView = (TextView) findViewById(com.reginald.swiperefresh.R.id.default_header_textview);
        mSubTextView = (TextView) findViewById(com.reginald.swiperefresh.R.id.default_header_time);
        mProgressBar = (ProgressBar) findViewById(com.reginald.swiperefresh.R.id.default_header_progressbar);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStateChange(CustomSwipeRefreshLayout.State state, CustomSwipeRefreshLayout.State lastState) {

        if (DEBUG) {
            Log.d("csrh", "onStateChange state = " + state + ", lastState = " + lastState);
        }
        int stateCode = state.getRefreshState();
        int lastStateCode = lastState.getRefreshState();
        float percent = state.getPercent();

        switch (stateCode) {
            case CustomSwipeRefreshLayout.State.STATE_NORMAL:
                if (percent > 0.5f) {
                    //setImageRotation((percent - 0.5f) * 180 / 0.5f);
                    mMainTextView.setTextColor(Color.argb(0xff, 0, (int) ((percent - 0.5f) * 140 / 0.5f), 0));
                } else {
                    //setImageRotation(0);
                    mMainTextView.setTextColor(Color.BLACK);
                }

                if (stateCode != lastStateCode) {
                    //mImageView.setVisibility(View.VISIBLE);
                    //mProgressBar.setVisibility(View.INVISIBLE);
                    mMainTextView.setText(R.string.csr_text_state_normal);
                }
                break;
            case CustomSwipeRefreshLayout.State.STATE_READY:
                if (stateCode != lastStateCode) {
                    //mImageView.setVisibility(View.VISIBLE);
                    //mProgressBar.setVisibility(View.INVISIBLE);
                    //setImageRotation(180);
                    mMainTextView.setText(R.string.csr_text_state_ready);
                    mMainTextView.setTextColor(getContext().getResources().getColor(R.color.Green));
                }
                break;
            case CustomSwipeRefreshLayout.State.STATE_REFRESHING:
                if (stateCode != lastStateCode) {
                    //mImageView.clearAnimation();
                    //mImageView.setVisibility(View.INVISIBLE);
                    //mProgressBar.setVisibility(View.VISIBLE);
                    mMainTextView.setText(R.string.csr_text_state_refresh);
                    mMainTextView.setTextColor(getContext().getResources().getColor(R.color.Green));
                }
                break;

            case CustomSwipeRefreshLayout.State.STATE_COMPLETE:
                if (stateCode != lastStateCode) {
                    //mImageView.setVisibility(View.INVISIBLE);
                    //mProgressBar.setVisibility(View.INVISIBLE);
                    if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.HONEYCOMB) {
                        Integer colorFrom = getContext().getResources().getColor(R.color.Green);
                        Integer colorTo = Color.BLACK;
                        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animator) {
                                mMainTextView.setTextColor((Integer) animator.getAnimatedValue());
                            }

                        });
                        colorAnimation.setDuration(1000);
                        colorAnimation.start();
                    } else {
                        mMainTextView.setTextColor(Color.BLACK);
                    }
                }
                mMainTextView.setText(R.string.csr_text_state_complete);
                break;
            default:
        }
        //mSubTextView.setText(String.format("state: %s, percent: %1.4f", STATE_MAP.get(stateCode), percent));
        //mState = stateCode;
    }

    /*
    private void setImageRotation(float rotation) {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            mImageView.setRotation(rotation);
        } else {
            if (mImageView.getTag() == null){
                mImageView.setTag(0f);
            }
            mImageView.clearAnimation();
            Float lastDegree = (Float)mImageView.getTag();
            RotateAnimation rotate = new RotateAnimation(lastDegree, rotation,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            mImageView.setTag(rotation);
            rotate.setFillAfter(true);
            mImageView.startAnimation(rotate);
        }
    }*/
}
