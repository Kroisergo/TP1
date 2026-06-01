package gestsaude.testes;

import java.time.LocalDateTime;
import java.util.ArrayList;

import gestsaude.recurso.Consulta;
import gestsaude.recurso.Especialidade;
import gestsaude.recurso.Utente;

public class TestaUtente {

    public static void main(String[] args) {

        System.out.println("=== TESTE 1: criar utente válido ===");
        Utente u1 = new Utente("Joao Silva", 123456789);
        System.out.println(u1);

        System.out.println("\n=== TESTE 2: alterar nome ===");
        u1.setNome("Joao Pedro Silva");
        System.out.println(u1);

        System.out.println("\n=== TESTE 3: lista de consultas começa vazia ===");
        System.out.println("Numero de consultas: " + u1.getConsultas().size());

        System.out.println("\n=== TESTE 4: tentar alterar colecao devolvida por getConsultas ===");
        try {
            u1.getConsultas().add(null);
            System.out.println("ERRO: foi possivel alterar a colecao.");
        } catch (UnsupportedOperationException e) {
            System.out.println("OK: nao foi possivel alterar a colecao devolvida.");
        }

        System.out.println("\n=== TESTE 5: addConsulta(null) ===");
        try {
            u1.addConsulta(null);
            System.out.println("ERRO: aceitou consulta null.");
        } catch (IllegalArgumentException e) {
            System.out.println("OK: lançou excecao -> " + e.getMessage());
        }

        System.out.println("\n=== TESTE 6: removeConsulta(null) ===");
        try {
            u1.removeConsulta(null);
            System.out.println("ERRO: aceitou remover consulta null.");
        } catch (IllegalArgumentException e) {
            System.out.println("OK: lançou excecao -> " + e.getMessage());
        }

        System.out.println("\n=== TESTE 7: nome invalido ===");
        try {
            Utente u2 = new Utente("   ", 123);
            System.out.println("ERRO: criou utente com nome invalido -> " + u2);
        } catch (IllegalArgumentException e) {
            System.out.println("OK: lançou excecao -> " + e.getMessage());
        }

        System.out.println("\n=== TESTE 8: SNS invalido ===");
        try {
            Utente u3 = new Utente("Maria", 0);
            System.out.println("ERRO: criou utente com SNS invalido -> " + u3);
        } catch (IllegalArgumentException e) {
            System.out.println("OK: lançou excecao -> " + e.getMessage());
        }

        System.out.println("\n=== TESTE 9: consultas ficam por ordem cronologica ===");
        Especialidade esp = new Especialidade("Ped1", "Pediatria");
        Consulta c1 = new Consulta(LocalDateTime.of(2026, 4, 22, 11, 0), u1, esp);
        Consulta c2 = new Consulta(LocalDateTime.of(2026, 4, 22, 9, 0), u1, esp);
        Consulta c3 = new Consulta(LocalDateTime.of(2026, 4, 22, 10, 0), u1, esp);

        u1.addConsulta(c1);
        u1.addConsulta(c2);
        u1.addConsulta(c3);

        ArrayList<Consulta> consultas = new ArrayList<>(u1.getConsultas());
        if (consultas.get(0) == c2 && consultas.get(1) == c3 && consultas.get(2) == c1) {
            System.out.println("OK: consultas ordenadas corretamente.");
        } else {
            System.out.println("ERRO: consultas nao ficaram ordenadas.");
        }
    }
}
