package src;

public class LNumber extends LExpression {
    private final double valor;

    public LNumber(double valor) {
        this.valor = valor;
    }

    public double getValor() {
        return valor;
    }
    
@Override
    public String toString() {
        return Double.toString(valor);
}
}
