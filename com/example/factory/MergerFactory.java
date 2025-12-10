package factory;

import pipeline.Slot;
import tareas.Merger;
import tareas.Task;

public class MergerFactory extends TaskFactory{
    

    public Task createTask(Slot[] entradas, Slot salida) {
        return new Merger(entradas, salida);
    }

}
