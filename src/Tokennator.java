

package src;
import java.util.ArrayList;
import java.util.List;

public class Tokennator {
    public List<String> tokennate(String entranteCodigo){
        List<String> tokens = new ArrayList<>();
        StringBuilder token =  new StringBuilder();
        int contadorParentesis = 0;

        for (char c : entranteCodigo.toCharArray())  {
            //Agregar caracter de espacio en blanco
            if(Character.isWhitespace(c)){
                if(hasChar(token)){
                    tokens.add(token.toString());
                    token.setLength(0);
                }
            }
            //Agregar inicio de parentesis
            else if(c =='('){
                contadorParentesis++;
                if(hasChar(token)){
                    tokens.add(token.toString());
                    token.setLength(0);
                }
                tokens.add("(");
            }
            //Agregar cierre de paréntesis
            else if (c == ')'){
                contadorParentesis--;
                if(contadorParentesis < 0){
                    throw new IllegalArgumentException("Error en la sintaxis, revisa la cantidad de parentesis");
                }
                
                if(hasChar(token)){
                    tokens.add(token.toString());
                    token.setLength(0);
                }
                token.append(")");
            } 

            else {
                token.append(c);
            }
        }
        if(hasChar(token)){
            tokens.add(token.toString());
        }        

        if(contadorParentesis != 0 ){
            throw new IllegalArgumentException("Error en la sistaxis, posiblemente falten o sobren parentesis");
        }

        return tokens;


    }
    
    public boolean hasChar(StringBuilder token){
        return token.length()>0;
    }
}
