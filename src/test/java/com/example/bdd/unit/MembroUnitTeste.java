package com.example.bdd.unit;

import com.example.bdd.modelos.Emprestimo;
import com.example.bdd.modelos.Livro;
import com.example.bdd.modelos.Membro;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MembroUnitTeste {

    private Emprestimo umEmprestimo(Membro membro, String titulo) {
        LocalDate retirada = LocalDate.of(2025, 1, 1);
        return new Emprestimo(membro, new Livro(titulo, "Autor"),
                retirada, retirada.plusDays(7));
    }

    @Test
    @DisplayName("Membro novo pode pegar emprestado")
    void membroNovoPodePegarEmprestado() {
        Membro membro = new Membro("Bruno", 2);

        assertTrue(membro.podePegarEmprestado());
        assertEquals(0, membro.getQuantidadeDeEmprestimosAtivos());
    }

    @Test
    @DisplayName("Adicionar emprestimos incrementa a contagem ativa")
    void adicionarEmprestimosContabiliza() {
        Membro membro = new Membro("Bruno", 2);

        membro.adicionarEmprestimo(umEmprestimo(membro, "Duna"));

        assertEquals(1, membro.getQuantidadeDeEmprestimosAtivos());
        assertTrue(membro.podePegarEmprestado());
    }

    @Test
    @DisplayName("Membro nao pode ultrapassar o limite")
    void naoUltrapassaLimite() {
        Membro membro = new Membro("Bruno", 2);
        membro.adicionarEmprestimo(umEmprestimo(membro, "Duna"));
        membro.adicionarEmprestimo(umEmprestimo(membro, "Clean Code"));

        assertFalse(membro.podePegarEmprestado());
        RuntimeException erro = assertThrows(RuntimeException.class,
                () -> membro.adicionarEmprestimo(umEmprestimo(membro, "Extra")));
        assertEquals("Membro atingiu o limite de emprestimos", erro.getMessage());
    }

    @Test
    @DisplayName("Remover emprestimo libera espaco no limite")
    void removerLiberaEspaco() {
        Membro membro = new Membro("Bruno", 1);
        Emprestimo emprestimo = umEmprestimo(membro, "Duna");
        membro.adicionarEmprestimo(emprestimo);

        membro.removerEmprestimo(emprestimo);

        assertEquals(0, membro.getQuantidadeDeEmprestimosAtivos());
        assertTrue(membro.podePegarEmprestado());
    }
}
