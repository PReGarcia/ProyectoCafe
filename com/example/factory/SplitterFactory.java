package factory;

import tareas.Splitter;
import tareas.Task;

public class SplitterFactory extends TaskFactory{

    @Override
    public Task createTask() {
        return new Splitter();
    }
    
}
