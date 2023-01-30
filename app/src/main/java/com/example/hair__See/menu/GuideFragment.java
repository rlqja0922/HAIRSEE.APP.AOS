package com.example.hair__See.menu;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.hair__See.R;
import com.example.hair__See.utils.OnSingleClickListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GuideFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuideFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView camerstart_iv;
    private Context mcontext;
    public GuideFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GuideFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GuideFragment newInstance(String param1, String param2) {
        GuideFragment fragment = new GuideFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //camerstart_iv
        View view = inflater.inflate(R.layout.fragment_guide, container, false);
        camerstart_iv = view.findViewById(R.id.camerstart_iv);
        mcontext = getContext();
        camerstart_iv.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.Fragment,new CameraFragment()).commit();
            }
        });
        return view;
    }
}