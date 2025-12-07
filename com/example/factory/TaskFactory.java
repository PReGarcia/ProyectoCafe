package factory;

import pipeline.Slot;
import tareas.Task;

public abstract class TaskFactory {
    public abstract Task createTask();
}
