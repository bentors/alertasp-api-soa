package br.com.fiap.alertasp.shared;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RegraRiscoTest {

    @Test
    void nivelAgua_distanciaMaiorQueAltura_retornaZero() {
        assertThat(RegraRisco.nivelAgua(200, 250)).isZero();
    }

    @Test
    void nivelAgua_subtraiDistanciaDaAltura() {
        assertThat(RegraRisco.nivelAgua(200, 150)).isEqualTo(50);
    }

    @Test
    void nivelPorMedicao_noLimite_alto() {
        assertThat(RegraRisco.nivelPorMedicao(30, 30)).isEqualTo(NivelRisco.ALTO);
    }

    @Test
    void nivelPorMedicao_metadeDoLimite_medio() {
        assertThat(RegraRisco.nivelPorMedicao(15, 30)).isEqualTo(NivelRisco.MEDIO);
    }

    @Test
    void nivelPorMedicao_abaixoDaMetade_baixo() {
        assertThat(RegraRisco.nivelPorMedicao(14.9, 30)).isEqualTo(NivelRisco.BAIXO);
    }

    @Test
    void geraAlerta_apenasAltoECritico() {
        assertThat(NivelRisco.MEDIO.geraAlerta()).isFalse();
        assertThat(NivelRisco.ALTO.geraAlerta()).isTrue();
        assertThat(NivelRisco.CRITICO.geraAlerta()).isTrue();
    }
}
