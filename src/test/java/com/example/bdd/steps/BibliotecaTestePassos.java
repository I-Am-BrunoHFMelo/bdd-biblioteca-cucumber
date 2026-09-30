package com.example.bdd.steps;

import com.example.bdd.modelos.Biblioteca;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

import static org.junit.jupiter.api.Assertions.*;

public class BibliotecaTestePassos {

    private Biblioteca biblioteca;
    private double ultimaMulta;
    private RuntimeException ultimoErro;

    @Dado("uma biblioteca com limite de {int} emprestimos por membro")
    public void uma_biblioteca_com_limite(int limite) {
        biblioteca = new Biblioteca(limite);
        ultimaMulta = 0.0;
        ultimoErro = null;
    }

    @Dado("o livro {string} disponivel no acervo")
    public void o_livro_disponivel_no_acervo(String titulo) {
        biblioteca.adicionarLivro(titulo);
    }

    @Dado("os livros disponiveis no acervo")
    public void os_livros_disponiveis_no_acervo(DataTable dataTable) {
        var livros = dataTable.asMaps(String.class, String.class);
        for (var livro : livros) {
            biblioteca.adicionarLivro(livro.get("titulo"));
        }
    }

    @Quando("o membro {string} pega emprestado o livro {string}")
    public void o_membro_pega_emprestado(String membro, String titulo) {
        try {
            biblioteca.emprestar(membro, titulo);
        } catch (RuntimeException e) {
            ultimoErro = e;
        }
    }

    @Quando("o membro {string} devolve o livro {string} com {int} dias de atraso")
    public void o_membro_devolve_com_atraso(String membro, String titulo, int diasDeAtraso) {
        try {
            ultimaMulta = biblioteca.devolver(titulo, diasDeAtraso);
        } catch (RuntimeException e) {
            ultimoErro = e;
        }
    }

    @Quando("o membro {string} devolve o livro {string} no prazo")
    public void o_membro_devolve_no_prazo(String membro, String titulo) {
        o_membro_devolve_com_atraso(membro, titulo, 0);
    }

    @Dado("o membro {string} pegou emprestado o livro {string} em {string}")
    public void o_membro_pegou_emprestado_em(String membro, String titulo, String dataRetirada) {
        try {
            biblioteca.emprestar(membro, titulo, java.time.LocalDate.parse(dataRetirada));
        } catch (RuntimeException e) {
            ultimoErro = e;
        }
    }

    @Quando("o membro {string} devolve o livro {string} na data {string}")
    public void o_membro_devolve_na_data(String membro, String titulo, String dataDevolucao) {
        try {
            ultimaMulta = biblioteca.devolver(titulo, java.time.LocalDate.parse(dataDevolucao));
        } catch (RuntimeException e) {
            ultimoErro = e;
        }
    }

    @Entao("o livro {string} deve estar disponivel")
    public void o_livro_deve_estar_disponivel(String titulo) {
        assertTrue(biblioteca.estaDisponivel(titulo));
    }

    @Entao("o livro {string} nao deve estar disponivel")
    public void o_livro_nao_deve_estar_disponivel(String titulo) {
        assertFalse(biblioteca.estaDisponivel(titulo));
    }

    @Entao("o membro {string} deve ter {int} emprestimos ativos")
    public void o_membro_deve_ter_x_emprestimos(String membro, int quantidade) {
        assertEquals(quantidade, biblioteca.emprestimosAtivosDoMembro(membro));
    }

    @Entao("a multa cobrada deve ser {string}")
    public void a_multa_cobrada_deve_ser(String valor) {
        assertEquals(Double.parseDouble(valor), ultimaMulta);
    }

    @Entao("deve ocorrer o erro {string}")
    public void deve_ocorrer_o_erro(String mensagem) {
        assertNotNull(ultimoErro, "Esperava um erro, mas nenhum foi lancado");
        assertEquals(mensagem, ultimoErro.getMessage());
    }

    @Entao("nao deve ocorrer nenhum erro")
    public void nao_deve_ocorrer_nenhum_erro() {
        assertNull(ultimoErro, "Nao esperava erro, mas ocorreu: "
                + (ultimoErro != null ? ultimoErro.getMessage() : ""));
    }
}
