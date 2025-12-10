package factory;

import pipeline.Slot;
import tareas.ContentEnricher;
import tareas.Task;

public class ContentEnricherFactory extends TaskFactory{
    

    public Task createTask(String xpath, Slot salida, Slot... entradas) {
        return new ContentEnricher(xpath, salida, entradas);
    }
}