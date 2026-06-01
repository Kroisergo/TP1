package gestsaude.testes;

import java.time.LocalDateTime;

import gestsaude.recurso.Consulta;
import gestsaude.recurso.Especialidade;
import gestsaude.recurso.GEstSaude;
import gestsaude.recurso.Senha;
import gestsaude.recurso.Servico;
import gestsaude.recurso.Utente;

public class TestaSenha {

    public static void main(String[] args) {
        GEstSaude gest = new GEstSaude();
        Utente utente = new Utente("Joao Silva", 123456789);
        Especialidade especialidade = new Especialidade("Ped1", "Pediatria");
        LocalDateTime data = LocalDateTime.of(2026, 4, 22, 10, 0);
        Consulta consulta = new Consulta(data, utente, especialidade);

        System.out.println("=== TESTE 1: criar senha valida ===");
        Senha senha = new Senha(gest, "A1", data.minusMinutes(5), data, consulta);
        System.out.println(senha);

        System.out.println("\n=== TESTE 2: sem servicos, servicoAtual devolve null ===");
        if (senha.servicoAtual() == null) {
            System.out.println("OK: nao ha servico atual.");
        } else {
            System.out.println("ERRO: nao devia existir servico atual.");
        }

        System.out.println("\n=== TESTE 3: addServico(null) ===");
        try {
            senha.addServico(null);
            System.out.println("ERRO: aceitou servico null.");
        } catch (IllegalArgumentException e) {
            System.out.println("OK: lancou excecao -> " + e.getMessage());
        }

        System.out.println("\n=== TESTE 4: adicionar servicos e ver o servico atual ===");
        Servico s1 = new Servico("Rad", "Radiologia", "Sala 1");
        Servico s2 = new Servico("Enf", "Enfermaria", "Sala 2");
        senha.addServico(s1);
        senha.addServico(s2);
        if (senha.servicoAtual() == s1) {
            System.out.println("OK: primeiro servico correto.");
        } else {
            System.out.println("ERRO: servico atual incorreto.");
        }

        System.out.println("\n=== TESTE 5: nao pode repetir servico ===");
        try {
            senha.addServico(s1);
            System.out.println("ERRO: aceitou servico repetido.");
        } catch (IllegalArgumentException e) {
            System.out.println("OK: lancou excecao -> " + e.getMessage());
        }

        System.out.println("\n=== TESTE 6: colecao protegida ===");
        try {
            senha.getServicos().add(s1);
            System.out.println("ERRO: foi possivel alterar a colecao devolvida.");
        } catch (UnsupportedOperationException e) {
            System.out.println("OK: nao foi possivel alterar a colecao devolvida.");
        }

        System.out.println("\n=== TESTE 7: termina atendimento envia para o primeiro servico ===");
        senha.terminaAtendimento();
        if (s1.getProximaSenha() == senha) {
            System.out.println("OK: senha enviada para o primeiro servico.");
        } else {
            System.out.println("ERRO: senha nao foi enviada para o primeiro servico.");
        }

        System.out.println("\n=== TESTE 8: construtor com valores invalidos ===");
        try {
            Senha s3 = new Senha(null, "A2", data.minusMinutes(5), data, consulta);
            System.out.println("ERRO: criou senha com sistema invalido -> " + s3);
        } catch (IllegalArgumentException e) {
            System.out.println("OK: lancou excecao -> " + e.getMessage());
        }

        try {
            Senha s4 = new Senha(gest, "   ", data.minusMinutes(5), data, consulta);
            System.out.println("ERRO: criou senha com numero invalido -> " + s4);
        } catch (IllegalArgumentException e) {
            System.out.println("OK: lancou excecao -> " + e.getMessage());
        }

        try {
            Senha s5 = new Senha(gest, "A2", null, data, consulta);
            System.out.println("ERRO: criou senha com entrada invalida -> " + s5);
        } catch (IllegalArgumentException e) {
            System.out.println("OK: lancou excecao -> " + e.getMessage());
        }

        try {
            Senha s6 = new Senha(gest, "A2", data.minusMinutes(5), null, consulta);
            System.out.println("ERRO: criou senha com atendimento invalido -> " + s6);
        } catch (IllegalArgumentException e) {
            System.out.println("OK: lancou excecao -> " + e.getMessage());
        }

        try {
            Senha s7 = new Senha(gest, "A2", data.minusMinutes(5), data, null);
            System.out.println("ERRO: criou senha com consulta invalida -> " + s7);
        } catch (IllegalArgumentException e) {
            System.out.println("OK: lancou excecao -> " + e.getMessage());
        }
    }
}
