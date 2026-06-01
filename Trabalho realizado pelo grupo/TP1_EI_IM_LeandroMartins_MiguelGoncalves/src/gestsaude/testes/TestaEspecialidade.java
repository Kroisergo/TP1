package gestsaude.testes;

import java.time.LocalDateTime;
import java.util.ArrayList;

import gestsaude.recurso.Consulta;
import gestsaude.recurso.Especialidade;
import gestsaude.recurso.GEstSaude;
import gestsaude.recurso.Senha;
import gestsaude.recurso.Utente;

public class TestaEspecialidade {

    public static void main(String[] args) {

        System.out.println("=== TESTE 1: criar especialidade válida ===");
        Especialidade e1 = new Especialidade("Ped1", "Pediatria");
        System.out.println(e1);

        System.out.println("\n=== TESTE 2: identificador invalido ===");
        try {
            Especialidade e2 = new Especialidade("   ", "Cardiologia");
            System.out.println("ERRO: criou especialidade com identificador invalido -> " + e2);
        } catch (IllegalArgumentException e) {
            System.out.println("OK: lançou excecao -> " + e.getMessage());
        }

        System.out.println("\n=== TESTE 3: descricao invalida ===");
        try {
            Especialidade e3 = new Especialidade("Card1", "   ");
            System.out.println("ERRO: criou especialidade com descricao invalida -> " + e3);
        } catch (IllegalArgumentException e) {
            System.out.println("OK: lançou excecao -> " + e.getMessage());
        }

        System.out.println("\n=== TESTE 4: proxima senha quando nao ha senhas ===");
        if (e1.getProximaSenha() == null) {
            System.out.println("OK: devolveu null.");
        } else {
            System.out.println("ERRO: devia devolver null.");
        }

        System.out.println("\n=== TESTE 5: addConsulta(null) ===");
        try {
            e1.addConsulta(null);
            System.out.println("ERRO: aceitou consulta null.");
        } catch (IllegalArgumentException e) {
            System.out.println("OK: lançou excecao -> " + e.getMessage());
        }

        System.out.println("\n=== TESTE 6: removeConsulta(null) ===");
        try {
            e1.removeConsulta(null);
            System.out.println("ERRO: aceitou remover consulta null.");
        } catch (IllegalArgumentException e) {
            System.out.println("OK: lançou excecao -> " + e.getMessage());
        }

        System.out.println("\n=== TESTE 7: addSenha(null) ===");
        try {
            e1.addSenha(null);
            System.out.println("ERRO: aceitou senha null.");
        } catch (IllegalArgumentException e) {
            System.out.println("OK: lançou excecao -> " + e.getMessage());
        }

        System.out.println("\n=== TESTE 8: removeSenha(null) ===");
        try {
            e1.removeSenha(null);
            System.out.println("ERRO: aceitou remover senha null.");
        } catch (IllegalArgumentException e) {
            System.out.println("OK: lançou excecao -> " + e.getMessage());
        }

        System.out.println("\n=== TESTE 9: colecao de senhas em espera protegida ===");
        try {
            e1.getEmEspera().add(null);
            System.out.println("ERRO: foi possivel alterar a colecao devolvida.");
        } catch (UnsupportedOperationException e) {
            System.out.println("OK: nao foi possivel alterar a colecao devolvida.");
        }

        System.out.println("\n=== ESTADO FINAL DA ESPECIALIDADE ===");
        System.out.println(e1);

        System.out.println("\n=== TESTE 10: senhas ficam ordenadas por hora prevista ===");
        GEstSaude gest = new GEstSaude();
        Utente u1 = new Utente("Joao Silva", 123456789);
        Utente u2 = new Utente("Maria Santos", 123456780);
        Consulta c1 = new Consulta(LocalDateTime.of(2026, 4, 22, 11, 0), u1, e1);
        Consulta c2 = new Consulta(LocalDateTime.of(2026, 4, 22, 10, 0), u2, e1);
        Senha s1 = new Senha(gest, "A1", LocalDateTime.of(2026, 4, 22, 9, 50),
                LocalDateTime.of(2026, 4, 22, 11, 0), c1);
        Senha s2 = new Senha(gest, "A2", LocalDateTime.of(2026, 4, 22, 9, 50),
                LocalDateTime.of(2026, 4, 22, 10, 0), c2);

        e1.addSenha(s1);
        e1.addSenha(s2);
        ArrayList<Senha> senhas = new ArrayList<>(e1.getEmEspera());
        if (senhas.get(0) == s2 && senhas.get(1) == s1) {
            System.out.println("OK: senhas ordenadas corretamente.");
        } else {
            System.out.println("ERRO: senhas nao ficaram ordenadas.");
        }

        System.out.println("\n=== TESTE 11: rejeitar proxima senha atrasa 15 minutos ===");
        e1.rejeitaProximaSenha();
        if (s2.getHoraPrevistaAtendimento().equals(LocalDateTime.of(2026, 4, 22, 10, 15))) {
            System.out.println("OK: senha foi atrasada 15 minutos.");
        } else {
            System.out.println("ERRO: senha nao foi atrasada corretamente.");
        }
    }
}
