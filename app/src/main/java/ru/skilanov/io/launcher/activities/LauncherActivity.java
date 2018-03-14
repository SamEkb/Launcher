package ru.skilanov.io.launcher.activities;

import android.support.v4.app.Fragment;

import ru.skilanov.io.launcher.fragments.LauncherFragment;

/**
 * Класс активности.
 */
public class LauncherActivity extends SingleFragmentActivity {
    /**
     * Метод создает новый LauncherFragment().
     *
     * @return созданный Fragment
     */
    @Override
    protected Fragment createFragment() {
        return new LauncherFragment();
    }
}
