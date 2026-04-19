package com.todo.todo_app;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoRepository repository;

    public TodoController(TodoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<TodoItem> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public TodoItem create(@RequestBody Map<String, String> body) {
        String title = body.get("title");
        String dueDate = body.get("dueDate");
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title is required");
        }
        return repository.save(new TodoItem(title, dueDate));
    }

    @PutMapping("/{id}")
    public TodoItem update(@PathVariable Long id, @RequestBody Map<String, String> body) {
        TodoItem item = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found"));
        if (body.containsKey("title")) item.setTitle(body.get("title"));
        if (body.containsKey("dueDate")) item.setDueDate(body.get("dueDate"));
        if (body.containsKey("completed")) item.setCompleted(Boolean.parseBoolean(body.get("completed")));
        return repository.save(item);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}