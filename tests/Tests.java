package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import src.Convertidor;
import src.DiccionarioSD;
import src.Evaluator;
import src.LExpression;
import src.LList;
import src.LNumber;
import src.LString;
import src.LSymbol;
import src.Tokennator;

public class Tests {
    private final Tokennator tokennator = new Tokennator();
    private final Convertidor convertidor = new Convertidor();
    private final DiccionarioSD diccionario = new DiccionarioSD();
    private final Evaluator evaluator = new Evaluator(diccionario);

    @Test
    public void testTokennate() {
        List<String> tokens = tokennator.tokennate("(+ (* 3 4) (- 5 2))");
        assertEquals(List.of("(", "+", "(", "*", "3", "4", ")", "(", "-", "5", "2", ")", ")"), tokens);
    }

    @Test
    public void testConversion() {
        LExpression resultado = convertidor.convertidor(new ArrayList<>(List.of("(", "+", "1", "2", ")")));
        assertTrue(resultado instanceof LList);
        List<LExpression> expresiones = ((LList) resultado).getLista();

        assertEquals(3, expresiones.size());
        assertTrue(expresiones.get(0) instanceof LSymbol);
        assertTrue(expresiones.get(1) instanceof LNumber);
        assertTrue(expresiones.get(2) instanceof LNumber);
    }

    @Test
    public void testEvaluarAritmetica() {
        LList suma = new LList(List.of(new LSymbol("+"), new LNumber(3), new LNumber(5)));
        LList multiplicacion = new LList(List.of(new LSymbol("*"), new LNumber(2), new LNumber(4)));

        assertEquals(8.0, evaluator.evaluate(suma));
        assertEquals(8.0, evaluator.evaluate(multiplicacion));
    }

    @Test
    public void testEvaluarComparacion() {
        LList mayorQue = new LList(List.of(new LSymbol(">"), new LNumber(10), new LNumber(5)));
        LList menorQue = new LList(List.of(new LSymbol("<"), new LNumber(3), new LNumber(7)));
        LList igualQue = new LList(List.of(new LSymbol("="), new LNumber(4), new LNumber(4)));

        assertTrue((Boolean) evaluator.evaluate(mayorQue));
        assertTrue((Boolean) evaluator.evaluate(menorQue));
        assertTrue((Boolean) evaluator.evaluate(igualQue));
    }

    @Test
    public void testDefinirFuncion() {
        List<LExpression> elementos = new ArrayList<>();
        elementos.add(new LSymbol("DEFUN"));
        elementos.add(new LSymbol("SUMAR"));
        elementos.add(new LList(List.of(new LSymbol("X"), new LSymbol("Y"))));
        elementos.add(new LList(List.of(new LSymbol("+"), new LSymbol("X"), new LSymbol("Y"))));

        Object resultado = evaluator.evaluate(new LList(elementos));
        assertEquals("SUMAR", resultado);

        assertNotNull(diccionario.getFuncion("SUMAR"));

        List<LExpression> llamadaFuncion = new ArrayList<>();
        llamadaFuncion.add(new LSymbol("SUMAR"));
        llamadaFuncion.add(new LNumber(3));
        llamadaFuncion.add(new LNumber(4));

        Object resultadoSuma = evaluator.evaluate(new LList(llamadaFuncion));
        assertEquals(7.0, resultadoSuma);
    }

    @Test
    public void testEvaluarIfTrue() {
        List<LExpression> elementos = new ArrayList<>();
        elementos.add(new LSymbol("IF"));
        elementos.add(new LSymbol("T")); 
        elementos.add(new LNumber(10));
        elementos.add(new LNumber(20));

        Object resultado = evaluator.evaluate(new LList(elementos));
        assertEquals(10.0, resultado);
    }

    @Test
    public void testEvaluarIfFalse() {
        List<LExpression> elementos = new ArrayList<>();
        elementos.add(new LSymbol("IF"));
        elementos.add(new LList(List.of(new LSymbol("<"), new LNumber(5), new LNumber(3))));
        elementos.add(new LNumber(10));
        elementos.add(new LNumber(20));

        Object resultado = evaluator.evaluate(new LList(elementos));
        assertEquals(20.0, resultado);
    }

    @Test
    public void EvaluarCondicional() {
        List<LExpression> elementos = new ArrayList<>();
        elementos.add(new LSymbol("COND"));
        elementos.add(new LList(List.of(new LList(List.of(new LSymbol(">"), new LNumber(5), new LNumber(3))), new LString("MAYOR"))));
        elementos.add(new LList(List.of(new LList(List.of(new LSymbol("="), new LNumber(5), new LNumber(5))), new LString("IGUAL"))));
        elementos.add(new LList(List.of(new LSymbol("T"), new LString("NINGUNA"))));

        Object resultado = evaluator.evaluate(new LList(elementos));
        assertEquals("MAYOR", resultado);
    }
}
