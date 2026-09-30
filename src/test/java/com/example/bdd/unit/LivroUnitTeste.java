package com.example.bdd.unit;

import com.example.bdd.modelos.Livro;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LivroUnitTeste {

    @Test
    @DisplayName("Livro novo comeca disponivel")
    void livroNovoEstaDisponivel() {
        Livro livro = new Livro("Clean Code", "Robert C. Martin");

        assertTrue(livro.estaDisponivel());
        assertFalse(livro.isEmprestado());
    }

    @Test
    @DisplayName("Emprestar torna o livro indisponivel")
    void emprestarTornaIndisponivel() {
        Livro livro = new Livro("Duna", "Frank Herbert");

        livro.emprestar();

        assertFalse(livro.estaDisponivel());
    }

    @Test
    @DisplayName("Nao pode emprestar um livro ja emprestado")
    void naoEmprestaDuasVezes() {
        Livro livro = new Livro("Duna", "Frank Herbert");
        livro.emprestar();

        RuntimeException erro = assertThrows(RuntimeException.class, livro::emprestar);
        assertEquals("Livro indisponivel", erro.getMessage());
    }

    @Test
    @DisplayName("Devolver torna o livro disponivel novamente")
    void devolverTornaDisponivel() {
        Livro livro = new Livro("Duna", "Frank Herbert");
        livro.emprestar();

        livro.devolver();

        assertTrue(livro.estaDisponivel());
    }
}
