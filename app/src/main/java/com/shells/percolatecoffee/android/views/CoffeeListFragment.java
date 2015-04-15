package com.shells.percolatecoffee.android.views;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.shells.percolatecoffee.BuildConfig;
import com.shells.percolatecoffee.R;
import com.shells.percolatecoffee.android.adapters.CoffeeListAdapter;
import com.shells.percolatecoffee.android.adapters.DividerItemDecoration;
import com.shells.percolatecoffee.android.adapters.RecyclerItemClickListener;
import com.shells.percolatecoffee.api.MemberClient;
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


    private static int retroRetryCount = 0;

    ArrayList<CoffeeResource> coffeeResourcesList;

    /* UI */
    View view;
    TextView textAction;
    SwipeRefreshLayout mSwipeLayout;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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
        textAction = (TextView) view.findViewById(R.id.text_empty);
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        init();
        return view;
    }

    private void init() {
        coffeeResourcesList = new ArrayList<>();
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(R.color.material_teal_500, R.color.material_teal_100, R.color.material_teal_400, R.color.material_teal_A700);

        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(), "Yo: " + coffeeResourcesList.get(position).name, Toast.LENGTH_SHORT).show();
                mListener.onCoffeeSelected(coffeeResourcesList.get(position).id);
            }
        }));

        mAdapter = new CoffeeListAdapter(getActivity(), coffeeResourcesList);
        mRecyclerView.setAdapter(mAdapter);

        onRefresh();
    }

    private void getCoffeeAll(final String pageNumber) {
        MemberClient.getApiClient().getCoffeeAll(BuildConfig.API_TOKEN, new Callback<ArrayList<CoffeeResource>>() {
            @Override
            public void success(ArrayList<CoffeeResource> coffeeResources, Response response) {
                coffeeResourcesList.addAll(coffeeResources);
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
                    if (coffeeResourcesList.isEmpty()) {
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
                coffeeResourcesList.clear();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                        textAction.setVisibility(View.VISIBLE);
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
        void onCoffeeSelected(String id);
    }

}
