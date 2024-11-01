package com.github.matkubiak.taskplanner.controller;

import com.github.matkubiak.taskplanner.model.Task;
import com.github.matkubiak.taskplanner.model.TaskDTO;
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

    @GetMapping(path="/")
    public ResponseEntity<List<Task>> getTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Object> createTask(@RequestBody TaskDTO taskDto) {
        taskService.saveTask(taskDto);
        return new ResponseEntity<>("Task created successfully", HttpStatus.OK);
    }
}
