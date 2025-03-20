package src;

import java.util.ArrayList;
import java.util.List;

public class Tokennator {
    public List<String> tokennate(String entranteCodigo) {
        List<String> tokens = new ArrayList<>();
        StringBuilder token = new StringBuilder();
        int contadorParentesis = 0;
        boolean dentroDeString = false;

        for (char c : entranteCodigo.toCharArray()) {
            if (c == '"') {
                dentroDeString = !dentroDeString; // Cambia el estado (dentro o fuera de un string) para arreglar el problema de las comillas
                token.append(c);
            } else if (dentroDeString) {
                token.append(c);
            } else if (Character.isWhitespace(c)) {
                // Si encontramos un espacio y no estamos dentro de un string, finalizamos el token actual
                if (token.length() > 0) {
                    tokens.add(token.toString());
                    token.setLength(0);
                }
            } else if (c == '(') {
                contadorParentesis++;
                if (token.length() > 0) {
                    tokens.add(token.toString());
                    token.setLength(0);
                }
                tokens.add("(");
            } else if (c == ')') {
                contadorParentesis--;
                if (contadorParentesis < 0) {
                    throw new IllegalArgumentException("Error en la sintaxis, revisa la cantidad de paréntesis");
                }
                if (token.length() > 0) {
                    tokens.add(token.toString());
                    token.setLength(0);
                }
                tokens.add(")");
            } else {
                token.append(c);
            }
        }

        if (token.length() > 0) {
            tokens.add(token.toString());
        }

        if (contadorParentesis != 0) {
            throw new IllegalArgumentException("Error en la sintaxis, posiblemente falten o sobren paréntesis");
        }

        return tokens;
    }

    public boolean hasChar(StringBuilder token) {
        return token.length() > 0;
    }
}