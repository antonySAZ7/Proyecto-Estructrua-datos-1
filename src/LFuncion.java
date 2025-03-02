
package src;
import java.util.List;

public class LFuncion{
    private List<String> evaluandos;
    private LExpression expresion; 


    public LFuncion(List<String> evaluandos, LExpression expresion){
        this.evaluandos = evaluandos;
        this.expresion = expresion;
    }

    public List<String> getEvaluandos(){
        return evaluandos; 
    }

    public LExpression getExpresion() {
        return expresion;
    }
}
