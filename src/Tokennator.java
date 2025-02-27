

package src;
import java.util.ArrayList;
import java.util.List;

public class Tokennator {


    public List<String> tokennate(String entranteCodigo){
        List<String> tokens = new ArrayList<String>();
        StringBuilder token =  new StringBuilder();
        for (char c : entranteCodigo.toCharArray())  {
           if(Character.isWhitespace(c)){
            if(token.length() > 0){
                tokens.add(token.toString());
                token.setLength(0);
            }
           }else if(c =='(' || c==')'){
            if(token.length()>0){
                tokens.add(token.toString());
                token.setLength(0);
            }
            tokens.add(String.valueOf(c));
           }else {
            token.append(c);
           }
        }
        if(token.length() > 0 ){
            tokens.add(token.toString());
        }        
        return tokens;


    }

    
}
