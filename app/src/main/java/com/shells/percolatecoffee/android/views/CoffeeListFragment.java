package com.shells.percolatecoffee.android.views;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shells.percolatecoffee.BuildConfig;
import com.shells.percolatecoffee.R;
import com.shells.percolatecoffee.android.adapters.CoffeeListAdapter;
import com.shells.percolatecoffee.api.MemberClient;
import com.shells.percolatecoffee.api.models.CoffeeListResource;
import com.shells.percolatecoffee.api.models.CoffeeResource;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CoffeeListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoffeeListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private final static String TAG = CoffeeListFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SwipeRefreshLayout mSwipeLayout;
    private static int retroRetryCount = 0;

    ArrayList<CoffeeListResource> coffeeListResources;
    ArrayList<CoffeeResource> coffeeResources;
    Boolean newPageLoaded;

    /* UI */
    View view;

    ProgressBar progressBar;
    TextView textAction;

    public static CoffeeListFragment newInstance(String param1, String param2) {
        CoffeeListFragment fragment = new CoffeeListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public CoffeeListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_coffee_list, container, false);
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        textAction = (TextView) view.findViewById(R.id.text_empty);

        // Set the adapter
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        //mListView.addFooterView(footer);

        init();
        return view;
    }

    private void init() {
        coffeeListResources = new ArrayList<>();
        coffeeResources = new ArrayList<>();
        newPageLoaded = false;
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(R.color.material_teal_500, R.color.material_teal_100, R.color.material_teal_400, R.color.material_teal_A700);

        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        mAdapter = new CoffeeListAdapter(getActivity(), coffeeResources);
        mRecyclerView.setAdapter(mAdapter);
//        final ItemClickSupport itemClick = ItemClickSupport.addTo(mRecyclerView);
//        itemClick.setOnItemClickListener(clickListener);
        //mRecyclerView.setOnScrollListener(scrollListener);

        onRefresh();
    }

    private void getCoffeeAll(final String pageNumber) {
        MemberClient.getApiClient().getCoffeeAll(BuildConfig.API_TOKEN, new Callback<CoffeeListResource>() {
            @Override
            public void success(CoffeeListResource coffeeListResource, Response response) {
                // Add to the page lists
                coffeeListResources.add(coffeeListResource);
                // stash all the data in our backing store
                coffeeResources.addAll(coffeeListResource.coffeeResources);
                // notify the adapter
                mAdapter.notifyDataSetChanged();
                mSwipeLayout.setRefreshing(false);

                // Done loading for now
                if (coffeeResources.isEmpty()) {
                    textAction.setText("No Coffee Today!");
                    textAction.setVisibility(View.VISIBLE);
                } else {
                    textAction.setVisibility(View.GONE);
                }
                progressBar.setVisibility(View.GONE);
                newPageLoaded = false;
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                if (retrofitError.isNetworkError()) {
                    if (retroRetryCount < 3) {
                        ++retroRetryCount;
                        getCoffeeAll(pageNumber);
                    } else {
                        retroRetryCount = 0;
                        mSwipeLayout.setRefreshing(false);
                    }
                } else {
                    if (coffeeResources.isEmpty()) {
                        textAction.setVisibility(View.VISIBLE);
                    } else {
                        textAction.setVisibility(View.GONE);
                    }
                    mSwipeLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                newPageLoaded = false;
                coffeeListResources.clear();
                coffeeResources.clear();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                        textAction.setVisibility(View.GONE);
                    }
                });
                getCoffeeAll("1");
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
            }
        }.execute();
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        public void onCoffeeSelected(String id);
    }

}
