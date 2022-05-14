package com.example.h4i;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.h4i.adapter.OnTodoClickListener;
import com.example.h4i.adapter.RecyclerViewAdapter;
import com.example.h4i.model.Priority;
import com.example.h4i.model.SharedViewModel;
import com.example.h4i.model.Task;
import com.example.h4i.model.TaskViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.List;

public class TodoActivity extends AppCompatActivity implements OnTodoClickListener {
    public static final String TAG="ITEM";
    private TaskViewModel taskViewModel;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private int counter;
    BottomSheetFragment bottomSheetFragment;
    private SharedViewModel sharedViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        counter=0;

        bottomSheetFragment=new BottomSheetFragment();
        ConstraintLayout constraintLayout=findViewById(R.id.bottomSheet);
        BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior=BottomSheetBehavior.from(constraintLayout);
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskViewModel=new ViewModelProvider.AndroidViewModelFactory(
                TodoActivity.this.getApplication())
                .create(TaskViewModel.class);

        sharedViewModel=new ViewModelProvider(this)
                .get(SharedViewModel.class);

        taskViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                recyclerViewAdapter= new RecyclerViewAdapter(tasks,TodoActivity.this);
                recyclerView.setAdapter(recyclerViewAdapter);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*Task task=new Task("Task "+counter++, Priority.MEDIUM, Calendar.getInstance().getTime(),
                       Calendar.getInstance().getTime(), false);

               TaskViewModel.insert(task);*/
                showBottomSheetDialog();

            }
        });
    }

    private void showBottomSheetDialog() {
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    @Override
    public void onTodoClick(Task task) {
        sharedViewModel.selectItem(task);
        sharedViewModel.setIsEdit(true);
        //Log.d("Click","onTodoClick: "+task.getTask());
        showBottomSheetDialog();
    }

    @Override
    public void onTodoRadioButtonClick(Task task) {
        Log.d("Click","onTodoRadioButtonClick: "+task.getTask());
        TaskViewModel.delete(task);
        recyclerViewAdapter.notifyDataSetChanged();
    }
}