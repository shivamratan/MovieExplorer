package com.ratanapps.movieexplorer.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.ratanapps.movieexplorer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class upcoming extends Fragment {


    public upcoming() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_upcoming, container, false);

        ImageView imageView=(ImageView)view.findViewById(R.id.imageView_upcoming);

        final Animation animationrotate= AnimationUtils.loadAnimation(getActivity(),R.anim.rotate_anim);
        imageView.startAnimation(animationrotate);
        return view;
    }

}
