package com.org.enotesapiservice.controller;

import com.org.enotesapiservice.dto.TodoDto;
import com.org.enotesapiservice.service.TodoService;
import com.org.enotesapiservice.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/todo")
public class ToDoController {

    private final TodoService todoService;

    @PostMapping("/")
    public ResponseEntity<?> saveToDo(@RequestBody TodoDto todoDto) throws Exception {
        Boolean saveToDo = todoService.saveToDo(todoDto);
        if (saveToDo) {
            return CommonUtil.createBuildResponseMessage("saved success", HttpStatus.CREATED);
           // return CommonUtil.createErrorResponseMessage("not-saved facing some issue", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return CommonUtil.createErrorResponseMessage("not-saved facing some issue", HttpStatus.INTERNAL_SERVER_ERROR);
            //return CommonUtil.createBuildResponseMessage("saved success", HttpStatus.CREATED);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getToDoById(@PathVariable Integer id) {
        TodoDto todoById = todoService.getTodoById(id);
        return CommonUtil.createBuildResponse(todoById, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllToDoByUser() {
        List<TodoDto> toDoByUser = todoService.getToDoByUser();
        if (CollectionUtils.isEmpty(toDoByUser)) {
            return CommonUtil.createBuildResponse("NO_CONTENT FOUND", HttpStatus.NO_CONTENT);
        }
        return CommonUtil.createBuildResponse(toDoByUser, HttpStatus.OK);
    }
}
