package gestsaude.recurso;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import poo.tempo.HorarioDiario;
import poo.util.Validator;

/**
 * Representa uma Especialidade. Deve ter uma lista das consultas marcadas e uma
 * lista com as senhas a serem atendidas no dia atual.
 */
public class Especialidade {

	private static final int MINUTOS_ATRASO_REJEICAO = 15;

	// para simplificar, cada especialidade vai ter sempre o mesmo horario
	private HorarioDiario horario = new HorarioDiario(LocalTime.of(8, 10), LocalTime.of(19, 50));

	private String identificador;
	private String descricao;
	private ArrayList<Consulta> consultas;
	private ArrayList<Senha> senhas;

	public Especialidade(String identificador, String descricao) {
		this.identificador = checkIdentificador(identificador);
		this.descricao = checkDescricao(descricao);
		this.consultas = new ArrayList<>();
		this.senhas = new ArrayList<>();
	}

	public String getIdentificador() {
		return identificador;
	}

	public String getDescricao() {
		return descricao;
	}

	public HorarioDiario getHorario() {
		return horario;
	}

	public Collection<Consulta> getConsultas() {
		return Collections.unmodifiableCollection(consultas);
	}

	/**
	 * Retorna qual a proxima senha em espera
	 * 
	 * @return a proxima senha em espera
	 */
	public Senha getProximaSenha() {
		return senhas.isEmpty() ? null : senhas.get(0);
	}

	/**
	 * O utente nao responde a chamada? Coloca a senha/consulta para 15 minutos mais
	 * tarde e passa ao proximo utente.
	 */
	public void rejeitaProximaSenha() {
		if (senhas.isEmpty()) {
			return;
		}

		Senha senha = senhas.remove(0);
		senha.setHoraPrevistaAtendimento(senha.getHoraPrevistaAtendimento().plusMinutes(MINUTOS_ATRASO_REJEICAO));

		// FEITO: implementar corretamente quando a classe Senha suportar
		// alteracao da hora prevista de atendimento e a respetiva validacao
		// contra o horario desta especialidade.
		if (horario.contem(senha.getHoraPrevistaAtendimento().toLocalTime())) {
			addSenha(senha);
		} else {
			senha.getGest().terminaConsulta(senha.getConsulta());
		}
	}

	/**
	 * Terminou a consulta da senha
	 * 
	 * @param senha a senha cuja consulta terminou
	 */
	public void terminaConsulta(Senha senha) {
		if (senha == null) {
			throw new IllegalArgumentException("A senha nao pode ser nula.");
		}

		senhas.remove(senha);
		senha.terminaAtendimento();
	}

	public void addConsulta(Consulta consulta) {
		if (consulta == null) {
			throw new IllegalArgumentException("A consulta nao pode ser nula.");
		}

		consultas.add(consulta);
	}

	public void removeConsulta(Consulta consulta) {
		if (consulta == null) {
			throw new IllegalArgumentException("A consulta nao pode ser nula.");
		}

		consultas.remove(consulta);
	}

	public void addSenha(Senha senha) {
		if (senha == null) {
			throw new IllegalArgumentException("A senha nao pode ser nula.");
		}

		int pos = 0;
		while (pos < senhas.size()
				&& senhas.get(pos).getHoraPrevistaAtendimento().isBefore(senha.getHoraPrevistaAtendimento())) {
			pos++;
		}
		senhas.add(pos, senha);
	}

	public void removeSenha(Senha senha) {
		if (senha == null) {
			throw new IllegalArgumentException("A senha nao pode ser nula.");
		}

		senhas.remove(senha);
	}

	/**
	 * Retorna as senhas que estao em lista de espera para serem atendidas nesta
	 * especialidade
	 * 
	 * @return as senhas que estao em lista de espera para serem atendidas
	 */
	public Collection<Senha> getEmEspera() {
		return Collections.unmodifiableCollection(senhas);
	}

	@Override
	public String toString() {
		return "Especialidade [horario=" + horario + ", identificador=" + identificador + ", descricao=" + descricao
				+ ", consultas=" + consultas + ", senhas=" + senhas + "]";
	}

	private String checkIdentificador(String identificador) {
		if (identificador == null) {
			throw new IllegalArgumentException("O identificador da especialidade nao pode ser nulo.");
		}
		return Validator.requireNonBlankTrimmed(identificador);
	}

	private String checkDescricao(String descricao) {
		if (descricao == null) {
			throw new IllegalArgumentException("A descricao da especialidade nao pode ser nula.");
		}
		return Validator.requireNonBlankTrimmed(descricao);
	}
}
