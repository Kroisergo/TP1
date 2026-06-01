package gestsaude.testes;

import java.time.LocalDateTime;

import gestsaude.recurso.Consulta;
import gestsaude.recurso.Especialidade;
import gestsaude.recurso.GEstSaude;
import gestsaude.recurso.Senha;
import gestsaude.recurso.Utente;

public class TestaConsulta {

    public static void main(String[] args) {
        Utente u1 = new Utente("Joao Silva", 123456789);
        Especialidade e1 = new Especialidade("Ped1", "Pediatria");
        LocalDateTime data = LocalDateTime.of(2026, 4, 22, 10, 0);

        System.out.println("=== TESTE 1: criar consulta valida ===");
        Consulta c1 = new Consulta(data, u1, e1);
        System.out.println(c1);

        System.out.println("\n=== TESTE 2: consulta comeca por validar ===");
        if (!c1.estaValidada()) {
            System.out.println("OK: consulta ainda nao esta validada.");
        } else {
            System.out.println("ERRO: consulta nao devia estar validada.");
        }

        System.out.println("\n=== TESTE 3: setSenha valida a consulta ===");
        GEstSaude gest = new GEstSaude();
        Senha s1 = new Senha(gest, "A1", data.minusMinutes(5), data, c1);
        c1.setSenha(s1);
        if (c1.estaValidada()) {
            System.out.println("OK: consulta ficou validada.");
        } else {
            System.out.println("ERRO: consulta devia estar validada.");
        }

        System.out.println("\n=== TESTE 4: data invalida ===");
        try {
            Consulta c2 = new Consulta(null, u1, e1);
            System.out.println("ERRO: criou consulta com data invalida -> " + c2);
        } catch (IllegalArgumentException e) {
            System.out.println("OK: lancou excecao -> " + e.getMessage());
        }

        System.out.println("\n=== TESTE 5: utente invalido ===");
        try {
            Consulta c3 = new Consulta(data, null, e1);
            System.out.println("ERRO: criou consulta com utente invalido -> " + c3);
        } catch (IllegalArgumentException e) {
            System.out.println("OK: lancou excecao -> " + e.getMessage());
        }

        System.out.println("\n=== TESTE 6: especialidade invalida ===");
        try {
            Consulta c4 = new Consulta(data, u1, null);
            System.out.println("ERRO: criou consulta com especialidade invalida -> " + c4);
        } catch (IllegalArgumentException e) {
            System.out.println("OK: lancou excecao -> " + e.getMessage());
        }
    }
}
