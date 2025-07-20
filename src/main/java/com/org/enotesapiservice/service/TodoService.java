package com.org.enotesapiservice.service;

import com.org.enotesapiservice.dto.TodoDto;

import java.util.List;

public interface TodoService {

    Boolean saveToDo(TodoDto todoDto) throws Exception;

    TodoDto getTodoById(Integer id);

    List<TodoDto> getToDoByUser();

}
