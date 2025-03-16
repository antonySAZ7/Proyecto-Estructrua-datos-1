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
            String linea;
            while ((linea = br.readLine()) != null) {
                List<String> tokens = tokennator.tokennate(linea);
                LExpression expresion = convertidor.convertidor(tokens);
                Object resultado = evaluator.evaluate(expresion);
                System.out.println("Resultado: " + resultado);
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