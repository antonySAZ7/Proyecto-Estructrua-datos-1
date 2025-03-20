

package src;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DiccionarioSD {
    private Map<String, Object> setVariables = new HashMap<>();
    private Map<String, LFuncion> defunfunciones = new HashMap<>();

    public void setVariable(String nombre, Object suValor){
        setVariables.put(nombre, suValor);
    }

    public Object getVariable(String nombre){
        return setVariables.getOrDefault(nombre, null);
    }

    public void setFuncion(String nombre, LFuncion sufuncion){
        defunfunciones.put(nombre, sufuncion);
    }

    public LFuncion getFuncion(String nombre){
        return defunfunciones.getOrDefault(nombre, null);
    }

    public Set<String> getTodasLasVariables() {
    return setVariables.keySet();
}

public void imprimirFunciones() {
    System.out.println("Funciones almacenadas:");
    for (String clave : defunfunciones.keySet()) {
        System.out.println("- " + clave);
    }
}
public Set<String> getTodasLasFunciones() {
    return defunfunciones.keySet();
}



    
}
