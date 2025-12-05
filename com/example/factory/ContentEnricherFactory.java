package factory;

import tareas.ContentEnricher;
import tareas.Task;

public class ContentEnricherFactory extends TaskFactory{
    @Override
    public Task createTask() {
        return new ContentEnricher();
    }
}