package com.nadetdev.qrksmicrosvc.task;

import com.nadetdev.qrksmicrosvc.user.UserService;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.security.UnauthorizedException;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.ObjectNotFoundException;

import java.time.ZonedDateTime;
import java.util.List;

@ApplicationScoped
public class TaskService {
    private final UserService userService;

    @Inject
    public TaskService(UserService userService) {
        this.userService = userService;
    }

    @WithSession
    public Uni<Task> finddById(long id){
        return userService.getCurrentUser()
                .chain(user -> Task.<Task>findById(id)
                        .onItem().ifNull().failWith(
                                () -> new ObjectNotFoundException(id, "Task")
                        )
                        .onItem().invoke(
                                Unchecked.consumer(task -> {
                                    if(!user.equals(task.user)) {
                                        throw new UnauthorizedException("You are not allowed to update this task");
                                    }
                                })
                        )
                );
    }

    @WithSession
    public Uni<List<Task>> listForUser() {
        return  userService.getCurrentUser()
                .chain(user -> Task.find("user", user).list());
    }

    @WithTransaction
    public Uni<Task> create(Task task){
        return userService.getCurrentUser()
                .chain(user -> {
                    task.user = user;
                    return task.persistAndFlush();
                });
    }

    @WithTransaction
    public Uni<Task> update(Task task){
        return finddById(task.id)
                .chain(t -> Task.getSession())
                .chain(s -> s.merge(task));
    }

    @WithTransaction
    public Uni<Void> delete(long id){
        return finddById(id)
                .chain(Task::delete);
    }

    @WithTransaction
    public Uni<Boolean> setComplete(long id, boolean complete) {
        return finddById(id)
                .chain(task -> {
                    task.complete = complete ? ZonedDateTime.now() : null;
                    return task.persistAndFlush();
                })
                .chain(task -> Uni.createFrom().item(complete));
    }
}
