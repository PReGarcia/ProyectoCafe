package tareas;

import java.util.List;

import utils.Message;

public interface Task {
    public List<Message> execute(Message inputMessage) throws Exception;
}