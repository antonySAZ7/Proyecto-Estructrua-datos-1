package src;

import java.util.List;
/**
* La clase Evaluator es la que nos va a evaluar las expresiones LISP que se le pasen 
* Utiliza un diccionario (DiccionarioSD) para guardar y jalar las variables y funciones
*/
public class Evaluator {
    private DiccionarioSD diccionario;

    public Evaluator(DiccionarioSD diccionario) {
        this.diccionario = diccionario;
        }
    


/**
* Evalúa una lista de expresiones LISP
* Una lista en LISP puede representar una función o expresiones como QUOTE o COND o una lista de elementos
* @param lista La lista de expresiones LISP a evaluar
* @return El resultado de evaluar la lista
* @throws RuntimeException Si la lista está vacía o no es evaluable
 */
private Object evaluateList(LList lista) {
    List<LExpression> elementos = lista.getLista();
    if (elementos.isEmpty()) {
        throw new RuntimeException("Lista vacía no evaluable");
    }

    LExpression primerElemento = elementos.get(0);
    if (primerElemento instanceof LSymbol) {
        String operador = ((LSymbol) primerElemento).getSimbolo();
        switch (operador) {
            case "QUOTE":
                //mas adelanteeee
                break;
            case "DEFUN":
                break;
            case "SETQ":
                break;
            case "COND":
               break;
            case "IF":
                break;
            default:
                throw new RuntimeException("Este operador no se reconoce: " + operador);
        }
    }
    throw new RuntimeException("Lista no evaluable: " + lista);
}

/**
* Evalúa una expresión LISP
* Dependiendo del tipo de expresión ya sea número, símbolo o lista se realiza la evaluación que tenga que hacerse
*
* @param expression La expresión LISP a evaluar
* @return El resultado de evaluar la expresión
* @throws RuntimeException Si la expresión no es válida o no está definida
*/

    public Object evaluate(LExpression expression) {
        if (expression instanceof LNumber) {
            return ((LNumber) expression).getValor();
        } else if (expression instanceof LSymbol) {
            String simbolo = ((LSymbol) expression).getSimbolo();
            Object valor = diccionario.getVariable(simbolo);
            if (valor == null) {
                throw new RuntimeException("variable no definida: " + simbolo);
            }
            return valor;
        } else if (expression instanceof LList) {
            return evaluateList((LList) expression);
        }
        throw new RuntimeException("expresió no válida: " + expression);
    }

}