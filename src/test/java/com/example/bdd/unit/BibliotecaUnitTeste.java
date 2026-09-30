package com.example.bdd.unit;

import com.example.bdd.modelos.Biblioteca;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BibliotecaUnitTeste {

    private Biblioteca biblioteca;

    @BeforeEach
    void prepararBiblioteca() {
        biblioteca = new Biblioteca(2);
        biblioteca.adicionarLivro("Clean Code");
        biblioteca.adicionarLivro("O Senhor dos Aneis");
        biblioteca.adicionarLivro("Duna");
    }

    @Test
    @DisplayName("Emprestar um livro disponivel deixa-o indisponivel")
    void emprestarLivroDisponivel() {
        biblioteca.emprestar("Bruno", "Clean Code");

        assertFalse(biblioteca.estaDisponivel("Clean Code"));
        assertEquals(1, biblioteca.emprestimosAtivosDoMembro("Bruno"));
        assertEquals(1, biblioteca.getQuantidadeDeEmprestimosAtivos());
    }

    @Test
    @DisplayName("Nao pode emprestar um livro que ja esta emprestado")
    void naoEmprestaLivroJaEmprestado() {
        biblioteca.emprestar("Bruno", "Clean Code");

        RuntimeException erro = assertThrows(RuntimeException.class,
                () -> biblioteca.emprestar("Ana", "Clean Code"));
        assertEquals("Livro indisponivel", erro.getMessage());
    }

    @Test
    @DisplayName("Nao pode emprestar um livro que nao existe no acervo")
    void naoEmprestaLivroInexistente() {
        RuntimeException erro = assertThrows(RuntimeException.class,
                () -> biblioteca.emprestar("Bruno", "Livro Fantasma"));
        assertEquals("Livro indisponivel", erro.getMessage());
    }

    @Test
    @DisplayName("Membro nao pode ultrapassar o limite de emprestimos ativos")
    void respeitaLimiteDeEmprestimos() {
        biblioteca.emprestar("Bruno", "Clean Code");
        biblioteca.emprestar("Bruno", "O Senhor dos Aneis");

        RuntimeException erro = assertThrows(RuntimeException.class,
                () -> biblioteca.emprestar("Bruno", "Duna"));
        assertEquals("Membro atingiu o limite de emprestimos", erro.getMessage());
        assertEquals(2, biblioteca.emprestimosAtivosDoMembro("Bruno"));
    }

    @Test
    @DisplayName("Devolver um livro deixa-o disponivel novamente")
    void devolverLiberaLivro() {
        biblioteca.emprestar("Bruno", "Duna");
        biblioteca.devolver("Duna", 0);

        assertTrue(biblioteca.estaDisponivel("Duna"));
        assertEquals(0, biblioteca.emprestimosAtivosDoMembro("Bruno"));
    }

    @Test
    @DisplayName("Nao pode devolver um livro que nao esta emprestado")
    void naoDevolveLivroNaoEmprestado() {
        RuntimeException erro = assertThrows(RuntimeException.class,
                () -> biblioteca.devolver("Duna", 0));
        assertEquals("Livro nao esta emprestado", erro.getMessage());
    }

    @Test
    @DisplayName("Devolver no prazo nao gera multa")
    void devolverNoPrazoSemMulta() {
        biblioteca.emprestar("Bruno", "Duna");

        double multa = biblioteca.devolver("Duna", 0);

        assertEquals(0.0, multa);
    }

    @Test
    @DisplayName("Devolver em atraso gera multa proporcional aos dias")
    void devolverEmAtrasoGeraMulta() {
        biblioteca.emprestar("Bruno", "Duna");

        double multa = biblioteca.devolver("Duna", 3);

        assertEquals(6.0, multa);
    }

    @ParameterizedTest(name = "{0} dia(s) de atraso => multa {1}")
    @CsvSource({
            "0, 0.0",
            "1, 2.0",
            "5, 10.0",
            "10, 20.0"
    })
    @DisplayName("Calculo da multa cresce conforme os dias de atraso")
    void calculoDaMulta(int dias, double multaEsperada) {
        biblioteca.emprestar("Bruno", "Duna");

        assertEquals(multaEsperada, biblioteca.devolver("Duna", dias));
    }

    @Test
    @DisplayName("Devolver por data real: a biblioteca conta os dias sozinha")
    void devolverPorDataRealCalculaDias() {
        biblioteca.emprestar("Bruno", "Duna", LocalDate.of(2025, 1, 1));

        double multa = biblioteca.devolver("Duna", LocalDate.of(2025, 1, 12));

        assertEquals(8.0, multa);
    }

    @Test
    @DisplayName("Devolver adiantado nao gera multa")
    void devolverAdiantadoSemMulta() {
        biblioteca.emprestar("Bruno", "Duna", LocalDate.of(2025, 1, 1));

        double multa = biblioteca.devolver("Duna", LocalDate.of(2025, 1, 5));

        assertEquals(0.0, multa);
    }

    @Test
    @DisplayName("Apos devolver, o membro pode pegar outro livro emprestado")
    void devolverPermiteNovoEmprestimo() {
        biblioteca.emprestar("Bruno", "Clean Code");
        biblioteca.emprestar("Bruno", "O Senhor dos Aneis");
        biblioteca.devolver("Clean Code", 0);

        assertDoesNotThrow(() -> biblioteca.emprestar("Bruno", "Duna"));
        assertEquals(2, biblioteca.emprestimosAtivosDoMembro("Bruno"));
    }
}
