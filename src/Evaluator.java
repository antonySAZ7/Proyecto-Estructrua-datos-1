package src;

import java.util.ArrayList;
import java.util.List;

/**
* La clase Evaluator es la que nos va a evaluar las expresiones LISP que se le pasen 
* Utiliza un diccionario (DiccionarioSD) para guardar y jalar las variables y funciones
*/
public class Evaluator {
    private DiccionarioSD diccionario;

    public Evaluator(DiccionarioSD diccionario) {
        this.diccionario = diccionario;
        // Definir T como una constante verdadera por si en un cond se nos atraviesa uno
        this.diccionario.setVariable("T", true);
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
            System.out.println("Funciones disponibles en DiccionarioSD: ");
            diccionario.imprimirFunciones();
            if(diccionario.getFuncion(operador) != null){
                System.out.println("Ejecutando función de usuario: " + operador); 
                LFuncion funcion = diccionario.getFuncion(operador);
                return evaluarFuncionUsuario(funcion, elementos.subList(1, elementos.size()));
            }
            
            switch (operador) {
                case "QUOTE":
                    return elementos.get(1);
                case "DEFUN":
                    return definirFuncion(elementos);
                case "SETQ":
                    return asignarVariable(elementos);
                case "COND":
                    return evaluarCondicional(elementos);
                case "IF":
                    return evaluarIf(elementos);
                case "+":
                case "-":
                case "*":
                case "/":
                    return evaluarAritmetica(operador, elementos.subList(1, elementos.size()));
                case "=":
                case "<":
                case ">":
                case "<=":
                case ">=":
                    return evaluarComparacion(operador, elementos.subList(1, elementos.size()));
                default:
                    throw new RuntimeException("Este operador no se reconoce: " + operador + 
                    ". Asegúrate de que la función esté bien definida.");
            }
        }
        throw new RuntimeException("Lista no evaluable: " + lista);
    }

    private Object evaluarFuncionUsuario(LFuncion funcion, List<LExpression> argumentos) {
        List<String> parametros = funcion.getEvaluandos();
    
        if (parametros.size() != argumentos.size()) {
            throw new RuntimeException("Número incorrecto de argumentos para la función.");
        }
    
        DiccionarioSD diccionarioTemporal = new DiccionarioSD();
        for (String variable : diccionario.getTodasLasVariables()) {
            diccionarioTemporal.setVariable(variable, diccionario.getVariable(variable));
        }

        for(String funcionNombre : diccionario.getTodasLasFunciones()){
            diccionarioTemporal.setFuncion(funcionNombre, diccionario.getFuncion(funcionNombre));
        }
        

        for (int i = 0; i < parametros.size(); i++) {
            diccionarioTemporal.setVariable(parametros.get(i), evaluate(argumentos.get(i)));
        }

        Evaluator subEvaluador = new Evaluator(diccionarioTemporal);
        return subEvaluador.evaluate(funcion.getExpresion());
    
        
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
        } else if (expression instanceof LString) {
            return ((LString) expression).getValor(); // Manejar strings como valores literales por si es que están dentro de comillas
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
        throw new RuntimeException("expresión no válida: " + expression);
    }

    /**
    * Define una nueva función en el diccionario
    *
    * @param elementos La lista de elementos que define la función con su nombre, parámetros y cuerpo
    * @return El nombre de la función definida
    * @throws RuntimeException Si la sintaxis de DEFUN es incorrecta
    */
    private Object definirFuncion(List<LExpression> elementos) {
        if (elementos.size() < 4) {
            throw new RuntimeException("Sintaxis incorrecta para DEFUN");
        }
        LExpression nombreExp = elementos.get(1);
        if (!(nombreExp instanceof LSymbol)) {
        throw new RuntimeException("El nombre de la función debe ser un símbolo.");
        }
        String nombreFuncion = ((LSymbol) nombreExp).getSimbolo();
        LExpression parametrosExp = elementos.get(2);
        if (!(parametrosExp instanceof LList)) {
        throw new RuntimeException("Los parámetros deben estar en una lista.");
        }


        List<String> parametros = ((LList) parametrosExp).getlistaString();
        LExpression cuerpo = elementos.get(3);
        LFuncion funcion = new LFuncion(parametros, cuerpo);
        diccionario.setFuncion(nombreFuncion, funcion);
        System.out.println("Función definida correctamente: " + nombreFuncion);
        return nombreFuncion;
    }

    /**
    * Asigna un valor a una variable en el diccionario
    *
    * @param elementos La lista de elementos (nombre de la variable y valor)
    * @return El valor asignado
    * @throws RuntimeException Si la sintaxis de SETQ está mal
    */
    private Object asignarVariable(List<LExpression> elementos) {
        if (elementos.size() != 3) {
            throw new RuntimeException("Sintaxis incorrecta para SETQ");
        }
        String nombreVariable = ((LSymbol) elementos.get(1)).getSimbolo();
        Object valor = evaluate(elementos.get(2));
        diccionario.setVariable(nombreVariable, valor);
        return valor;
    }

    /**
    * Evalúa una expresión COND
    * @param elementos La lista de elementos que define las condiciones y sus resultados
    * @return El resultado de la primera condición verdadera
    * @throws RuntimeException Si la sintaxis de COND es incorrecta
    */
    private Object evaluarCondicional(List<LExpression> elementos) {
        for (int i = 1; i < elementos.size(); i++) {
            LExpression condicion = elementos.get(i);
            if (!(condicion instanceof LList)) {
                throw new RuntimeException("Condición no válida en COND");
            }
            List<LExpression> clausula = ((LList) condicion).getLista();
            if (clausula.size() != 2) {
                throw new RuntimeException("Cláusula COND debe tener dos elementos");
            }
            Object resultadoCondicion = evaluate(clausula.get(0));
            if (resultadoCondicion instanceof Boolean && (Boolean) resultadoCondicion || 
                resultadoCondicion.equals("T")) {
                return evaluate(clausula.get(1));
            }
        }
        return null; 
    }

/**
* Evalúa un IF
*
* @param elementos La lista de elementos que define la condición, el valor si es verdadero y el valor si es falso
* @return El resultado de evaluar la condición
* @throws RuntimeException Si la sintaxis de IF es incorrecta
*/
private Object evaluarIf(List<LExpression> elementos) {
    System.out.println("Elementos en IF: " + elementos); // Depuración
    if (elementos.size() != 4) {
        throw new RuntimeException("Sintaxis incorrecta para IF");
    }
    Object condicion = evaluate(elementos.get(1));
    if (condicion instanceof Boolean && (Boolean) condicion) {
        return evaluate(elementos.get(2));
    } else {
        return evaluate(elementos.get(3));
    }
}

    /**
    * Evalúa una operación aritmética (+, -, *, /)
    *
    * @param operador El operador aritmético (+, -, *, /)
    * @param argumentos Los argumentos de la operación
    * @return El resultado de la operación
    * @throws RuntimeException Si los argumentos no son válidos o si hay división por cero
    */
    private Object evaluarAritmetica(String operador, List<LExpression> argumentos) {
        if (argumentos.isEmpty()) {
            throw new RuntimeException("Se necesitan argumentos para la operación: " + operador);
        }

        double resultado = (Double) evaluate(argumentos.get(0));
        for (int i = 1; i < argumentos.size(); i++) {
            Object valor = evaluate(argumentos.get(i));
            if (!(valor instanceof Double)) {
                throw new RuntimeException("Argumento no válido para la operación " + operador + ": " + valor);
            }
            double valorNumerico = (Double) valor;

            switch (operador) {
                case "+":
                    resultado += valorNumerico;
                    break;
                case "-":
                    resultado -= valorNumerico;
                    break;
                case "*":
                    resultado *= valorNumerico;
                    break;
                case "/":
                    if (valorNumerico == 0) {
                        throw new RuntimeException("División por cero");
                    }
                    resultado /= valorNumerico;
                    break;
                default:
                    throw new RuntimeException("Este operador no se reconoce: " + operador);
            }
        }
        return resultado;
    }

    /**
    * Evalúa una comparación entre números (=, <, >, <=, >=)
    *
    * @param operador El operador (=, <, >, <=, >=)
    * @param argumentos Los argumentos de la comparación
    * @return true o false, dependiendo del resultado de la comparación
    * @throws RuntimeException Si los argumentos no son números o si la cantidad de argumentos a comparar es menor a 2
    */
    private Object evaluarComparacion(String operador, List<LExpression> argumentos) {
        if (argumentos.size() < 2) {
            throw new RuntimeException("Se necesitan al menos 2 argumentos para la comparación: " + operador);
        }

        List<Double> numeros = new ArrayList<>();
        for (LExpression arg : argumentos) {
            Object valor = evaluate(arg);
            if (!(valor instanceof Double)) {
                throw new RuntimeException("Los argumentos de la comparación deben ser números: " + operador);
            }
            numeros.add((Double) valor);
        }

        switch (operador) {
            case "=":
                for (int i = 1; i < numeros.size(); i++) {
                    if (!numeros.get(i - 1).equals(numeros.get(i))) {
                        return false;
                    }
                }
                return true;
            case "<":
                for (int i = 1; i < numeros.size(); i++) {
                    if (numeros.get(i - 1) >= numeros.get(i)) {
                        return false;
                    }
                }
                return true;
            case ">":
                for (int i = 1; i < numeros.size(); i++) {
                    if (numeros.get(i - 1) <= numeros.get(i)) {
                        return false;
                    }
                }
                return true;
            case "<=":
                for (int i = 1; i < numeros.size(); i++) {
                    if (numeros.get(i - 1) > numeros.get(i)) {
                        return false;
                    }
                }
                return true;
            case ">=":
                for (int i = 1; i < numeros.size(); i++) {
                    if (numeros.get(i - 1) < numeros.get(i)) {
                        return false;
                    }
                }
                return true;
            default:
                throw new RuntimeException("este operador de comparacion no se ha reconocido" + operador);
        }
    }
}