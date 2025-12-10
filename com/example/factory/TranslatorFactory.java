package factory;

import tareas.Task;
import tareas.Translator;

public class TranslatorFactory extends TaskFactory{

    public Task createTask(String xslt, pipeline.Slot entrada, pipeline.Slot salida) {
        return new Translator(xslt, entrada, salida);
    }

}
