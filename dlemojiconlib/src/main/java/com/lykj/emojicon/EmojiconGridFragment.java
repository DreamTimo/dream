/*
 * Copyright 2014 Hieu Rocker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lykj.emojicon;

import com.luyz.dlemojiconlib.R;
import com.lykj.emojicon.emoji.Emojicon;
import com.lykj.emojicon.emoji.People;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

/**
 * @author Hieu Rocker (rockerhieu@gmail.com)
 */
public class EmojiconGridFragment extends Fragment implements AdapterView.OnItemClickListener {
    private OnEmojiconClickedListener mOnEmojiconClickedListener;
    private Emojicon[] mData;
    private boolean mUseSystemDefault = false;

    private static final String USE_SYSTEM_DEFAULT_KEY = "useSystemDefaults";
    private static final String EMOJICONS_KEY = "emojicons";

    public static EmojiconGridFragment newInstance(Emojicon[] emojicons) {
        return newInstance(emojicons, false);
    }

    public static EmojiconGridFragment newInstance(Emojicon[] emojicons, boolean useSystemDefault) {
        EmojiconGridFragment emojiGridFragment = new EmojiconGridFragment();
        Bundle args = new Bundle();
        args.putParcelableArray(EMOJICONS_KEY, emojicons);
        args.putBoolean(USE_SYSTEM_DEFAULT_KEY, useSystemDefault);
        emojiGridFragment.setArguments(args);
        return emojiGridFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.emojicon_grid, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        GridView gridView = (GridView) view.findViewById(R.id.Emoji_GridView);
        Bundle bundle = getArguments();
        if (bundle == null) {
            mData = People.DATA;
            mUseSystemDefault = false;
        } else {
            Parcelable[] parcels = bundle.getParcelableArray(EMOJICONS_KEY);
            mData = new Emojicon[parcels.length];
            for (int i = 0; i < parcels.length; i++) {
                mData[i] = (Emojicon) parcels[i];
            }
            mUseSystemDefault = bundle.getBoolean(USE_SYSTEM_DEFAULT_KEY);
        }
        gridView.setAdapter(new EmojiAdapter(view.getContext(), mData, mUseSystemDefault));
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArray(EMOJICONS_KEY, mData);
    }

    @SuppressWarnings("deprecation")
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnEmojiconClickedListener) {
            mOnEmojiconClickedListener = (OnEmojiconClickedListener) activity;
        } else if (getParentFragment() instanceof OnEmojiconClickedListener) {
            mOnEmojiconClickedListener = (OnEmojiconClickedListener) getParentFragment();
        } else {
            throw new IllegalArgumentException(activity + " must implement interface " + OnEmojiconClickedListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        mOnEmojiconClickedListener = null;
        super.onDetach();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    	
    	Emojicon tempItem = (Emojicon) parent.getItemAtPosition(position);
    	if(tempItem!=null){
    		if(tempItem.isDel()){
    			if(mOnEmojiconClickedListener!=null){
    				mOnEmojiconClickedListener.onEmojiconBackspaceClicked(null);
    			}
    		}else{
		        if (mOnEmojiconClickedListener != null) {
		            mOnEmojiconClickedListener.onEmojiconClicked(tempItem);
		        }
    		}
    	}
    }

    public interface OnEmojiconClickedListener {
        void onEmojiconClicked(Emojicon emojicon);
        void onEmojiconBackspaceClicked(View v);
    }
}
