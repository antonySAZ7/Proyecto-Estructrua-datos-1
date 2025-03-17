package src;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class LispInterpreter {
    private Tokennator tokennator;
    private Convertidor convertidor;
    private Evaluator evaluator;

    public LispInterpreter() {
        this.tokennator = new Tokennator();
        this.convertidor = new Convertidor();
        this.evaluator = new Evaluator(new DiccionarioSD());
    }

    public void interpret(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            StringBuilder codigoCompleto = new StringBuilder();
            int contadorParentesis = 0;
            String linea;
            while ((linea = br.readLine()) != null) {
                codigoCompleto.append(linea).append(" ");

                for(char c : linea.toCharArray()) {
                    if (c == '(') contadorParentesis++;
                    if (c == ')') contadorParentesis--;
                }
                if(contadorParentesis == 0 && !codigoCompleto.toString().trim().isEmpty()){
                    List<String> tokens = tokennator.tokennate(codigoCompleto.toString().trim());
                    LExpression expresion = convertidor.convertidor(tokens);
                    Object resultado = evaluator.evaluate(expresion);
                    System.out.println("Resultado: " + resultado);
                    codigoCompleto.setLength(0);
                }
               
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LispInterpreter interpreter = new LispInterpreter();
        interpreter.interpret("texto1.txt");
    }
}