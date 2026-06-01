package gestsaude.recurso;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import poo.util.Validator;

/**
 * Representa uma Senha. Deve ter um numero (letra e numero, por exemplo A1, A2,
 * A12), a hora de entrada e a hora prevista de atendimento e qual a consulta
 * associada.
 */
public class Senha {

	private GEstSaude gest;
	private String numero;
	private LocalDateTime horaEntrada;
	private LocalDateTime horaPrevistaAtendimento;
	private Consulta consulta;
	private ArrayList<Servico> servicos;
	private boolean consultaTerminada;

	public Senha(GEstSaude gest, String numero, LocalDateTime horaEntrada, LocalDateTime horaPrevistaAtendimento,
			Consulta consulta) {
		this.gest = checkGest(gest);
		this.numero = checkNumero(numero);
		this.horaEntrada = checkHoraEntrada(horaEntrada);
		this.horaPrevistaAtendimento = checkHoraPrevistaAtendimento(horaPrevistaAtendimento);
		this.consulta = checkConsulta(consulta);
		this.servicos = new ArrayList<>();
		this.consultaTerminada = false;
	}

	public GEstSaude getGest() {
		return gest;
	}

	public String getNumero() {
		return numero;
	}

	public LocalDateTime getHoraEntrada() {
		return horaEntrada;
	}

	public LocalDateTime getHoraPrevistaAtendimento() {
		return horaPrevistaAtendimento;
	}

	public void setHoraPrevistaAtendimento(LocalDateTime horaPrevistaAtendimento) {
		this.horaPrevistaAtendimento = checkHoraPrevistaAtendimento(horaPrevistaAtendimento);
	}

	public Consulta getConsulta() {
		return consulta;
	}

	public Collection<Servico> getServicos() {
		return Collections.unmodifiableCollection(servicos);
	}

	public void addServico(Servico servico) {
		if (servico == null) {
			throw new IllegalArgumentException("O servico nao pode ser nulo.");
		}
		if (servicos.contains(servico)) {
			throw new IllegalArgumentException("O servico nao pode ser repetido.");
		}
		servicos.add(servico);
	}

	/**
	 * Indica qual o proximo servico que tem de visitar
	 * 
	 * @return o servico que tem de visitar ou null se nao tiver servicos a visitar
	 */
	public Servico servicoAtual() {
		return servicos.isEmpty() ? null : servicos.get(0);
	}

	/**
	 * Terminou o atendimento na consulta ou servico atual. Se ainda tem servicos
	 * por visitar, deve passar para o proximo
	 */
	public void terminaAtendimento() {
		if (!consultaTerminada) {
			consultaTerminada = true;
		} else if (!servicos.isEmpty()) {
			servicos.remove(0);
		}

		// FEITO quando GEstSaude estiver implementado, se nao existirem mais servicos
		// pendentes a senha deve ser removida do sistema e a consulta terminada.
		Servico proximo = servicoAtual();
		if (proximo != null) {
			proximo.addSenha(this);
		} else {
			gest.terminaConsulta(consulta);
		}
	}

	@Override
	public String toString() {
		return "Senha [numero=" + numero + ", horaEntrada=" + horaEntrada + ", horaPrevistaAtendimento="
				+ horaPrevistaAtendimento + ", consulta=" + consulta + ", servicos=" + servicos + "]";
	}

	private GEstSaude checkGest(GEstSaude gest) {
		if (gest == null) {
			throw new IllegalArgumentException("O sistema associado a senha nao pode ser nulo.");
		}
		return gest;
	}

	private String checkNumero(String numero) {
		if (numero == null) {
			throw new IllegalArgumentException("O numero da senha nao pode ser nulo.");
		}
		return Validator.requireNonBlankTrimmed(numero);
	}

	private LocalDateTime checkHoraEntrada(LocalDateTime horaEntrada) {
		if (horaEntrada == null) {
			throw new IllegalArgumentException("A hora de entrada nao pode ser nula.");
		}
		return horaEntrada;
	}

	private LocalDateTime checkHoraPrevistaAtendimento(LocalDateTime horaPrevistaAtendimento) {
		if (horaPrevistaAtendimento == null) {
			throw new IllegalArgumentException("A hora prevista de atendimento nao pode ser nula.");
		}
		return horaPrevistaAtendimento;
	}

	private Consulta checkConsulta(Consulta consulta) {
		if (consulta == null) {
			throw new IllegalArgumentException("A consulta associada a senha nao pode ser nula.");
		}
		return consulta;
	}
}
