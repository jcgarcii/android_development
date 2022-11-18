package edu.iastate.cpre388.findmyfriends.ui;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import edu.iastate.cpre388.findmyfriends.FMFViewModel;
import edu.iastate.cpre388.findmyfriends.R;
import edu.iastate.cpre388.findmyfriends.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private FMFViewModel viewModel;
    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            BluetoothManager manager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
            BluetoothAdapter adapter = manager.getAdapter();

            String text = "Bluetooth Settings" + "\n"
                    + "Name: " + adapter.getName() + "\n"
                    + "Enabled: " + adapter.isEnabled() + "\n"
                    + "To get the MAC address, view the About section in Settings\n"
                    + "(depending on Android version, may need to click on Status)";
            binding.myBluetoothInfoTextView.setText(text);
        } else {
            Toast.makeText(getContext(), R.string.no_bluetooth_text, Toast.LENGTH_LONG).show();
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}