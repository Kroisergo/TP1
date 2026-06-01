package gestsaude.testes;

import java.time.LocalDateTime;

import gestsaude.recurso.Consulta;
import gestsaude.recurso.Especialidade;
import gestsaude.recurso.GEstSaude;
import gestsaude.recurso.Senha;
import gestsaude.recurso.Servico;
import gestsaude.recurso.Utente;

public class TestaServico {

    public static void main(String[] args) {
        System.out.println("=== TESTE 1: criar servico valido ===");
        Servico servico = new Servico("Rad", "Radiologia", "Sala 1");
        System.out.println(servico);

        System.out.println("\n=== TESTE 2: dados invalidos ===");
        try {
            Servico s1 = new Servico("   ", "Radiologia", "Sala 1");
            System.out.println("ERRO: criou servico com id invalido -> " + s1);
        } catch (IllegalArgumentException e) {
            System.out.println("OK: lancou excecao -> " + e.getMessage());
        }

        try {
            Servico s2 = new Servico("Rad", "   ", "Sala 1");
            System.out.println("ERRO: criou servico com descricao invalida -> " + s2);
        } catch (IllegalArgumentException e) {
            System.out.println("OK: lancou excecao -> " + e.getMessage());
        }

        try {
            Servico s3 = new Servico("Rad", "Radiologia", "   ");
            System.out.println("ERRO: criou servico com sala invalida -> " + s3);
        } catch (IllegalArgumentException e) {
            System.out.println("OK: lancou excecao -> " + e.getMessage());
        }

        System.out.println("\n=== TESTE 3: proxima senha quando nao ha senhas ===");
        if (servico.getProximaSenha() == null) {
            System.out.println("OK: devolveu null.");
        } else {
            System.out.println("ERRO: devia devolver null.");
        }

        System.out.println("\n=== TESTE 4: addSenha(null) ===");
        try {
            servico.addSenha(null);
            System.out.println("ERRO: aceitou senha null.");
        } catch (IllegalArgumentException e) {
            System.out.println("OK: lancou excecao -> " + e.getMessage());
        }

        System.out.println("\n=== TESTE 5: removeSenha(null) ===");
        try {
            servico.removeSenha(null);
            System.out.println("ERRO: aceitou remover senha null.");
        } catch (IllegalArgumentException e) {
            System.out.println("OK: lancou excecao -> " + e.getMessage());
        }

        System.out.println("\n=== TESTE 6: fila de senhas ===");
        GEstSaude gest = new GEstSaude();
        Especialidade esp = new Especialidade("Ped1", "Pediatria");
        Utente u1 = new Utente("Joao Silva", 123456789);
        Utente u2 = new Utente("Maria Santos", 123456780);
        LocalDateTime data = LocalDateTime.of(2026, 4, 22, 10, 0);
        Consulta c1 = new Consulta(data, u1, esp);
        Consulta c2 = new Consulta(data.plusMinutes(10), u2, esp);
        Senha senha1 = new Senha(gest, "A1", data.minusMinutes(5), data, c1);
        Senha senha2 = new Senha(gest, "A2", data.minusMinutes(4), data.plusMinutes(10), c2);

        servico.addSenha(senha1);
        servico.addSenha(senha2);
        if (servico.getProximaSenha() == senha1) {
            System.out.println("OK: primeira senha correta.");
        } else {
            System.out.println("ERRO: primeira senha incorreta.");
        }

        System.out.println("\n=== TESTE 7: saltar proxima senha ===");
        servico.saltaProximaSenha();
        if (servico.getProximaSenha() == senha2) {
            System.out.println("OK: passou a senha para o fim.");
        } else {
            System.out.println("ERRO: devia chamar a segunda senha.");
        }

        System.out.println("\n=== TESTE 8: colecao protegida ===");
        try {
            servico.getEmEspera().add(senha1);
            System.out.println("ERRO: foi possivel alterar a colecao devolvida.");
        } catch (UnsupportedOperationException e) {
            System.out.println("OK: nao foi possivel alterar a colecao devolvida.");
        }
    }
}
