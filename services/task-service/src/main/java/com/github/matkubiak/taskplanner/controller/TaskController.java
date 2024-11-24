/*
 * Task Planner
 * Copyright (C) 2024 Mateusz Kubiak
 *
 * Licensed under the GNU General Public License v3.
 * See LICENSE or visit <https://www.gnu.org/licenses/>.
 */

package com.github.matkubiak.taskplanner.controller;

import com.github.matkubiak.taskplanner.model.Task;
import com.github.matkubiak.taskplanner.model.TaskCreateDTO;
import com.github.matkubiak.taskplanner.model.TaskNotFoundException;
import com.github.matkubiak.taskplanner.model.TaskUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.github.matkubiak.taskplanner.service.TaskService;

import java.util.List;

@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/health")
    public ResponseEntity<Object> healthCheck() {
        return new ResponseEntity<>("Service is up and running!", HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<Task>> getTasks(@RequestHeader("X-Subject") Long userId) {
        List<Task> tasks = taskService.getAllUserTasks(userId);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Object> createTask(
            @RequestHeader("X-Subject") Long userId,
            @RequestBody TaskCreateDTO taskDto) {

        taskService.saveTask(userId, taskDto);
        return new ResponseEntity<>("Task created successfully", HttpStatus.CREATED);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Object> deleteTask(
            @RequestHeader("X-Subject") Long userId,
            @PathVariable(name="taskId") Long taskId) {

        try {
            taskService.deleteTask(userId, taskId);
        } catch (TaskNotFoundException e) {
            return new ResponseEntity<>(String.format("Task of id %d does not exist for current user", taskId), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Task deleted successfully", HttpStatus.ACCEPTED);
    }

    @PutMapping("/")
    public ResponseEntity<Object> updateTask(
            @RequestHeader("X-Subject") Long userId,
            @RequestBody TaskUpdateDTO taskDto) {

        try {
            taskService.updateTask(userId, taskDto);
        } catch (TaskNotFoundException e) {
            return new ResponseEntity<>(String.format("Task of id %d does not exist for current user", taskDto.getId()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Task modified successfully", HttpStatus.OK);
    }
}
