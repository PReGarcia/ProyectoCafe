package factory;

import pipeline.Slot;
import tareas.Replicator;
import tareas.Task;

public class ReplicatorFactory extends TaskFactory{
    

    public Task createTask(Slot input, Slot... output) {
        return new Replicator(input, output);
    }

}
