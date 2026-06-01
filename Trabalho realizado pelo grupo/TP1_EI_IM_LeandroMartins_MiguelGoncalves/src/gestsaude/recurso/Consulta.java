package gestsaude.recurso;

import java.time.LocalDateTime;

/**
 * Representa uma Consulta. Deve ter o dia e hora da marcacao, o utente e a
 * especialidade.
 */
public class Consulta {

	private LocalDateTime dataMarcacao;
	private Utente utente;
	private Especialidade especialidade;
	private Senha senha;

	public Consulta(LocalDateTime dataMarcacao, Utente utente, Especialidade especialidade) {
		this.dataMarcacao = checkDataMarcacao(dataMarcacao);
		this.utente = checkUtente(utente);
		this.especialidade = checkEspecialidade(especialidade);
	}

	public Consulta() {
	}

	public LocalDateTime getDataMarcacao() {
		return dataMarcacao;
	}

	public void setDataMarcacao(LocalDateTime dataMarcacao) {
		this.dataMarcacao = checkDataMarcacao(dataMarcacao);
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = checkUtente(utente);
	}

	public Especialidade getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(Especialidade especialidade) {
		this.especialidade = checkEspecialidade(especialidade);
	}

	public Senha getSenha() {
		return senha;
	}

	public void setSenha(Senha senha) {
		this.senha = senha;
	}

	/**
	 * Indica se a consulta ja esta validada
	 * 
	 * @return true, se a consulta ja esta validada
	 */
	public boolean estaValidada() {
		return senha != null;
	}

	@Override
	public String toString() {
		return "Consulta [dataMarcacao=" + dataMarcacao + ", utente=" + utente + ", especialidade=" + especialidade
				+ ", senha=" + senha + "]";
	}

	private LocalDateTime checkDataMarcacao(LocalDateTime dataMarcacao) {
		if (dataMarcacao == null) {
			throw new IllegalArgumentException("A data e hora da consulta nao pode ser nula.");
		}
		return dataMarcacao;
	}

	private Utente checkUtente(Utente utente) {
		if (utente == null) {
			throw new IllegalArgumentException("O utente da consulta nao pode ser nulo.");
		}
		return utente;
	}

	private Especialidade checkEspecialidade(Especialidade especialidade) {
		if (especialidade == null) {
			throw new IllegalArgumentException("A especialidade da consulta nao pode ser nula.");
		}
		return especialidade;
	}
}
