package gestsaude.recurso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import poo.util.Validator;

/**
 * Representa um servico. Deve ter o id, uma descricao, a sala. Deve ter uma
 * lista das senhas que ainda terao de ser atendidas.
 */
public class Servico {

    private String id;
    private String descricao;
    private String sala;
    private ArrayList<Senha> senhas;

    public Servico(String id, String descricao, String sala) {
        this.id = checkId(id);
        this.descricao = checkDescricao(descricao);
        this.sala = checkSala(sala);
        this.senhas = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getSala() {
        return sala;
    }

    public void addSenha(Senha senha) {
        if (senha == null) {
            throw new IllegalArgumentException("A senha nao pode ser nula.");
        }

        senhas.add(senha);
    }

    public void removeSenha(Senha senha) {
        if (senha == null) {
            throw new IllegalArgumentException("A senha nao pode ser nula.");
        }

        senhas.remove(senha);
    }

    /**
     * Retorna a proxima senha a ser atendida por este servico
     * 
     * @return a proxima senha a ser atendida por este servico, null se nao tiver
     *         mais senhas
     */
    public Senha getProximaSenha() {
        return senhas.isEmpty() ? null : senhas.get(0);
    }

    /**
     * o utente nao responde a chamada? A sua senha passa para o fim da lista.
     */
    public void saltaProximaSenha() {
        if (!senhas.isEmpty()) {
            senhas.add(senhas.remove(0));
        }
    }

    /**
     * a senha termina a consulta neste servico
     * 
     * @param s a senha que terminou o servico
     */
    public void terminaConsulta(Senha s) {
        if (s == null) {
            throw new IllegalArgumentException("A senha nao pode ser nula.");
        }
        senhas.remove(s);
        s.terminaAtendimento();
    }

    /**
     * Retorna as senhas que estao em lista de espera para serem atendidas neste
     * servico
     * 
     * @return as senhas que estao em lista de espera para serem atendidas
     */
    public Collection<Senha> getEmEspera() {
        return Collections.unmodifiableCollection(senhas);
    }

    @Override
    public String toString() {
        return "Servico [id=" + id + ", descricao=" + descricao + ", sala=" + sala + ", senhas=" + senhas + "]";
    }

    private String checkId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("O id do servico nao pode ser nulo.");
        }
        return Validator.requireNonBlankTrimmed(id);
    }

    private String checkDescricao(String descricao) {
        if (descricao == null) {
            throw new IllegalArgumentException("A descricao do servico nao pode ser nula.");
        }
        return Validator.requireNonBlankTrimmed(descricao);
    }

    private String checkSala(String sala) {
        if (sala == null) {
            throw new IllegalArgumentException("A sala do servico nao pode ser nula.");
        }
        return Validator.requireNonBlankTrimmed(sala);
    }
}
