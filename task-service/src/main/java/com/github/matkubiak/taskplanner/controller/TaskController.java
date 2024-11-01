package com.github.matkubiak.taskplanner.controller;

import com.github.matkubiak.taskplanner.model.Task;
import com.github.matkubiak.taskplanner.model.TaskDTO;
import com.github.matkubiak.taskplanner.model.TaskNotFoundException;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.github.matkubiak.taskplanner.service.TaskService;

import java.util.List;

@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping(path="/health")
    public ResponseEntity<Object> healthCheck() {
        return new ResponseEntity<>("Service is up and running!", HttpStatus.OK);
    }

    @GetMapping(path="/")
    public ResponseEntity<List<Task>> getTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PostMapping(path="/")
    public ResponseEntity<Object> createTask(@RequestBody TaskDTO taskDto) {
        taskService.saveTask(taskDto);
        return new ResponseEntity<>("Task created successfully", HttpStatus.CREATED);
    }

    @DeleteMapping(path="/{taskId}")
    public ResponseEntity<Object> deleteTask(@PathVariable(name="taskId") Long taskId) {
        try {
            taskService.deleteTask(taskId);
        } catch (TaskNotFoundException e) {
            return new ResponseEntity<>(String.format("Task of id %d does not exist", taskId), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Task deleted successfully", HttpStatus.ACCEPTED);
    }
}
