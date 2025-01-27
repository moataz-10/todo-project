package org.example.todoback.services;

import org.example.todoback.repos.TaskRepo;
import org.example.todoback.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepo taskRepo;
    @Autowired
    private TaskPublisher taskPublisher;

    public Task markAsDone(Integer id, Boolean done) {
        Optional<Task> optionalTodo = taskRepo.findById(id);
        if (optionalTodo.isPresent()) {
            Task todo = optionalTodo.get();
            todo.setDone(done); // Update the "done" field
            taskPublisher.publishTodoEvent("Task updated: " + todo.getText() + " - Done: " + done);
            return taskRepo.save(todo); // Save the updated entity
        }
        return null;
    }

    public List<Task> getAllTasks() {
        return taskRepo.findAll();
    }

    public void addTask(Task todo) {
        taskRepo.save(todo);
        taskPublisher.publishTodoEvent("Task created: " + todo.getText());
    }

    public void testDb() {
        taskRepo.flush();
    }
}
