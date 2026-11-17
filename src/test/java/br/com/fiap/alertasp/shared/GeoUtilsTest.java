package br.com.fiap.alertasp.shared;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class GeoUtilsTest {

    static final Coordenada PRACA_DA_SE = new Coordenada(-23.5503, -46.6339);
    static final Coordenada MASP = new Coordenada(-23.5614, -46.6559);

    @Test
    void distanciaMetros_entreSeEMasp_aproximadamente2560() {
        assertThat(GeoUtils.distanciaMetros(PRACA_DA_SE, MASP)).isCloseTo(2560, within(50.0));
    }

    @Test
    void distanciaAteLinha_pontoSobreSegmento_menorQueUmMetro() {
        var a = new Coordenada(-23.5400, -46.5800);
        var b = new Coordenada(-23.5400, -46.5600);
        var meio = new Coordenada(-23.5400, -46.5700);
        assertThat(GeoUtils.distanciaAteLinhaMetros(meio, List.of(a, b))).isLessThan(1);
    }

    @Test
    void distanciaAteLinha_pontoAoLadoDoMeio_aproximadamente111Metros() {
        var a = new Coordenada(-23.5400, -46.5800);
        var b = new Coordenada(-23.5400, -46.5600);
        var aoLado = new Coordenada(-23.5410, -46.5700);
        assertThat(GeoUtils.distanciaAteLinhaMetros(aoLado, List.of(a, b))).isCloseTo(111, within(2.0));
    }

    @Test
    void distanciaAteLinha_alemDoFimDoSegmento_usaDistanciaAteExtremidade() {
        var a = new Coordenada(-23.5400, -46.5800);
        var b = new Coordenada(-23.5400, -46.5600);
        var depois = new Coordenada(-23.5400, -46.5500);
        assertThat(GeoUtils.distanciaAteLinhaMetros(depois, List.of(a, b)))
                .isCloseTo(GeoUtils.distanciaMetros(depois, b), within(5.0));
    }

    @Test
    void distanciaAteLinha_linhaComUmPonto_usaDistanciaSimples() {
        assertThat(GeoUtils.distanciaAteLinhaMetros(PRACA_DA_SE, List.of(MASP)))
                .isCloseTo(GeoUtils.distanciaMetros(PRACA_DA_SE, MASP), within(0.001));
    }
}
