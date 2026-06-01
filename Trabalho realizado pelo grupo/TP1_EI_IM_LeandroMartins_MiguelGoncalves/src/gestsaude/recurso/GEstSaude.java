package gestsaude.recurso;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import poo.util.RelogioSimulado;

/**
 * Representa o sistema. Deve armazenar todas as consulta, utentes,
 * especialidades, serviços e senhas
 */
public class GEstSaude {

	// constantes para erros e situações que podem ocorrer para usar no retorno de
	// alguns métodos
	public static final int CONSULTA_ACEITE = 0;
	public static final int CONSULTA_JA_VALIDADA = CONSULTA_ACEITE + 1;
	public static final int UTENTE_SEM_CONSULTA_HOJE = CONSULTA_JA_VALIDADA + 1;
	public static final int UTENTE_SEM_CONSULTA_PROXIMA = UTENTE_SEM_CONSULTA_HOJE + 1;
	public static final int UTENTE_DEMASIADO_ATRASADO = UTENTE_SEM_CONSULTA_PROXIMA + 1;
	public static final int UTENTE_ATRASADO_FORAHORAS = UTENTE_DEMASIADO_ATRASADO + 1;
	public static final int UTENTE_ATRASADO_ADIADO = UTENTE_ATRASADO_FORAHORAS + 1;
	public static final int SERVICO_SEM_CONSULTA = UTENTE_ATRASADO_ADIADO + 1;
	public static final int DATA_JA_PASSOU = SERVICO_SEM_CONSULTA + 1;
	public static final int UTENTE_JA_TEM_CONSULTA = DATA_JA_PASSOU + 1;
	public static final int ESPECIALISTA_JA_TEM_CONSULTA = UTENTE_JA_TEM_CONSULTA + 1;
	public static final int ALTERACAO_INVALIDA = ESPECIALISTA_JA_TEM_CONSULTA + 1;

	private static final int MINUTOS_ENTRE_CONSULTAS_UTENTE = 180;
	private static final int MINUTOS_ENTRE_CONSULTAS_ESPECIALIDADE = 10;
	private static final int MINUTOS_ATRASO_ATENDIMENTO = 5;
	private static final int HORAS_ANTECEDENCIA_VALIDACAO = 3;
	private static final int HORAS_ATRASO_MAXIMO = 2;

	private ArrayList<Consulta> consultas;
	private ArrayList<Utente> utentes;
	private ArrayList<Especialidade> especialidades;
	private ArrayList<Servico> servicos;
	private ArrayList<Senha> senhas;

	public GEstSaude() {

		this.consultas = new ArrayList<>();
		this.utentes = new ArrayList<>();
		this.especialidades = new ArrayList<>();
		this.servicos = new ArrayList<>();
		this.senhas = new ArrayList<>();
	}

	public Collection<Consulta> getConsultas() {
		return Collections.unmodifiableCollection(consultas);
	}

	public Collection<Utente> getUtentes() {
		return Collections.unmodifiableCollection(utentes);
	}

	public Collection<Especialidade> getEspecialidades() {
		return Collections.unmodifiableCollection(especialidades);
	}

	public Collection<Servico> getServicos() {
		return Collections.unmodifiableCollection(servicos);
	}

	public Collection<Senha> getSenhas() {
		return Collections.unmodifiableCollection(senhas);
	}

	public Utente getUtente(int nrSns) {
		for (Utente u : utentes) {
			if (u.getNrSns() == nrSns) {
				return u;
			}
		}
		return null;
	}

	public Especialidade getEspecialidade(String identificador) {
		if (identificador == null) {
			throw new IllegalArgumentException("O identificador nao pode ser nulo.");
		}

		for (Especialidade e : especialidades) {
			if (e.getIdentificador().equals(identificador)) {
				return e;
			}
		}
		return null;
	}

	public Servico getServico(String id) {
		if (id == null) {
			throw new IllegalArgumentException("O id nao pode ser nulo.");
		}

		for (Servico s : servicos) {
			if (s.getId().equals(id)) {
				return s;
			}
		}
		return null;
	}

	public void addUtente(Utente utente) {
		if (utente == null) {
			throw new IllegalArgumentException("O utente nao pode ser nulo.");
		}
		if (!utentes.contains(utente)) {
			utentes.add(utente);
		}
	}

	public void removeUtente(Utente utente) {
		if (utente == null) {
			throw new IllegalArgumentException("O utente nao pode ser nulo.");
		}
		utentes.remove(utente);
	}

	public void addEspecialidade(Especialidade especialidade) {
		if (especialidade == null) {
			throw new IllegalArgumentException("A especialidade nao pode ser nula.");
		}
		if (!especialidades.contains(especialidade)) {
			especialidades.add(especialidade);
		}
	}

	public void removeEspecialidade(Especialidade especialidade) {
		if (especialidade == null) {
			throw new IllegalArgumentException("A especialidade nao pode ser nula.");
		}
		especialidades.remove(especialidade);
	}

	public void addServico(Servico servico) {
		if (servico == null) {
			throw new IllegalArgumentException("O servico nao pode ser nulo.");
		}
		if (!servicos.contains(servico)) {
			servicos.add(servico);
		}
	}

	public void removeServico(Servico servico) {
		if (servico == null) {
			throw new IllegalArgumentException("O servico nao pode ser nulo.");
		}
		servicos.remove(servico);
	}

	public void addSenha(Senha senha) {
		if (senha == null) {
			throw new IllegalArgumentException("A senha nao pode ser nula.");
		}
		if (!senhas.contains(senha)) {
			senhas.add(senha);
		}
	}

	public void removeSenha(Senha senha) {
		if (senha == null) {
			throw new IllegalArgumentException("A senha nao pode ser nula.");
		}
		senhas.remove(senha);
	}

	public void addConsulta(Consulta consulta) {
		if (consulta == null) {
			throw new IllegalArgumentException("A consulta nao pode ser nula.");
		}
		if (!consultas.contains(consulta)) {
			int pos = 0;
			while (pos < consultas.size() && consultaVemDepois(consulta, consultas.get(pos))) {
				pos++;
			}
			consultas.add(pos, consulta);
		}

		Utente utente = consulta.getUtente();
		Especialidade especialidade = consulta.getEspecialidade();

		addUtente(utente);
		addEspecialidade(especialidade);

		if (!utente.getConsultas().contains(consulta)) {
			utente.addConsulta(consulta);
		}
		if (!especialidade.getConsultas().contains(consulta)) {
			especialidade.addConsulta(consulta);
		}
	}

	private boolean consultaVemDepois(Consulta nova, Consulta existente) {
		if (nova.getDataMarcacao().isAfter(existente.getDataMarcacao())) {
			return true;
		}
		if (nova.getDataMarcacao().isBefore(existente.getDataMarcacao())) {
			return false;
		}
		return nova.getEspecialidade().getIdentificador()
				.compareTo(existente.getEspecialidade().getIdentificador()) > 0;
	}

	public void removeConsulta(Consulta consulta) {
		if (consulta == null) {
			throw new IllegalArgumentException("A consulta nao pode ser nula.");
		}
		consultas.remove(consulta);
		consulta.getUtente().removeConsulta(consulta);
		consulta.getEspecialidade().removeConsulta(consulta);
	}

	/**
	 * Deve retornar qual a primeira consulta do dia de um utente
	 * 
	 * @param u o utente de quem ver a primeira consulta do dia
	 * @return a primeira consula do dia do utente
	 */
	public Consulta primeiraConsultaDia(Utente u) {
		// FEITO implementar este método

		if (!utentes.contains(u)) {
			return null;
		}
		
		LocalDateTime agora = RelogioSimulado.getTempoAtual();
		Consulta primeira = null;

		for (Consulta c : u.getConsultas()) {
			if (!c.getDataMarcacao().toLocalDate().equals(agora.toLocalDate())) {
				continue;
			}

			if (primeira == null || c.getDataMarcacao().isBefore(primeira.getDataMarcacao())) {
				primeira = c;
			}
		}

		return primeira;

	}

	/**
	 * Valida a consulta. Verifica se a consulta pode ser concretizada e, se sim,
	 * emite a senha respetiva.
	 * 
	 * @param c a consulta a verificar
	 * @return o resultado deve ser um dos seguintes:
	 *         <li>
	 *         CONSULTA_ACEITE se a consulta pode ser realizada
	 *         <li>UTENTE_SEM_CONSULTA_PROXIMA se o utente não tem qualquer consulta
	 *         nas próximas 3 horas
	 *         <li>UTENTE_DEMASIADO_ATRASADO se o utente tinha a consulta há mais de
	 *         2 horas
	 *         <li>UTENTE_ATRASADO_ADIADO se o utente chegou atrasado e isso levou a
	 *         que a hora da consulta fosse adiada
	 *         <li>UTENTE_ATRASADO_FORAHORAS o utente atrsou-se e devido a isso a
	 *         hora de atendimento já fica fora do horário
	 */
	public int validarConsulta(Consulta c) {
		// FEITO validar a consulta
		// FEITO emitir a senha
		if (c == null) {
			throw new IllegalArgumentException("A consulta nao pode ser nula.");
		}

		if (c.estaValidada()) {
			return CONSULTA_JA_VALIDADA;
		}

		LocalDateTime agora = RelogioSimulado.getTempoAtual();
		LocalDateTime dataConsulta = c.getDataMarcacao();

		if (dataConsulta.isAfter(agora.plusHours(HORAS_ANTECEDENCIA_VALIDACAO))) {
			return UTENTE_SEM_CONSULTA_PROXIMA;
		}

		if (dataConsulta.isBefore(agora.minusHours(HORAS_ATRASO_MAXIMO))) {
			removeConsulta(c);
			return UTENTE_DEMASIADO_ATRASADO;
		}

		LocalDateTime atendimento = dataConsulta;
		if (agora.isAfter(dataConsulta)) {
			atendimento = agora.plusMinutes(MINUTOS_ATRASO_ATENDIMENTO);
		}

		if (!c.getEspecialidade().getHorario().contem(atendimento.toLocalTime())) {
			removeConsulta(c);
			return UTENTE_ATRASADO_FORAHORAS;
		}

		emiteSenha(c, agora, atendimento);

		if (atendimento.isAfter(dataConsulta)) {
			return UTENTE_ATRASADO_ADIADO;
		}

		return CONSULTA_ACEITE;
	}

	/**
	 * Emite a senha para a consulta. Se a consulta já está validada, retorna a
	 * senha previamente emitida.
	 * 
	 * @param c           a consulta a qual a senha ficará associada
	 * @param entrada     a data de entrada no sistema
	 * @param atendimento a data prevista de atendimento (pode ser diferente da data
	 *                    da consulta)
	 * @return a senha criada
	 */
	public Senha emiteSenha(Consulta c, LocalDateTime entrada, LocalDateTime atendimento) {
		// FEITO implementar este método
		if (c == null) {
			throw new IllegalArgumentException("A consulta nao pode ser nula.");
		}
		if (entrada == null || atendimento == null) {
			throw new IllegalArgumentException("As datas da senha nao podem ser nulas.");
		}

		if (c.estaValidada()) {
			return c.getSenha();
		}

		String numero = "A" + (senhas.size() + 1);
		Senha senha = new Senha(this, numero, entrada, atendimento, c);

		c.setSenha(senha);
		addSenha(senha);
		c.getEspecialidade().addSenha(senha);

		return senha;
	}

	/**
	 * Indicação ao sistema de que a consulta terminou. Deve eliminar a consulta e a
	 * senha respetiva
	 * 
	 * @param c a consulta terminada
	 */
	public void terminaConsulta(Consulta c) {
		// FEITO implementar este método
		if (c == null) {
			throw new IllegalArgumentException("A consulta nao pode ser nula.");
		}

		Senha senha = c.getSenha();
		if (senha != null) {
			removeSenha(senha);
			c.getEspecialidade().removeSenha(senha);
			c.setSenha(null);
		}
		removeConsulta(c);
	}

	/**
	 * Verica se a consulta pode ser adicionada ao sistema
	 * 
	 * @param c a consulta a testar
	 * @return o resultado deve ser um dos seguintes:
	 *         <li>CONSULTA_ACEITE se a consulta puder ser adicionada ao sistema
	 *         <li>DATA_JA_PASSOU se a data for numa altura anterior à data atual
	 *         <li>UTENTE_JA_TEM_CONSULTA se o utente já tem uma consulta no horário
	 *         indicado
	 *         <li>ESPECIALISTA_JA_TEM_CONSULTA se o o especialista já tiver outra
	 *         consulta na mesma data
	 */
	public int podeAceitarConsulta(Consulta c) {
		// FEITO implementar este método
		if (c == null) {
			throw new IllegalArgumentException("A consulta nao pode ser nula.");
		}

		LocalDateTime agora = RelogioSimulado.getTempoAtual();
		LocalDateTime dataConsulta = c.getDataMarcacao();

		if (dataConsulta.isBefore(agora)) {
			return DATA_JA_PASSOU;
		}

		if (!c.getEspecialidade().getHorario().contem(dataConsulta.toLocalTime())) {
			return DATA_JA_PASSOU;
		}

		for (Consulta outra : consultas) {
			if (outra.getUtente().equals(c.getUtente())) {
				long diferenca = ChronoUnit.MINUTES.between(dataConsulta, outra.getDataMarcacao());
				if (diferenca < 0) {
					diferenca = -diferenca;
				}
				if (diferenca < MINUTOS_ENTRE_CONSULTAS_UTENTE) {
					return UTENTE_JA_TEM_CONSULTA;
				}
			}

			if (outra.getEspecialidade().equals(c.getEspecialidade())) {
				long diferenca = ChronoUnit.MINUTES.between(dataConsulta, outra.getDataMarcacao());
				if (diferenca < 0) {
					diferenca = -diferenca;
				}
				if (diferenca < MINUTOS_ENTRE_CONSULTAS_ESPECIALIDADE) {
					return ESPECIALISTA_JA_TEM_CONSULTA;
				}
			}
		}

		return CONSULTA_ACEITE;
	}

	/**
	 * Verica se a consulta pode ser alterada
	 * 
	 * @param c a consulta com as alterações
	 * @return o resultado deve ser um dos seguintes:
	 *         <li>CONSULTA_ACEITE se a consulta puder ser adicionada ao sistema
	 *         <li>DATA_JA_PASSOU se a data for numa altura anterior à data atual
	 *         <li>UTENTE_JA_TEM_CONSULTA se o utente já tem uma consulta no horário
	 *         indicado
	 *         <li>ESPECIALISTA_JA_TEM_CONSULTA se o o especialista já tiver outra
	 *         consulta na mesma data
	 *         <li>ALTERACAO_INVALIDA se mudou a especialidade ou o utente na
	 *         consulta
	 */
	public int podeAlterarConsulta(Consulta antiga, Consulta nova) {
		// FEITO implementar este método

		if(antiga == null || nova == null) {
			throw new IllegalArgumentException("As consultas nao podem ser nulas.");
		}

		if(!antiga.getEspecialidade().equals(nova.getEspecialidade()) || !antiga.getUtente().equals(nova.getUtente())) {
			return ALTERACAO_INVALIDA;
		}

		LocalDateTime agora = RelogioSimulado.getTempoAtual();
		LocalDateTime dataConsulta = nova.getDataMarcacao();

		if (dataConsulta.isBefore(agora)) {
			return DATA_JA_PASSOU;
		}

		if (!nova.getEspecialidade().getHorario().contem(dataConsulta.toLocalTime())) {
			return DATA_JA_PASSOU;
		}

		for (Consulta outra : consultas) {
			if (outra == antiga) {
				continue;
			}

			if (outra.getUtente().equals(nova.getUtente())) {
				long diferenca = ChronoUnit.MINUTES.between(dataConsulta, outra.getDataMarcacao());
				if (diferenca < 0) {
					diferenca = -diferenca;
				}
				if (diferenca < MINUTOS_ENTRE_CONSULTAS_UTENTE) {
					return UTENTE_JA_TEM_CONSULTA;
				}
			}

			if (outra.getEspecialidade().equals(nova.getEspecialidade())) {
				long diferenca = ChronoUnit.MINUTES.between(dataConsulta, outra.getDataMarcacao());
				if (diferenca < 0) {
					diferenca = -diferenca;
				}
				if (diferenca < MINUTOS_ENTRE_CONSULTAS_ESPECIALIDADE) {
					return ESPECIALISTA_JA_TEM_CONSULTA;
				}
			}
		}

		return CONSULTA_ACEITE;
	}

	/**
	 * altera uma consulta por outra
	 * 
	 * @param antiga a consulta que será alterada
	 * @param nova   a consulta já com as alterações
	 * @return o resultado deve ser um dos seguintes:
	 *         <li>CONSULTA_ACEITE se a consulta puder ser adicionada ao sistema
	 *         <li>DATA_JA_PASSOU se a data for numa altura anterior à data atual
	 *         <li>UTENTE_JA_TEM_CONSULTA se o utente já tem uma consulta no horário
	 *         indicado
	 *         <li>ESPECIALISTA_JA_TEM_CONSULTA se o o especialista já tiver outra
	 *         consulta na mesma data
	 *         <li>ALTERACAO_INVALIDA se mudou a especialidade ou o utente na
	 *         consulta
	 */
	public int alteraConsulta(Consulta antiga, Consulta nova) {
		// FEITO implementar este método
		int resultado = podeAlterarConsulta(antiga, nova);
		if (resultado != CONSULTA_ACEITE) {
			return resultado;
		}

		removeConsulta(antiga);
		addConsulta(nova);
		return CONSULTA_ACEITE;
	}
}
