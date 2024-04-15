package com.example.tasklist;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.gson.Gson;

import java.util.List;

public class TaskListFragment extends Fragment implements TaskAdapter.OnTaskClickListener {

    private RecyclerView recyclerView;
    private TextView addSomeTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TaskDatabaseHelper taskDatabaseHelper = new TaskDatabaseHelper(getContext());
        List<Task> taskList = taskDatabaseHelper.getAllTasks();

        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        addSomeTask = view.findViewById(R.id.addSomeTaskView);
        recyclerView = view.findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Set visibility based on task list size
        if (taskList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            addSomeTask.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            addSomeTask.setVisibility(View.GONE);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            TaskAdapter taskAdapter = new TaskAdapter(taskList, this);
            taskAdapter.setOnTaskClickListener(this); // Set the listener
            recyclerView.setAdapter(taskAdapter);
        }
        return view;
    }

    @Override
    public void onTaskClicked(Task task) {
        TaskFragment taskFragment = new TaskFragment();
        Gson gson = new Gson();
        String taskJson = gson.toJson(task);
        Bundle args = new Bundle();
        args.putString("taskJson", taskJson);
        taskFragment.setArguments(args);
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, taskFragment).addToBackStack(null).commit();
    }
}
