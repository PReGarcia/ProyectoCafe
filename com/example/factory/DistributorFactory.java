package factory;

import tareas.Distributor;
import tareas.Task;

public class DistributorFactory extends TaskFactory {
    @Override
    public Task createTask() {
        return new Distributor();
    }
}