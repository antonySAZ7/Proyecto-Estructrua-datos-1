package src;


import java.util.stream.Collectors;
import java.util.List;

public class LList extends LExpression {
    private final List<LExpression> lista;
    
    public LList(List<LExpression> lista) {
        this.lista = lista;
    }
    
    public List<LExpression> getLista() {
        return lista;
    }
    
    public List<String> getlistaString() {
        return lista.stream().map(Object::toString).collect(Collectors.toList());
    }
}

