package com.org.enotesapiservice.service.impl;

import com.org.enotesapiservice.dto.TodoDto;
import com.org.enotesapiservice.entity.Todo;
import com.org.enotesapiservice.enums.ToDoStatus;
import com.org.enotesapiservice.exception.ResourceNotFoundException;
import com.org.enotesapiservice.repository.TodoRepository;
import com.org.enotesapiservice.service.TodoService;
import com.org.enotesapiservice.util.Validation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final ModelMapper modelMapper;
    private final Validation validation;

    @Override
    public Boolean saveToDo(TodoDto todoDto) throws Exception {
        //validation toDo status
        validation.todoValidation(todoDto);
        Todo toDo = modelMapper.map(todoDto, Todo.class);
        toDo.setStatusId(todoDto.getStatus().getId());
        Todo saveToDo = todoRepository.save(toDo);
        if (!ObjectUtils.isEmpty(saveToDo)) {
            return true;
        }
        return false;
    }

    @Override
    public TodoDto getTodoById(Integer id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id is invalid:Not Found"));
        TodoDto toDoDto = modelMapper.map(todo, TodoDto.class);
        setStatus(toDoDto, todo);
        return toDoDto;
    }

    private void setStatus(TodoDto toDoDto, Todo todo) {
        for (ToDoStatus st : ToDoStatus.values()) {
            if (st.getId().equals(todo.getStatusId())) {
                TodoDto.StatusDto statusDto = TodoDto.StatusDto.builder()
                        .id(st.getId())
                        .name(st.getName())
                        .build();
                toDoDto.setStatus(statusDto);
            }
        }
    }

    @Override
    public List<TodoDto> getToDoByUser() {
        Integer userId = 1;
        List<Todo> todoList = todoRepository.findByCreatedBy(userId);
        return todoList.stream()
                .map(todo -> modelMapper.map(todo, TodoDto.class)).
                toList();
    }
}
