package com.example.bdd.steps;

import com.example.bdd.modelos.Livro;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LivroPassos {

    private Livro livro;
    private final List<Livro> livros = new ArrayList<>();
    private RuntimeException erro;

    @Dado("um livro de titulo {string} do autor {string}")
    public void um_livro(String titulo, String autor) {
        livro = new Livro(titulo, autor);
        erro = null;
    }

    @Dado("o cadastro dos livros")
    public void o_cadastro_dos_livros(DataTable dataTable) {
        livros.clear();
        for (var linha : dataTable.asMaps(String.class, String.class)) {
            livros.add(new Livro(linha.get("titulo"), linha.get("autor")));
        }
    }

    @Quando("o livro e emprestado")
    public void o_livro_e_emprestado() {
        try {
            livro.emprestar();
        } catch (RuntimeException e) {
            erro = e;
        }
    }

    @Quando("o livro e emprestado novamente")
    public void o_livro_e_emprestado_novamente() {
        o_livro_e_emprestado();
    }

    @Quando("o livro e devolvido")
    public void o_livro_e_devolvido() {
        livro.devolver();
    }

    @Entao("o livro deve estar disponivel")
    public void o_livro_deve_estar_disponivel() {
        assertTrue(livro.estaDisponivel());
    }

    @Entao("o livro nao deve estar disponivel")
    public void o_livro_nao_deve_estar_disponivel() {
        assertFalse(livro.estaDisponivel());
    }

    @Entao("todos os livros cadastrados devem estar disponiveis")
    public void todos_disponiveis() {
        assertFalse(livros.isEmpty(), "Nenhum livro foi cadastrado");
        for (Livro l : livros) {
            assertTrue(l.estaDisponivel(), "Livro indisponivel: " + l.getTitulo());
        }
    }

    @Entao("deve ocorrer o erro de livro {string}")
    public void deve_ocorrer_o_erro_de_livro(String mensagem) {
        assertNotNull(erro, "Esperava um erro, mas nenhum foi lancado");
        assertEquals(mensagem, erro.getMessage());
    }
}
