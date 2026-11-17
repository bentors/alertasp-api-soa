package br.com.fiap.alertasp.shared;

public final class RegraRisco {

    private RegraRisco() {
    }

    /** Nível da água em cm a partir da altura do sensor e da distância medida até a superfície. */
    public static double nivelAgua(double alturaInstalacaoCm, double distanciaCm) {
        return Math.max(0, alturaInstalacaoCm - distanciaCm);
    }

    public static NivelRisco nivelPorMedicao(double nivelAguaCm, double limiteAlertaCm) {
        if (nivelAguaCm >= limiteAlertaCm) {
            return NivelRisco.ALTO;
        }
        if (nivelAguaCm >= 0.5 * limiteAlertaCm) {
            return NivelRisco.MEDIO;
        }
        return NivelRisco.BAIXO;
    }
}
