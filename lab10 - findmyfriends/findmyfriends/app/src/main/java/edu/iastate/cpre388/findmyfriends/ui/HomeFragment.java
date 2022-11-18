package edu.iastate.cpre388.findmyfriends.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import edu.iastate.cpre388.findmyfriends.FMFApplication;
import edu.iastate.cpre388.findmyfriends.FMFViewModel;
import edu.iastate.cpre388.findmyfriends.FMFViewModelFactory;
import edu.iastate.cpre388.findmyfriends.databinding.FragmentHomeBinding;

/* TODO: implement searching for and displaying nearby friends */

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private FMFViewModel viewModel;
    private FragmentHomeBinding binding;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(getActivity(), new FMFViewModelFactory(getActivity().getApplicationContext(),
                ((FMFApplication)getActivity().getApplication()).executor)).get(FMFViewModel.class);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}