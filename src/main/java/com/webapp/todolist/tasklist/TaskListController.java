package com.webapp.todolist.tasklist;

import com.webapp.todolist.appuser.AppUserDetails;
import com.webapp.todolist.exceptions.ApiRequestException;
import com.webapp.todolist.exceptions.ListNotFoundException;
import com.webapp.todolist.messageresponse.MessageResponse;
import com.webapp.todolist.tasklist.task.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    public ResponseEntity<?> getListById(@PathVariable("id") Long id, Model model, Authentication auth) throws NumberFormatException, ListNotFoundException {
        AppUserDetails appUserDetails = (AppUserDetails) auth.getPrincipal();
        TaskList taskList = taskListService.findById(id);
        if (taskList.getAppUserDetails().getId() != appUserDetails.getId()) {
            throw new ApiRequestException("You are not authorized to get this list");
        }
        if (!taskListRepository.existsById(id)) {
            throw new  ApiRequestException("The list that was requested was not found");
        }
        return new ResponseEntity<>(taskList, HttpStatus.OK);
    }

    @RequestMapping(value = "/getlists")
    public ResponseEntity<AllListResponse> getLists(Authentication auth) {
        AppUserDetails appUserDetails = (AppUserDetails) auth.getPrincipal();
        System.out.println(appUserDetails);
        List<TaskList> lists = taskListService.findByAppUser(appUserDetails);
        return new ResponseEntity<>(new AllListResponse(lists), HttpStatus.OK);
    }

    @PostMapping(value = "/createlist")
    public ResponseEntity<AllListResponse> createList( @RequestBody CreateListRequest createListRequest, Authentication auth) throws ParseException {
        Date dueDate = simpleDateFormat.parse(createListRequest.getDate());
        AppUserDetails appuser = (AppUserDetails) auth.getPrincipal();
        TaskList list = taskListService.addList(createListRequest.getName(), appuser, dueDate, createListRequest.getDescription());
        return new ResponseEntity<>(new AllListResponse(appuser.getListOfTaskLists()), HttpStatus.OK);

    }

    @PostMapping(value = "/deletelist")
    public ResponseEntity<?> deleteList(@RequestParam("id") Long id, Authentication auth) throws NumberFormatException, ListNotFoundException {


            if (taskListService.findById(id).getAppUserDetails().getId() != ((AppUserDetails) auth.getPrincipal()).getId()) {
                return new ResponseEntity<MessageResponse>(new MessageResponse("you do not have permision to edit this list"), HttpStatus.UNAUTHORIZED);
            }

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
    public ResponseEntity<?> editList(@RequestBody EditListRequest editListRequest, Authentication auth ) throws ParseException, ListNotFoundException {

        if (taskListService.findById(editListRequest.getId()).getAppUserDetails().getId() != ((AppUserDetails) auth.getPrincipal()).getId()) {
            throw new ApiRequestException("you are not authorized to edit this list");
        }
        Date dueDate = simpleDateFormat.parse(editListRequest.getDate());
        taskListService.editList(editListRequest.getName(), (AppUserDetails) auth.getPrincipal(), dueDate, editListRequest.getDescription(), editListRequest.getId());
        return new ResponseEntity<>(new AllListResponse(taskListService.findByAppUser((AppUserDetails) auth.getPrincipal())), HttpStatus.OK);
    }






}
