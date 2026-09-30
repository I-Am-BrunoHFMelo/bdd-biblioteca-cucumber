package com.example.bdd.steps;

import com.example.bdd.modelos.Emprestimo;
import com.example.bdd.modelos.Livro;
import com.example.bdd.modelos.Membro;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class MembroPassos {

    private Membro membro;
    private RuntimeException erro;
    private Emprestimo ultimoEmprestimo;

    private Emprestimo umEmprestimo(String titulo) {
        LocalDate retirada = LocalDate.of(2025, 1, 1);
        return new Emprestimo(membro, new Livro(titulo, "Autor"),
                retirada, retirada.plusDays(7));
    }

    @Dado("um membro chamado {string} com limite de {int} emprestimos")
    public void um_membro_com_limite(String nome, int limite) {
        membro = new Membro(nome, limite);
        erro = null;
    }

    @Quando("o membro recebe {int} emprestimos")
    public void o_membro_recebe_emprestimos(int quantidade) {
        for (int i = 0; i < quantidade; i++) {
            ultimoEmprestimo = umEmprestimo("Livro " + i);
            membro.adicionarEmprestimo(ultimoEmprestimo);
        }
    }

    @Quando("o membro devolve {int} emprestimos")
    public void o_membro_devolve_emprestimos(int quantidade) {
        for (int i = 0; i < quantidade; i++) {
            membro.removerEmprestimo(ultimoEmprestimo);
        }
    }

    @Quando("o membro tenta receber os emprestimos")
    public void o_membro_tenta_receber(DataTable dataTable) {
        try {
            for (var linha : dataTable.asMaps(String.class, String.class)) {
                membro.adicionarEmprestimo(umEmprestimo(linha.get("titulo")));
            }
        } catch (RuntimeException e) {
            erro = e;
        }
    }

    @Entao("o membro pode pegar emprestado")
    public void o_membro_pode_pegar_emprestado() {
        assertTrue(membro.podePegarEmprestado());
    }

    @Entao("o membro pode pegar emprestado deve ser {word}")
    public void o_membro_pode_pegar_emprestado_deve_ser(String esperado) {
        assertEquals(Boolean.parseBoolean(esperado), membro.podePegarEmprestado());
    }

    @Entao("o membro tem {int} emprestimos ativos")
    public void o_membro_tem_emprestimos_ativos(int quantidade) {
        assertEquals(quantidade, membro.getQuantidadeDeEmprestimosAtivos());
    }

    @Entao("deve ocorrer o erro de membro {string}")
    public void deve_ocorrer_o_erro_de_membro(String mensagem) {
        assertNotNull(erro, "Esperava um erro, mas nenhum foi lancado");
        assertEquals(mensagem, erro.getMessage());
    }
}
