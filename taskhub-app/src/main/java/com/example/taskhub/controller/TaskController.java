package com.example.taskhub.controller;

import com.example.taskhub.dto.TaskForm;
import com.example.taskhub.model.Task;
import com.example.taskhub.model.TaskStatus;
import com.example.taskhub.model.User;
import com.example.taskhub.service.TaskService;
import com.example.taskhub.service.UserService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @ModelAttribute("statuses")
    public TaskStatus[] statuses() {
        return TaskStatus.values();
    }

    @GetMapping("/dashboard")
    public String dashboard(Principal principal, Model model) {
        User user = userService.getByEmail(principal.getName());
        List<Task> tasks = taskService.listFor(user);
        model.addAttribute("user", user);
        model.addAttribute("tasks", tasks);
        model.addAttribute("todoCount", count(tasks, TaskStatus.TODO));
        model.addAttribute("progressCount", count(tasks, TaskStatus.IN_PROGRESS));
        model.addAttribute("doneCount", count(tasks, TaskStatus.DONE));
        return "dashboard";
    }

    @GetMapping("/tasks/new")
    public String newTask(Model model) {
        model.addAttribute("taskForm", new TaskForm());
        model.addAttribute("heading", "New Task");
        return "task-form";
    }

    @GetMapping("/tasks/{id}/edit")
    public String editTask(@PathVariable Long id, Principal principal, Model model) {
        User user = userService.getByEmail(principal.getName());
        Task task = taskService.getOwned(id, user);
        TaskForm form = new TaskForm();
        form.setId(task.getId());
        form.setTitle(task.getTitle());
        form.setDescription(task.getDescription());
        form.setStatus(task.getStatus());
        form.setDueDate(task.getDueDate());
        model.addAttribute("taskForm", form);
        model.addAttribute("heading", "Edit Task");
        return "task-form";
    }

    @PostMapping("/tasks/save")
    public String save(@Valid @ModelAttribute("taskForm") TaskForm form,
                       BindingResult result,
                       Principal principal,
                       Model model,
                       RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("heading", form.getId() == null ? "New Task" : "Edit Task");
            return "task-form";
        }
        taskService.save(form, userService.getByEmail(principal.getName()));
        redirect.addFlashAttribute("success", "Task saved.");
        return "redirect:/dashboard";
    }

    @PostMapping("/tasks/{id}/status")
    public String changeStatus(@PathVariable Long id, @RequestParam TaskStatus status, Principal principal) {
        taskService.changeStatus(id, status, userService.getByEmail(principal.getName()));
        return "redirect:/dashboard";
    }

    @PostMapping("/tasks/{id}/delete")
    public String delete(@PathVariable Long id, Principal principal, RedirectAttributes redirect) {
        taskService.delete(id, userService.getByEmail(principal.getName()));
        redirect.addFlashAttribute("success", "Task deleted.");
        return "redirect:/dashboard";
    }

    private long count(List<Task> tasks, TaskStatus status) {
        return tasks.stream().filter(t -> t.getStatus() == status).count();
    }
}
