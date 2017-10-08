package com.example.todo;

import com.example.todo.entity.Todo;
import com.example.todo.repository.TodoRepository;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TodoList extends VerticalLayout implements TodoChangeListener{

    @Autowired
    TodoRepository repository;
    private List<Todo> todoes;

    @PostConstruct
    void init(){
        setSpacing(true);
        update();
    }

    public void setTodoes(List<Todo> todoes) {
        this.todoes = todoes;
        removeAllComponents();
        todoes.forEach(todo->{
            addComponent(new TodoLayout(todo, this));
        });
    }

    public void add(Todo todo) {
        repository.save(todo);
        update();
    }

    private void update() {
        setTodoes(repository.findAll());

    }

    @Override
    public void todoChanged(Todo todo) {
        save(todo);
    }

    private void save(Todo todo) {
        repository.save(todo);
        update();
    }

    public void deleteCompleted() {
        repository.deleteInBatch(
        todoes.stream().filter(Todo::isDone).collect(Collectors.toList()));
        update();
    }
}
