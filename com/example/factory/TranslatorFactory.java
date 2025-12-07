package factory;

import tareas.Task;
import tareas.Translator;

public class TranslatorFactory extends TaskFactory{
    private String xslt;

    @Override
    public Task createTask() {
        return null;
    }

    public Task createTask(String xslt, pipeline.Slot entrada, pipeline.Slot salida) {
        return new Translator(xslt, entrada, salida);
    }

}
