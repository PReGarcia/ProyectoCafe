package factory;

import pipeline.Slot;
import tareas.Splitter;
import tareas.Task;

public class SplitterFactory extends TaskFactory{

    @Override
    public Task createTask() {
        return null;
    }

    public Task createTask( String xpath,Slot entrada, Slot salida) {
        return new Splitter(xpath ,entrada, salida);
    }
    
}
