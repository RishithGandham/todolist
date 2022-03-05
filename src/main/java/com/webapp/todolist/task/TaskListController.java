package com.webapp.todolist.task;

import com.webapp.todolist.appuser.AppUserDetails;
import com.webapp.todolist.exceptions.ListNotFoundException;
import com.webapp.todolist.messageresponse.MessageResponse;
import lombok.AllArgsConstructor;
import net.bytebuddy.build.Plugin;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.Date;
import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v2/listresource")
public class TaskListController {

    private final TaskListService taskListService;
    private final TaskListRepository taskListRepository;
    private final SimpleDateFormat simpleDateFormat;
    private final TaskRepository taskRepository;

    @GetMapping("/getlist/{id}")
    public ResponseEntity<?> getListByName(@PathVariable("id") Long id, Model model, Authentication auth) throws NumberFormatException, ListNotFoundException {

        if (!taskListRepository.existsById(id)) {
            MessageResponse messageResponse = new MessageResponse("The list that was requested was not found");
            return new ResponseEntity<>(messageResponse, HttpStatus.NOT_FOUND);
        }

        MessageResponse messageResponse = new MessageResponse("The list that was requested was not found");
        return new ResponseEntity<>(messageResponse, HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/getlists")
    public ResponseEntity<AllListResponse> getLists(Authentication auth) {
        AppUserDetails appUserDetails = (AppUserDetails) auth.getPrincipal();
        System.out.println(appUserDetails);
        List<TaskList> lists = taskListService.findByAppUser(appUserDetails);
        return new ResponseEntity<>(new AllListResponse(lists), HttpStatus.OK);
    }

    @PostMapping(value = "/createlist")
    public ResponseEntity<AllListResponse> createList(@RequestParam("name") String name,@RequestParam("desc") String desc, @RequestParam("date")  String date, Authentication auth) throws ParseException {
        Date dueDate = simpleDateFormat.parse(date);
        AppUserDetails appuser = (AppUserDetails) auth.getPrincipal();
        TaskList list = taskListService.addList(name, appuser, dueDate, desc);
        return new ResponseEntity<>(new AllListResponse(appuser.getListOfTaskLists()), HttpStatus.OK);

    }

    @PostMapping(value = "/deletelist")
    public ResponseEntity<MessageResponse> deleteList(@RequestParam("id") Long id, Authentication auth) throws NumberFormatException, ListNotFoundException {

            if (!taskListRepository.existsById(id)) {
                MessageResponse messageResponse = new MessageResponse("List wasn't found, maybe refresh? ");
                new ResponseEntity<MessageResponse>(messageResponse, HttpStatus.NOT_FOUND);

            }

            AppUserDetails appUserDetails = (AppUserDetails) auth.getPrincipal();
            taskListService.deleteList(id);
            MessageResponse messageResponse = new MessageResponse("List Deleted");
            return new ResponseEntity<>(messageResponse, HttpStatus.OK);


    }


    @PostMapping("/editlist")
    public ResponseEntity<AllListResponse> editlist(@RequestParam("id") Long id, @RequestParam("name") String name,@RequestParam("desc") String desc, @RequestParam("date")  String date, Authentication auth ) throws ParseException, ListNotFoundException {
        Date dueDate = simpleDateFormat.parse(date);
        taskListService.editList(name, (AppUserDetails) auth.getPrincipal(), dueDate, desc, id);
        return new ResponseEntity<>(new AllListResponse(taskListService.findByAppUser((AppUserDetails) auth.getPrincipal())), HttpStatus.OK);
    }

    @PostMapping("/deleteTodo")
    public ResponseEntity<TaskList> deleteTask(@RequestParam("id") Long id, Authentication authentication) {
        if (!taskRepository.existsById(id)) {
            MessageResponse messageResponse = new MessageResponse("Task wasn't found, maybe refresh? ");
            new ResponseEntity<>(messageResponse, HttpStatus.NOT_FOUND);
        }
        TaskList taskList = taskListService.deleteTask(id);
        return new ResponseEntity<>(taskList, HttpStatus.OK);
    }

//    public ResponseEntity<TaskList> createTodo() {
//        return new ResponseEntity<>();
//    }


}
