package factory;

import tareas.Task;
import tareas.Translator;

public class TranslatorFactory extends TaskFactory{

    @Override
    public Task createTask() {
        return new Translator();
    }

}
