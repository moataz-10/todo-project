package org.example.todoback.controllers;

import org.example.todoback.services.TaskService;
import org.example.todoback.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class TaskController {
    @Autowired
    private TaskService taskService;


    @GetMapping("/todos")
    public List<Task> getTodos() {
        List<Task> tasks =taskService.getAllTasks();
        System.out.println(tasks);
        return tasks;
    }

    @PostMapping("/todos")
    public void addTodo(@RequestBody Task todo) {
        if(todo.getText().length()<=140){
            taskService.addTask(todo);
            System.out.println("Added todo: " + todo);
        }else{
            System.out.println("Unallowed String, it must be less than 140");
        }
    }
    @PutMapping("/todos/{id}")
    public ResponseEntity<Task> markAsDone(@PathVariable Integer id, @RequestBody Task updatedTodo) {
        Task todo = taskService.markAsDone(id, updatedTodo.isDone());
        if (todo != null) {
            return ResponseEntity.ok(todo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/testDb")
    public void testDb(){
        taskService.testDb();
    }
}
