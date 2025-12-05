package utils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class Singleton {
    private Document arbol;
    private String expresion;
    public Singleton(Document arbol, String expresion) {
        this.arbol = arbol;
        this.expresion = expresion;
    }
    public Document getArbol() {
        return arbol;
    }
    public void quitarNodos() throws Exception{
        Node nodo  = XmlUtils.NodeSearch(arbol, expresion);
        nodo.getParentNode().removeChild(nodo);
    }

}
