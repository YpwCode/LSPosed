package org.mliboot.mlsp.manager.ui.fragment;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import org.mliboot.mlspd.ILSPManagerService;
import org.mliboot.mlsp.manager.ConfigManager;
import org.mliboot.mlsp.manager.R;
import org.mliboot.mlsp.manager.databinding.FragmentHomeBinding;
import org.mliboot.mlsp.manager.util.Telemetry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import rikka.core.util.ClipboardUtils;
import rikka.material.app.LocaleDelegate;

public class HomeFragment extends BaseFragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding.toolbar.setTitle(R.string.app_name);
        updateStates(requireActivity(), ConfigManager.isBinderAlive());
        return binding.getRoot();
    }

    private void updateStates(Activity activity, boolean binderAlive) {
        if (binderAlive) {
            boolean dex2oatAbnormal = ConfigManager.getDex2OatWrapperCompatibility() != ILSPManagerService.DEX2OAT_OK && !ConfigManager.dex2oatFlagsLoaded();
            var sepolicyAbnormal = !ConfigManager.isSepolicyLoaded();
            var systemServerAbnormal = !ConfigManager.systemServerRequested();
            if (sepolicyAbnormal || systemServerAbnormal || dex2oatAbnormal) {
                binding.statusTitle.setText(R.string.partial_activated);
                binding.statusIcon.setImageResource(R.drawable.ic_round_warning_24);
            } else {
                binding.statusTitle.setText(R.string.activated);
                binding.statusIcon.setImageResource(R.drawable.ic_round_check_circle_24);
            }
            binding.statusSummary.setText(String.format(LocaleDelegate.getDefaultLocale(), "%s (%d) - %s",
                    ConfigManager.getXposedVersionName(), ConfigManager.getXposedVersionCode(), ConfigManager.getApi()));
            binding.developerWarningCard.setVisibility(isDeveloper() ? View.VISIBLE : View.GONE);
        } else {
            binding.statusTitle.setText(R.string.not_installed);
            binding.statusSummary.setText(R.string.not_install_summary);
        }

        if (ConfigManager.isBinderAlive()) {
            binding.apiVersion.setText(String.valueOf(ConfigManager.getXposedApiVersion()));
            binding.api.setText(ConfigManager.isDexObfuscateEnabled() ? R.string.enabled : R.string.not_enabled);
            binding.frameworkVersion.setText(String.format(LocaleDelegate.getDefaultLocale(), "%1$s (%2$d)", ConfigManager.getXposedVersionName(), ConfigManager.getXposedVersionCode()));
            binding.managerPackageName.setText(activity.getPackageName());
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                binding.dex2oatWrapper.setText(String.format(LocaleDelegate.getDefaultLocale(), "%s (%s)", getString(R.string.unsupported), getString(R.string.android_version_unsatisfied)));
            } else switch (ConfigManager.getDex2OatWrapperCompatibility()) {
                case ILSPManagerService.DEX2OAT_OK ->
                        binding.dex2oatWrapper.setText(R.string.supported);
                case ILSPManagerService.DEX2OAT_CRASHED ->
                        binding.dex2oatWrapper.setText(String.format(LocaleDelegate.getDefaultLocale(), "%s (%s)", getString(R.string.unsupported), getString(R.string.crashed)));
                case ILSPManagerService.DEX2OAT_MOUNT_FAILED ->
                        binding.dex2oatWrapper.setText(String.format(LocaleDelegate.getDefaultLocale(), "%s (%s)", getString(R.string.unsupported), getString(R.string.mount_failed)));
                case ILSPManagerService.DEX2OAT_SELINUX_PERMISSIVE ->
                        binding.dex2oatWrapper.setText(String.format(LocaleDelegate.getDefaultLocale(), "%s (%s)", getString(R.string.unsupported), getString(R.string.selinux_permissive)));
                case ILSPManagerService.DEX2OAT_SEPOLICY_INCORRECT ->
                        binding.dex2oatWrapper.setText(String.format(LocaleDelegate.getDefaultLocale(), "%s (%s)", getString(R.string.unsupported), getString(R.string.sepolicy_incorrect)));
            }
        } else {
            binding.apiVersion.setText(R.string.not_installed);
            binding.api.setText(R.string.not_installed);
            binding.frameworkVersion.setText(R.string.not_installed);
            binding.managerPackageName.setText(activity.getPackageName());
        }

        if (Build.VERSION.PREVIEW_SDK_INT != 0) {
            binding.systemVersion.setText(String.format(LocaleDelegate.getDefaultLocale(), "%1$s Preview (API %2$d)", Build.VERSION.CODENAME, Build.VERSION.SDK_INT));
        } else {
            binding.systemVersion.setText(String.format(LocaleDelegate.getDefaultLocale(), "%1$s (API %2$d)", Build.VERSION.RELEASE, Build.VERSION.SDK_INT));
        }

        binding.device.setText(getDevice());
        binding.systemAbi.setText(Build.SUPPORTED_ABIS[0]);
        String info = activity.getString(R.string.info_api_version) +
                "\n" +
                binding.apiVersion.getText() +
                "\n\n" +
                activity.getString(R.string.settings_xposed_api_call_protection) +
                "\n" +
                binding.api.getText() +
                "\n\n" +
                activity.getString(R.string.info_dex2oat_wrapper) +
                "\n" +
                binding.dex2oatWrapper.getText() +
                "\n\n" +
                activity.getString(R.string.info_framework_version) +
                "\n" +
                binding.frameworkVersion.getText() +
                "\n\n" +
                activity.getString(R.string.info_manager_package_name) +
                "\n" +
                binding.managerPackageName.getText() +
                "\n\n" +
                activity.getString(R.string.info_system_version) +
                "\n" +
                binding.systemVersion.getText() +
                "\n\n" +
                activity.getString(R.string.info_device) +
                "\n" +
                binding.device.getText() +
                "\n\n" +
                activity.getString(R.string.info_system_abi) +
                "\n" +
                binding.systemAbi.getText();
        var map = new HashMap<String, String>();
        map.put("apiVersion", binding.apiVersion.getText().toString());
        map.put("api", binding.api.getText().toString());
        map.put("frameworkVersion", binding.frameworkVersion.getText().toString());
        map.put("systemAbi", Arrays.toString(Build.SUPPORTED_ABIS));
        Telemetry.trackEvent("HomeFragment", map);
        binding.copyInfo.setOnClickListener(v -> {
            ClipboardUtils.put(activity, info);
            showHint(R.string.info_copied, false);
        });
    }

    private String getDevice() {
        String manufacturer = Character.toUpperCase(Build.MANUFACTURER.charAt(0)) + Build.MANUFACTURER.substring(1);
        if (!Build.BRAND.equals(Build.MANUFACTURER)) {
            manufacturer += " " + Character.toUpperCase(Build.BRAND.charAt(0)) + Build.BRAND.substring(1);
        }
        manufacturer += " " + Build.MODEL + " ";
        return manufacturer;
    }

    private boolean isDeveloper() {
        var developer = new AtomicBoolean(false);
        var pids = Paths.get("/data/local/tmp/.studio/ipids");
        try (var dir = Files.list(pids)) {
            dir.findFirst().ifPresent(name -> {
                var pid = Integer.parseInt(name.getFileName().toString());
                try {
                    Os.kill(pid, 0);
                    developer.set(true);
                } catch (ErrnoException e) {
                    if (e.errno == OsConstants.ESRCH) {
                        try {
                            Files.delete(name);
                        } catch (IOException ignored) {
                        }
                    } else {
                        developer.set(true);
                    }
                }
            });
        } catch (IOException e) {
            return false;
        }
        return developer.get();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
