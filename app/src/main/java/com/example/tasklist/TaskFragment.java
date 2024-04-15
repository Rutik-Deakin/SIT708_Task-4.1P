package com.example.tasklist;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.Calendar;

public class TaskFragment extends Fragment {

    String[] selectedDate = new String[1];
    EditText title;
    EditText description;
    EditText editTextDate;
    Button addTaskButton;
    Button buttonDeleteTask;
    long taskIdToUpdate = -1;
    TaskDatabaseHelper taskDatabaseHelper; // Declare without initialization

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_task, container, false);
        taskDatabaseHelper = new TaskDatabaseHelper(requireContext());
        title = rootView.findViewById(R.id.editTextTitle);
        description = rootView.findViewById(R.id.editTextDescription);
        editTextDate = rootView.findViewById(R.id.editTextDate);
        addTaskButton = rootView.findViewById(R.id.buttonAddTask);
        buttonDeleteTask = rootView.findViewById(R.id.buttonDeleteTask);

        Bundle args = getArguments();
        if (args != null) {
            String taskJson = args.getString("taskJson");
            if (taskJson != null) {
                Gson gson = new Gson();
                Task task = gson.fromJson(taskJson, Task.class);

                title.setText(task.getTitle());
                description.setText(task.getDescription());
                editTextDate.setText(task.getDate());
                selectedDate[0] = task.getDate();
                taskIdToUpdate = task.getTaskId();

                buttonDeleteTask.setVisibility(taskIdToUpdate == -1 ? View.GONE : View.VISIBLE);
            }
        }


        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDateEditTextClicked();
            }
        });

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskTitle = title.getText().toString();
                String taskDescription = description.getText().toString();
                String taskDate = selectedDate[0];

                // Perform validation
                if (taskTitle.isEmpty() || taskDescription.isEmpty() || taskDate.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter all details!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (taskIdToUpdate == -1) {
                    insertTask(taskTitle, taskDescription, taskDate);
                } else {
                    updateTask(taskIdToUpdate, taskTitle, taskDescription, taskDate);
                }
            }
        });

        buttonDeleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTask(taskIdToUpdate);
            }

        });

        buttonDeleteTask.setVisibility(taskIdToUpdate == -1 ? View.GONE : View.VISIBLE);
        if (taskIdToUpdate == -1) {
            addTaskButton.setText("Add Task");
        } else {
            addTaskButton.setText("Update Task");
        }
        return rootView;
    }

    private void onDateEditTextClicked() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectedDate[0] = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                editTextDate.setText(selectedDate[0]);
            }
        }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    public void insertTask(String title, String description, String date) {
        Long taskId = System.currentTimeMillis();
        Boolean result = taskDatabaseHelper.insertTask(taskId, title, description, date);
        if (result) {
            TaskListFragment taskListFragment = new TaskListFragment();
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, taskListFragment).commit();
        } else {
            Log.d("TASK!!!!!!!", "Task NOT Inserted");
        }
    }

    public void updateTask(long taskId, String title, String description, String date) {
        Boolean result = taskDatabaseHelper.updateTask(taskId, title, description, date);
        if (result) {
            TaskListFragment taskListFragment = new TaskListFragment();
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, taskListFragment).commit();
            requireActivity().getSupportFragmentManager().popBackStack();
        } else {
            Log.d("TASK!!!!!!!", "Task NOT UPDATED!!!!");
        }
    }

    public void deleteTask(long taskId) {
        Boolean result = taskDatabaseHelper.deleteTask(taskId);
        if (result) {
            Log.d("TASK!!!!!!!", "Task UPDATED!!!!");
            TaskListFragment taskListFragment = new TaskListFragment();
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, taskListFragment).commit();
        } else {
            Log.d("TASK!!!!!!!", "Task NOT UPDATED!!!!");
        }
    }
}
