package src;

import java.util.ArrayList;
import java.util.List;

public class Convertidor {
    public LExpression convertidor(List<String> tokens) {
        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("No se puede analizar porque está vacía");
        }

        String primero = tokens.remove(0);
        if (primero.equals("(")) {
            List<LExpression> expresiones = new ArrayList<>();
            while (!tokens.isEmpty() && !tokens.get(0).equals(")")) {
                expresiones.add(convertidor(tokens));
            }
            if (tokens.isEmpty()) {
                throw new IllegalArgumentException("Error en la sintaxis, posiblemente falte un ')'");
            }
            tokens.remove(0); // Remover el parentesis de cierre ")"
            return new LList(expresiones);
        } else if (primero.equals(")")) {
            throw new IllegalArgumentException("Error de sintaxis, paréntesis de cierre inesperado");
        } else if (primero.matches("-?\\d+(\\.\\d+)?")) {
            return new LNumber(Double.parseDouble(primero));
        } else if (primero.startsWith("\"") && primero.endsWith("\"")) {
            // Si el token es un string entre comillas, crear una instancia de LString y devolverla para evitar nuestros errores al leer las comillas
            return new LString(primero.substring(1, primero.length() - 1));
        } else {
            return new LSymbol(primero);
        }
    }
}