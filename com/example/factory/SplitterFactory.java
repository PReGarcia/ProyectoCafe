package factory;

import pipeline.Slot;
import tareas.Splitter;
import tareas.Task;

public class SplitterFactory extends TaskFactory{

    public Task createTask( String xpath,Slot entrada, Slot salida) {
        return new Splitter(xpath ,entrada, salida);
    }
    
}
