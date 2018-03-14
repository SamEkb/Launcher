package ru.skilanov.io.launcher.fragments;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.skilanov.io.launcher.R;

/**
 * Фрагмент хостом которой является LauncherActivity.
 */

public class LauncherFragment extends Fragment {
    private RecyclerView mRecyclerView;

    /**
     * Метод создающий и заполняющий view.
     *
     * @param inflater           LayoutInflater
     * @param container          ViewGroup
     * @param savedInstanceState Bundle
     * @return view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_launcher, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view_id);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setupAdapter();

        return view;
    }

    /**
     * Создаем иетнет и получаем список интентов лаунчеров и сортируем их в алфавитном порядке,
     * затем задаем адаптер для recycler view.
     */
    private void setupAdapter() {
        Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager manager = getActivity().getPackageManager();
        List<ResolveInfo> activities = manager.queryIntentActivities(startupIntent, 0);
        Collections.sort(activities, new Comparator<ResolveInfo>() {
            /**
             * Метод сортировки объектов ResolveInfo.
             * @param o1 ResolveInfo
             * @param o2 ResolveInfo
             * @return int
             */
            @Override
            public int compare(ResolveInfo o1, ResolveInfo o2) {
                PackageManager packageManager = getActivity().getPackageManager();
                return String.CASE_INSENSITIVE_ORDER.compare(
                        o1.loadLabel(packageManager).toString(),
                        o2.loadLabel(packageManager).toString()
                );
            }
        });
        mRecyclerView.setAdapter(new ActivityAdapter(activities));
    }

    /**
     * Класс холдер хранит ссылки на виджеты.
     */
    private class ActivityHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ResolveInfo mResolvedInfo;
        private TextView mApplicationNameTextView;

        /**
         * Конструктор.
         *
         * @param itemView View
         */
        public ActivityHolder(View itemView) {
            super(itemView);
            mApplicationNameTextView = (TextView) itemView;
            mApplicationNameTextView.setOnClickListener(this);
        }

        /**
         * Метод связывания.
         *
         * @param resolveInfo ResolveInfo
         */
        public void bind(ResolveInfo resolveInfo) {
            mResolvedInfo = resolveInfo;
            PackageManager packageManager = getActivity().getPackageManager();
            String applicationName = mResolvedInfo.loadLabel(packageManager).toString();
            mApplicationNameTextView.setText(applicationName);
        }

        /**
         * Метод клика по позиции item в recycler view. Мы задаем в явном интенте имя пакета
         * и имя активности для ее запуска.
         *
         * @param v View
         */
        @Override
        public void onClick(View v) {
            ActivityInfo activityInfo = mResolvedInfo.activityInfo;
            Intent intent = new Intent(Intent.ACTION_MAIN).setClassName(activityInfo.applicationInfo
                    .packageName, activityInfo.name);
            startActivity(intent);
        }
    }

    /**
     * Класс адаптер, отвечает за передачу информации в recycler view.
     */
    private class ActivityAdapter extends RecyclerView.Adapter<ActivityHolder> {
        private List<ResolveInfo> mActivities;

        /**
         * Конструктор.
         *
         * @param activities List
         */
        public ActivityAdapter(List<ResolveInfo> activities) {
            this.mActivities = activities;
        }

        /**
         * Создает и заполняет view.
         *
         * @param parent   ViewGroup
         * @param viewType ште
         * @return заполненный ActivityHolder
         */
        @Override
        public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ActivityHolder(view);
        }

        /**
         * Связывает позиции recycler view с виджетами.
         *
         * @param holder   ActivityHolder
         * @param position int
         */
        @Override
        public void onBindViewHolder(ActivityHolder holder, int position) {
            ResolveInfo resolveInfo = mActivities.get(position);
            holder.bind(resolveInfo);
        }

        /**
         * Возвращает размер List в котором хранятся resolveInfo.
         *
         * @return int
         */
        @Override
        public int getItemCount() {
            return mActivities.size();
        }
    }
}
