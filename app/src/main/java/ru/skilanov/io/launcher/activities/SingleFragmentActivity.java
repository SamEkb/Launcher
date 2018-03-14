package ru.skilanov.io.launcher.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import ru.skilanov.io.launcher.R;

/**
 * Абстрактный класс активности.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {
    /**
     * Метод создает фрагмент.
     *
     * @return Fragment
     */
    protected abstract Fragment createFragment();

    /**
     * Метод жизненного цикла в котром fragmentManager проверяет есть ли в списке фрагмент, если
     * его нет, то фрагмент создается вызовом метода createFragment() и добавляется в писок
     * фрагментов через транзакцию.
     *
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
}
