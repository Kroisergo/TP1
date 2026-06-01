package gestsaude.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import gestsaude.recurso.Consulta;

/**
 * Class utilitária que define um conjunto base de operações com listas de
 * consultas. Usaremos Lambdas, quando as aprendermos.
 */
public class Consultas {

	/**
	 * retorna uma lista apenas com as consultas que estão entre as datas definidas
	 * 
	 * @param cs  consultas a filtrar
	 * @param ini data inicial a considerar (inclusive)
	 * @param fim data final a considerar (inclusive)
	 * @return uma lista com as consultas que estão entre as datas definidas
	 */
	public static List<Consulta> getConsultaEntreDatas(Collection<Consulta> cs, LocalDateTime ini, LocalDateTime fim) {
		// FEITO implementar este método
		if (cs == null || ini == null || fim == null) {
			throw new IllegalArgumentException("Os parametros nao podem ser nulos.");
		}

		ArrayList<Consulta> res = new ArrayList<>();
		for (Consulta c : cs) {
			LocalDateTime data = c.getDataMarcacao();
			if ((data.isAfter(ini) || data.equals(ini)) && (data.isBefore(fim) || data.equals(fim))) {
				res.add(c);
			}
		}
		return res;
	}

	/**
	 * Retorna uma lista apenas com as consultas marcadas num dado dia
	 * 
	 * @param cs  consultas a filtrar
	 * @param dia dia a usar
	 * @return uma lista com as consultas marcadas para dia
	 */
	public static List<Consulta> getConsultasDoDia(Collection<Consulta> cs, LocalDate dia) {
		// FEITO implementar este método
		if (cs == null || dia == null) {
			throw new IllegalArgumentException("Os parametros nao podem ser nulos.");
		}

		ArrayList<Consulta> res = new ArrayList<>();
		for (Consulta c : cs) {
			if (c.getDataMarcacao().toLocalDate().equals(dia)) {
				res.add(c);
			}
		}
		return res;
	}

	/**
	 * Retorna uma lista com as consultas após uma dada data
	 * 
	 * @param cs lista de consulta a filtrar
	 * @param t  tempo a partir do qual se deve considerar as consultas (inclusive)
	 * @return uma lista com as consultas após a data t
	 */
	public static List<Consulta> getConsultasApos(List<Consulta> cs, LocalDateTime t) {
		// FEITO implementar este método
		if (cs == null || t == null) {
			throw new IllegalArgumentException("Os parametros nao podem ser nulos.");
		}

		ArrayList<Consulta> res = new ArrayList<>();
		for (Consulta c : cs) {
			LocalDateTime data = c.getDataMarcacao();
			if (data.isAfter(t) || data.equals(t)) {
				res.add(c);
			}
		}
		return res;
	}
}
