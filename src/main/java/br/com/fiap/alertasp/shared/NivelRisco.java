package br.com.fiap.alertasp.shared;

public enum NivelRisco {
    BAIXO, MEDIO, ALTO, CRITICO;

    public boolean geraAlerta() {
        return compareTo(ALTO) >= 0;
    }
}
