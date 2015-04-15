package com.shells.percolatecoffee.android.views;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shells.percolatecoffee.BuildConfig;
import com.shells.percolatecoffee.R;
import com.shells.percolatecoffee.android.adapters.CoffeeListAdapter;
import com.shells.percolatecoffee.android.adapters.DividerItemDecoration;
import com.shells.percolatecoffee.android.adapters.RecyclerItemClickListener;
import com.shells.percolatecoffee.api.MemberClient;
import com.shells.percolatecoffee.api.models.CoffeeResource;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CoffeeDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoffeeDetailFragment extends Fragment{

    public final static String ID = "param_id";

    private OnFragmentInteractionListener mListener;
    private String mId;

    /* UI */
    View view;
    TextView tvName;
    TextView tvDesc;
    ImageView imgCoffee;
    TextView tvUpdate;


    public static CoffeeDetailFragment newInstance(String id) {
        CoffeeDetailFragment fragment = new CoffeeDetailFragment();
        Bundle args = new Bundle();
        args.putString(ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    public CoffeeDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getString(ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_coffee_detail, container, false);
        tvName = (TextView) view.findViewById(R.id.tv_coffee_name);
        tvDesc = (TextView) view.findViewById(R.id.tv_coffee_desc);
        imgCoffee = (ImageView) view.findViewById(R.id.img_coffee_image);
        tvUpdate = (TextView) view.findViewById(R.id.tv_coffee_updated);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onCoffeeLoaded(Context context, CoffeeResource resource){
        tvName.setText(resource.name);
        tvDesc.setText(resource.desc);
        Picasso.with(context).load(resource.image_url).into(imgCoffee);
        tvUpdate.setText(resource.last_updated_at);
    }

    public interface OnFragmentInteractionListener {
        public void onCoffeeSelected(String id);
    }

}
