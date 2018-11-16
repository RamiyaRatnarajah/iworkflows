package com.kajan.iworkflows.workflow;

import com.kajan.iworkflows.workflow.dto.MyTaskBasicDetails;
import com.kajan.iworkflows.workflow.dto.SubmittedRequestBasicDetails;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.rest.dto.task.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static com.kajan.iworkflows.util.WorkflowConstants.RECOMMENDATION_KEY;

@RestController
@RequestMapping("api/v1/camunda/")
@Slf4j
public class CamundaTaskController {

    private final TaskService taskService;
    private final RuntimeService runtimeService;

    @Autowired
    public CamundaTaskController(TaskService taskService, RuntimeService runtimeService) {
        this.taskService = taskService;
        this.runtimeService = runtimeService;
    }

    /**
     * Get the list of all tasks.
     * Only the user with 'ROLE_ADMIN' can access this end point.
     *
     * @return the list of all tasks.
     */
    @GetMapping("all-tasks")
    @PreAuthorize("hasRole('ADMIN')")
    public List<TaskDto> getAllTasks() {
        List<TaskDto> result = new ArrayList<>();
        taskService
                .createTaskQuery().list()
                .forEach(task -> {
                    TaskDto taskDto = TaskDto.fromEntity(task);
                    result.add(taskDto);
                });
        return result;
    }

    /**
     * Get the list of tasks that the requesting user is assigned to
     *
     * @param principal the logged in user
     * @return the list of tasks that the requesting user is assigned to
     */
    @GetMapping("my-tasks")
    public List<MyTaskBasicDetails> getTasks(Principal principal) {
        List<MyTaskBasicDetails> result = new ArrayList<>();
        taskService
                .createTaskQuery().list().stream()
                .filter(task -> task.getAssignee() != null && task.getAssignee().equalsIgnoreCase(principal.getName()))
                .forEach(task -> {
                    MyTaskBasicDetails myTaskBasicDetails = MyTaskBasicDetails.fromTask(task);
                    myTaskBasicDetails.setRecommendation((String) runtimeService.getVariable(task.getProcessInstanceId(), RECOMMENDATION_KEY));
                    result.add(myTaskBasicDetails);
                });
        return result;
    }

    /**
     * Get the list of tasks that the requesting user submitted
     *
     * @param principal the logged in user
     * @return the list of tasks that the requesting user submitted
     */
    @GetMapping("submitted-tasks")
    public List<SubmittedRequestBasicDetails> getSubmittedRequests(Principal principal) {
        List<SubmittedRequestBasicDetails> submittedRequestBasicDetails = new ArrayList<>();
        taskService
                .createTaskQuery().list().stream()
                .filter(task -> task.getOwner() != null && task.getOwner().equalsIgnoreCase(principal.getName()))
                .forEach(task -> {
                    SubmittedRequestBasicDetails request = SubmittedRequestBasicDetails.fromTask(task);
                    submittedRequestBasicDetails.add(request);
                });
        return submittedRequestBasicDetails;
    }
}
