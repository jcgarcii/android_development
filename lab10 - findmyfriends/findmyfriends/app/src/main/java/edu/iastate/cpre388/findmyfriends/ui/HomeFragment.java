package edu.iastate.cpre388.findmyfriends.ui;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothClass;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
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

import java.util.ArrayList;
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

    private BluetoothAdapter mBluetoothAdapter;
    private TextView friendList;
    private List<String> addresses;
    private List<Friend> tot;
    private List<String> toList;
    private LiveData<List<Friend>> db;
    private int iterations;

    private Handler handler;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity(), new FMFViewModelFactory(getActivity().getApplicationContext(),
                ((FMFApplication)getActivity().getApplication()).executor)).get(FMFViewModel.class);

        handler = new Handler();
        handler.post(runnable);
    }

    @SuppressLint("MissingPermission")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        friendList = root.findViewById(R.id.nearFriendsListTextView);
        addresses = new ArrayList<>();
        tot = new ArrayList<>();
        toList = new ArrayList<>();
        iterations = 0;

        db = viewModel.getFriendsList();


        final Observer<List<Friend>> getFriends = new Observer<List<Friend>>(){
            @Override
            public void onChanged(@Nullable final List<Friend> list){
                tot = list;
            }
        };
        db.observe(getViewLifecycleOwner(), getFriends);

        //viewModel.getFriendsList().observe(getViewLifecycleOwner(), getFriends);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.startDiscovery();

        //String s = String.valueOf(mBluetoothAdapter.getState());
        //Log.d("LOG", s);
        //makeDiscoverable();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getContext().registerReceiver(receiver, filter);

        return root;
    }

/*
    @SuppressLint("MissingPermission")
    public void makeDiscoverable(){
        int requestCode = 1;
        Intent discoverableIntent =
                new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3600);
        startActivityForResult(discoverableIntent, requestCode);

    }
*/

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(iterations == 10){
                addresses.clear();
                toList.clear();
            }
            updateList();
            iterations++;
            handler.postDelayed(this, 100);
        }
    };

    public void updateList(){
        String _set = "";

        if(addresses.size() == 0){
            _set = "You must have no friends, bum";
        }
        else{
            for(int j = 0; j < tot.size(); j++){
                Friend curr_tot = tot.get(j);
                String curr_totS = curr_tot.BTMacAddr.toString();

                for(int i = 0; i < addresses.size(); i++){
                    String curr = addresses.get(i).toLowerCase();

                    if(curr_totS.equals(curr)){
                        String l = curr_tot.name.toString();
                        toList.add(l);
                    }

                }
            }
            //set = String.format("This is a test: %s", tot.get(0).name.toString());
            if(toList.isEmpty()){
                _set = "No Friends Nearby, possible friends: " + addresses.toString();
            }
            else{
                _set = toList.toString();
            }
        }

        friendList.setText(_set);
    }




    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceHardwareAddress = device.getAddress(); // MAC address

                //if(iterations == 10){
                //    addresses.clear();
                //}
                addresses.add(deviceHardwareAddress);
                //updateList();
                //iterations++;

            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getContext().unregisterReceiver(receiver);
        binding = null;
    }
}