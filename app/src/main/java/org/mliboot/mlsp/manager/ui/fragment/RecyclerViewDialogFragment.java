

package org.mliboot.mlsp.manager.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.mliboot.mlspd.models.UserInfo;
import org.mliboot.mlsp.manager.R;
import org.mliboot.mlsp.manager.databinding.DialogTitleBinding;
import org.mliboot.mlsp.manager.databinding.SwiperefreshRecyclerviewBinding;
import org.mliboot.mlsp.manager.ui.dialog.BlurBehindDialogBuilder;
import org.mliboot.mlsp.manager.util.ModuleUtil;

public class RecyclerViewDialogFragment extends AppCompatDialogFragment {
    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        var parent = getParentFragment();
        var arguments = getArguments();
        if (!(parent instanceof ModulesFragment) || arguments == null) {
            throw new IllegalStateException();
        }
        var modulesFragment = (ModulesFragment) parent;
        var user = (UserInfo) arguments.getParcelable("userInfo");

        var pickAdaptor = modulesFragment.createPickModuleAdapter(user);
        var binding = SwiperefreshRecyclerviewBinding.inflate(LayoutInflater.from(requireActivity()), null, false);

        binding.recyclerView.setAdapter(pickAdaptor);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        pickAdaptor.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                binding.swipeRefreshLayout.setRefreshing(!pickAdaptor.isLoaded());
            }
        });
        binding.swipeRefreshLayout.setProgressViewEndTarget(true, binding.swipeRefreshLayout.getProgressViewEndOffset());
        binding.swipeRefreshLayout.setOnRefreshListener(pickAdaptor::fullRefresh);
        pickAdaptor.refresh();
        var title = DialogTitleBinding.inflate(getLayoutInflater()).getRoot();
        title.setText(getString(R.string.install_to_user, user.name));
        var dialog = new BlurBehindDialogBuilder(requireActivity(), R.style.ThemeOverlay_MaterialAlertDialog_FullWidthButtons)
                .setCustomTitle(title)
                .setView(binding.getRoot())
                .setNegativeButton(android.R.string.cancel, null)
                .create();
        title.setOnClickListener(s -> binding.recyclerView.smoothScrollToPosition(0));
        pickAdaptor.setOnPickListener(picked -> {
            var module = (ModuleUtil.InstalledModule) picked.getTag();
            modulesFragment.installModuleToUser(module, user);
            dialog.dismiss();
        });
        onViewCreated(binding.getRoot(), savedInstanceState);
        return dialog;
    }

    // prevent from overriding
    public final void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
