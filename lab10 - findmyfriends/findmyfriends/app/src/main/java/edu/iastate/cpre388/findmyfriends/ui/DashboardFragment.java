package edu.iastate.cpre388.findmyfriends.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import edu.iastate.cpre388.findmyfriends.FMFApplication;
import edu.iastate.cpre388.findmyfriends.FMFViewModel;
import edu.iastate.cpre388.findmyfriends.FMFViewModelFactory;
import edu.iastate.cpre388.findmyfriends.Friend;
import edu.iastate.cpre388.findmyfriends.R;
import edu.iastate.cpre388.findmyfriends.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FMFViewModel viewModel;
    private FragmentDashboardBinding binding;

    ActivityResultLauncher<Intent> launcher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK) {
                            String name = result.getData().getStringExtra(AddFriendDialogActivity.NEW_FRIEND_NAME);
                            String addr = result.getData().getStringExtra(AddFriendDialogActivity.NEW_FRIEND_MAC_ADDR);
                            Friend friend = new Friend(name, addr.toLowerCase());
                            viewModel.addFriend(friend);
                        }
                    }
                });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(getActivity(), new FMFViewModelFactory(getActivity().getApplicationContext(),
                ((FMFApplication)getActivity().getApplication()).executor)).get(FMFViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewModel.getFriendsList().observe(getViewLifecycleOwner(), new Observer<List<Friend>>() {
            @Override
            public void onChanged(@Nullable List<Friend> friends) {
                updateFriendsListUI(friends);
            }
        });

        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddFriendDialogActivity.class);

                launcher.launch(intent);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void updateFriendsListUI(List<Friend> friends) {
        binding.allFriendsLayout.removeAllViews();

        View item;
        Button removeButton;
        TextView tv;
        for(Friend friend : friends) {
            item = getLayoutInflater().inflate(R.layout.all_friends_list_item_layout, null);
            removeButton = item.findViewById(R.id.removeFriendButton);
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewModel.deleteFriend(friend);
                }
            });
            tv = item.findViewById(R.id.nameTextView);
            tv.setText(friend.name);
            tv = item.findViewById(R.id.addressTextView);
            tv.setText(friend.BTMacAddr);
            binding.allFriendsLayout.addView(item);
        }
    }
}