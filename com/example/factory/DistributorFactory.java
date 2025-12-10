package factory;

import java.util.List;

import pipeline.Slot;
import tareas.Distributor;
import tareas.Task;

public class DistributorFactory extends TaskFactory {

    public Task createTask(List<String> xpath, Slot entrada, List<Slot> salidas) {
        return new Distributor(xpath, entrada, salidas);
    }
}