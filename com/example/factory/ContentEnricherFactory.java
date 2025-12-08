package factory;

import pipeline.Slot;
import tareas.ContentEnricher;
import tareas.Task;

public class ContentEnricherFactory extends TaskFactory{
    @Override
    public Task createTask() {
        return null;
    }

    public Task createTask(String xpath, Slot salida, Slot... entradas) {
        return new ContentEnricher(xpath, salida, entradas);
    }
}