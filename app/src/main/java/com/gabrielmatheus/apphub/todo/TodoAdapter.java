package com.gabrielmatheus.apphub.todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.gabrielmatheus.apphub.R;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TaskViewHolder> {

    private List<Task> tasks;

    public TodoAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkBox;
        public TextView textTitle;
        public TextView textDate;
        public TextView textPriority;

        public TaskViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkboxTask);
            textTitle = itemView.findViewById(R.id.textTaskTitle);
            textDate = itemView.findViewById(R.id.textTaskDate);
            textPriority = itemView.findViewById(R.id.textTaskPriority);
        }
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.textTitle.setText(task.title);
        holder.textDate.setText("Vence em: " + task.dueDate);
        holder.textPriority.setText("Prioridade: " + task.priority);
        holder.checkBox.setChecked(task.isDone);

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.isDone = isChecked;
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void addTask(Task task) {
        tasks.add(task);
        notifyItemInserted(tasks.size() - 1);
    }
}
