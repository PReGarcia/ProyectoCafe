package conexiones;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import infraestructura.DatabaseConnection;
import tareas.Task;
import utils.Message;

public class DatabaseQueryTask implements Task {

    @Override
    public List<Message> execute(Message inputMessage) throws Exception {
        System.out.println("Ejecutando DatabaseQueryTask (Procesando solicitud)...");

        String querySql = (String) inputMessage.getPayload();
        
        List<Map<String, Object>> resultados = new ArrayList<>();
        Connection conn = DatabaseConnection.getInstance().getConnection();

        if (conn == null) {
            System.out.println("   [!] AVISO: No se detectó base de datos. Ejecutando en MODO SIMULACIÓN.");
            System.out.println("       Consulta recibida: " + querySql);
            
            Map<String, Object> filaFalsa = new HashMap<>();
            filaFalsa.put("info", "Datos simulados");
            filaFalsa.put("status", "OK_SIMULADO");
            resultados.add(filaFalsa);
            
            Message respuesta = new Message(resultados);
            respuesta.setHeader("format", "DB_RESPONSE_SIMULATED");
            return Collections.singletonList(respuesta);
        }
        try (PreparedStatement stmt = conn.prepareStatement(querySql);
             ResultSet rs = stmt.executeQuery()) {

            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= colCount; i++) {
                    row.put(meta.getColumnName(i), rs.getObject(i));
                }
                resultados.add(row);
            }
        }

        Message respuesta = new Message(resultados);
        respuesta.setHeader("format", "DB_RESPONSE");
        return Collections.singletonList(respuesta);
    }
}