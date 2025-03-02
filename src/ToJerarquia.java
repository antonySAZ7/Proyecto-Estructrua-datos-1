


package src;
import java.util.ArrayList;
import java.util.List;

public class ToJerarquia{
    public LExpression tojera(List<String> tokens){
        if(tokens.isEmpty()){
            throw new IllegalArgumentException("No se puede analizar porque esta vacia");
        }

        String primero = tokens.remove(0);
        if(primero.equals("()")){
            List<LExpression> expresiones = new ArrayList<LExpression>();
            while(!tokens.isEmpty() && !tokens.get(0).equals(")")){
                expresiones.add(tojera(tokens));
            }
            if(!tokens.isEmpty()){
                tokens.remove(0);
            }
            return new LList(expresiones);

        }else if(primero.matches("-?\\d+(\\.\\d+)?")){
            return new LNumber(Double.parseDouble(primero));
        }else {
            return new LSymbol(primero);
        }
    }
        





    }