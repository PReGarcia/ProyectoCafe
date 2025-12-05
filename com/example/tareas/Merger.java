package tareas;

import java.util.Collections;
import java.util.List;

import utils.Message;

public class Merger implements Task {

    @Override
    public List<Message> execute(Message inputMessage) {
        System.out.println("Ejecutando Merger...");
        return Collections.singletonList(inputMessage);
    }
}