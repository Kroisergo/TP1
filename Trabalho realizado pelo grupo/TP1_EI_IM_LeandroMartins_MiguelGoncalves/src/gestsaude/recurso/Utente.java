package gestsaude.recurso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import poo.util.Validator;

/**
 * Representa um Utente. Deve ter um nome, o número de SNS e armazenar as
 * consultas que tem marcadas no futuro.
 */
public class Utente {

    private String nome;
    private int nrSns;
    private ArrayList<Consulta> consultas;

    public Utente(String nome, int nrSns) {
        this.nome = checkNome(nome);
        this.nrSns = checkNrSns(nrSns);
        this.consultas = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = checkNome(nome);
    }

    public int getNrSns() {
        return nrSns;
    }

    public Collection<Consulta> getConsultas() {
        return Collections.unmodifiableCollection(consultas);
    }

    public void addConsulta(Consulta consulta) {
        if (consulta == null) {
            throw new IllegalArgumentException("A consulta não pode ser nula.");
        }

        int pos = 0;
        while (pos < consultas.size()
                && consultas.get(pos).getDataMarcacao().isBefore(consulta.getDataMarcacao())) {
            pos++;
        }
        consultas.add(pos, consulta);

        // FEITO: Quando a classe Consulta tiver a data/hora disponível, garantir a ordem cronológica.
        
    }

    public void removeConsulta(Consulta consulta) {
        if (consulta == null) {
            throw new IllegalArgumentException("A consulta não pode ser nula.");
        }

        consultas.remove(consulta);
    }

    @Override
    public String toString() {
        return "Utente [nome=" + nome + ", nrSns=" + nrSns + ", consultas=" + consultas + "]";
    }

    private String checkNome(String nome) {
        if (nome == null) {
            throw new IllegalArgumentException("O nome do utente nao pode ser nulo.");
        }
        return Validator.requireNonBlankTrimmed(nome);
    }

    private int checkNrSns(int nrSns) {
        return Validator.requirePositive(nrSns);
    }
}
