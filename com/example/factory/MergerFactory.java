package factory;

import tareas.Merger;
import tareas.Task;

public class MergerFactory extends TaskFactory{
    @Override
    public Task createTask() {
        return new Merger();
    }

}
