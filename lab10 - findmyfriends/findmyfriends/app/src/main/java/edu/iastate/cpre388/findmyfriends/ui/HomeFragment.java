package edu.iastate.cpre388.findmyfriends.ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothClass;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import edu.iastate.cpre388.findmyfriends.Friend;
import edu.iastate.cpre388.findmyfriends.FriendDatabase;
import edu.iastate.cpre388.findmyfriends.MainActivity;
import edu.iastate.cpre388.findmyfriends.R;
import edu.iastate.cpre388.findmyfriends.FMFApplication;
import edu.iastate.cpre388.findmyfriends.FMFViewModel;
import edu.iastate.cpre388.findmyfriends.FMFViewModelFactory;
import edu.iastate.cpre388.findmyfriends.databinding.FragmentHomeBinding;

/* TODO: implement searching for and displaying nearby friends */

public class HomeFragment extends Fragment{
    private static final String TAG = "HomeFragment";
    private FMFViewModel viewModel;
    private FragmentHomeBinding binding;
    private TextView friendList;
    private List<String> addresses;
    private List<Friend> friends;
    private LiveData<List<Friend>> allFriends;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity(), new FMFViewModelFactory(getActivity().getApplicationContext(),
                ((FMFApplication)getActivity().getApplication()).executor)).get(FMFViewModel.class);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        friendList = root.findViewById(R.id.nearFriendsListTextView);


        final Observer<List<String>> addressObserver = new Observer<List<String>>(){
            @Override
            public void onChanged(@Nullable final List<String> steps){
                String set = updateList();
                friendList.setText(set);
                viewModel.clearAddresses();
            }
        };
        viewModel.getCurrentAddr().observe(getViewLifecycleOwner(), addressObserver);


        return root;
    }


    public String updateList(){
        String _set = "";
        allFriends = viewModel.getFriendsList();
        friends = allFriends.getValue();

        for(Friend friend: friends){
            for(String i : addresses){
                i.toLowerCase();
                if(i == friend.BTMacAddr){
                    _set = _set + friend.toString() + "\n";
                }
            }
        }
        return _set;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}