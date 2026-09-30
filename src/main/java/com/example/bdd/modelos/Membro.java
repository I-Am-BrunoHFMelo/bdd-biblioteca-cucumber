package com.example.bdd.modelos;

import java.util.ArrayList;
import java.util.List;

public class Membro {

    private final String nome;
    private final int limiteDeEmprestimos;
    private final List<Emprestimo> emprestimosAtivos;

    public Membro(String nome, int limiteDeEmprestimos) {
        this.nome = nome;
        this.limiteDeEmprestimos = limiteDeEmprestimos;
        this.emprestimosAtivos = new ArrayList<>();
    }

    public boolean podePegarEmprestado() {
        return emprestimosAtivos.size() < limiteDeEmprestimos;
    }

    public void adicionarEmprestimo(Emprestimo emprestimo) {
        if (!podePegarEmprestado()) {
            throw new RuntimeException("Membro atingiu o limite de emprestimos");
        }
        emprestimosAtivos.add(emprestimo);
    }

    public void removerEmprestimo(Emprestimo emprestimo) {
        emprestimosAtivos.remove(emprestimo);
    }

    public int getQuantidadeDeEmprestimosAtivos() {
        return emprestimosAtivos.size();
    }

    public String getNome() {
        return nome;
    }

    public int getLimiteDeEmprestimos() {
        return limiteDeEmprestimos;
    }
}
