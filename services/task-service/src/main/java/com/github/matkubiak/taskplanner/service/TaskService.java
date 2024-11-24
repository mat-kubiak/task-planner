/*
 * Task Planner
 * Copyright (C) 2024 Mateusz Kubiak
 *
 * Licensed under the GNU General Public License v3.
 * See LICENSE or visit <https://www.gnu.org/licenses/>.
 */

package com.github.matkubiak.taskplanner.service;

import com.github.matkubiak.taskplanner.model.*;
import com.github.matkubiak.taskplanner.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllUserTasks(Long userId) {
        return taskRepository.findAllByUserId(userId);
    }

    public void saveTask(Long userId, TaskCreateDTO taskDto) {
        Task task = new Task();
        task.setUserId(userId);
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setState(false);

        taskRepository.save(task);
    }

    public void deleteTask(Long userId, Long taskId) throws TaskNotFoundException {
        if (!taskRepository.existsByIdAndUserId(taskId, userId)) {
            throw new TaskNotFoundException();
        }
        taskRepository.deleteById(taskId);
    }

    public void updateTask(Long userId, TaskUpdateDTO taskDto) throws TaskNotFoundException {
        if (!taskRepository.existsByIdAndUserId(taskDto.getId(), userId)) {
            throw new TaskNotFoundException();
        }

        Task task = new Task();
        task.setId(taskDto.getId());
        task.setUserId(userId);
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setState(taskDto.getState());

        taskRepository.save(task);
    }
}
