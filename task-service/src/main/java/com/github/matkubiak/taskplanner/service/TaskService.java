package com.github.matkubiak.taskplanner.service;

import com.github.matkubiak.taskplanner.model.Task;
import com.github.matkubiak.taskplanner.model.TaskDTO;
import com.github.matkubiak.taskplanner.model.TaskNotFoundException;
import com.github.matkubiak.taskplanner.model.TaskUpdateDTO;
import com.github.matkubiak.taskplanner.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public void saveTask(TaskDTO taskDto) {
        Task task = new Task();
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setState(false);

        taskRepository.save(task);
    }

    public void deleteTask(Long taskId) throws TaskNotFoundException {
        boolean exists = taskRepository.existsById(taskId);
        if (!exists) {
            throw new TaskNotFoundException();
        }
        taskRepository.deleteById(taskId);
    }

    public void updateTask(TaskUpdateDTO taskDto) throws TaskNotFoundException {
        boolean exists = taskRepository.existsById(taskDto.getId());
        if (!exists) {
            throw new TaskNotFoundException();
        }

        Task task = new Task();
        task.setId(taskDto.getId());
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setState(taskDto.getState());

        taskRepository.save(task);
    }
}
