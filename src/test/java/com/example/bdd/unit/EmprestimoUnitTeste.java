package com.example.bdd.unit;

import com.example.bdd.modelos.Emprestimo;
import com.example.bdd.modelos.Livro;
import com.example.bdd.modelos.Membro;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EmprestimoUnitTeste {

    private static final LocalDate RETIRADA = LocalDate.of(2025, 1, 1);
    private static final LocalDate PREVISTA = LocalDate.of(2025, 1, 8);

    private Emprestimo novoEmprestimo() {
        Membro membro = new Membro("Bruno", 2);
        Livro livro = new Livro("Duna", "Frank Herbert");
        return new Emprestimo(membro, livro, RETIRADA, PREVISTA);
    }

    @Test
    @DisplayName("Devolver na data prevista nao gera multa")
    void noPrazoSemMulta() {
        assertEquals(0.0, novoEmprestimo().calcularMulta(PREVISTA));
    }

    @Test
    @DisplayName("Devolver antes do prazo nao gera multa")
    void adiantadoSemMulta() {
        assertEquals(0.0, novoEmprestimo().calcularMulta(PREVISTA.minusDays(3)));
    }

    @Test
    @DisplayName("A funcao conta sozinha os dias entre a data prevista e a real")
    void contaDiasEntreDatas() {
        double multa = novoEmprestimo().calcularMulta(LocalDate.of(2025, 1, 11));

        assertEquals(6.0, multa);
    }

    @ParameterizedTest(name = "atraso de {0} dia(s) => multa {1}")
    @CsvSource({
            "0, 0.0",
            "1, 2.0",
            "5, 10.0",
            "10, 20.0"
    })
    @DisplayName("Multa cresce R$ 2,00 por dia de atraso, calculado pelas datas")
    void multaProporcionalPorDatas(int diasDeAtraso, double multaEsperada) {
        LocalDate dataDevolucao = PREVISTA.plusDays(diasDeAtraso);

        assertEquals(multaEsperada, novoEmprestimo().calcularMulta(dataDevolucao));
    }

    @Test
    @DisplayName("Emprestimo mantem referencia ao membro, livro e datas")
    void guardaDados() {
        Emprestimo emprestimo = novoEmprestimo();

        assertEquals("Bruno", emprestimo.getMembro().getNome());
        assertEquals("Duna", emprestimo.getLivro().getTitulo());
        assertEquals(RETIRADA, emprestimo.getDataRetirada());
        assertEquals(PREVISTA, emprestimo.getDataPrevistaDevolucao());
    }
}
