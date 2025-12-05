package factory;

import tareas.Task;

public abstract class TaskFactory {
    public Task buildTask() {
        Task task = createTask();
        return task;
    }
    public abstract tareas.Task createTask();
}
