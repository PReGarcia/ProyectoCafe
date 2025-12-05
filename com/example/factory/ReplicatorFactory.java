package factory;

import tareas.Replicator;
import tareas.Task;

public class ReplicatorFactory extends TaskFactory{
    @Override
    public Task createTask() {
        return new Replicator();
    }

}
