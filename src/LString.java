package src;
//tuvimos que hacer esta clase para manejar los strings dentro de comillas
public class LString  extends LExpression {
    private final String valor;

    public LString(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

@Override
    public String toString() {
        return valor;
    }
}

