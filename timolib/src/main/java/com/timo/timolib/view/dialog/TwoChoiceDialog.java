package com.timo.timolib.view.dialog;

import android.view.View;
import android.widget.TextView;

import com.timo.timolib.R;
import com.timo.timolib.base.BaseBottomDialog;

/**
 * Created by 蔡永汪 on 2017/8/25.
 */

public class TwoChoiceDialog extends BaseBottomDialog {
    private TextView choice1;
    private TextView choice2;
    private TextView cancel;
    @Override
    public int getLayoutRes() {
        return R.layout.dialog_two_choices;
    }

    private TwoChoiceListener listener;

    public void setListener(TwoChoiceListener listener) {
        this.listener = listener;
    }

    @Override
    public void bindView(View v) {
        choice1= (TextView) v.findViewById(R.id.choice_1);
        choice2= (TextView) v.findViewById(R.id.choice_2);
        cancel= (TextView) v.findViewById(R.id.cancel);
        choice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.choice1();
                }
            }
        });
        choice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.choice2();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.cancel();
                }
            }
        });
    }

    public interface TwoChoiceListener {
        void choice1();

        void choice2();

        void cancel();
    }
}
