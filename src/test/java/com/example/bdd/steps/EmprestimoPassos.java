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

public class EmprestimoPassos {

    private Emprestimo emprestimo;
    private double multa;

    @Dado("um emprestimo com retirada em {string} e prazo de {int} dias")
    public void um_emprestimo(String dataRetirada, int prazo) {
        LocalDate retirada = LocalDate.parse(dataRetirada);
        Membro membro = new Membro("Bruno", 2);
        Livro livro = new Livro("Duna", "Frank Herbert");
        emprestimo = new Emprestimo(membro, livro, retirada, retirada.plusDays(prazo));
    }

    @Quando("o livro e devolvido em {string}")
    public void o_livro_e_devolvido_em(String dataDevolucao) {
        multa = emprestimo.calcularMulta(LocalDate.parse(dataDevolucao));
    }

    @Entao("a multa do emprestimo deve ser {string}")
    public void a_multa_do_emprestimo_deve_ser(String valor) {
        assertEquals(Double.parseDouble(valor), multa);
    }

    @Entao("as devolucoes geram as multas esperadas")
    public void as_devolucoes_geram_as_multas(DataTable dataTable) {
        for (var linha : dataTable.asMaps(String.class, String.class)) {
            LocalDate dataDevolucao = LocalDate.parse(linha.get("dataDevolucao"));
            double esperada = Double.parseDouble(linha.get("multa"));
            assertEquals(esperada, emprestimo.calcularMulta(dataDevolucao),
                    "Multa incorreta para devolucao em " + dataDevolucao);
        }
    }
}
