package tareas;

import java.util.ArrayList;
import java.util.List;

import utils.Message;

public class Replicator implements Task {

    @Override
    public List<Message> execute(Message inputMessage) {
        List<Message> copies = new ArrayList<>();
        copies.add(inputMessage.cloneMessage());
        copies.add(inputMessage.cloneMessage());
        
        return copies;
    }
}