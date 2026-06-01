package gestsaude.arranque;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

import javax.swing.Timer;

import gestsaude.menu.MaquinaEntrada;
import gestsaude.menu.MenuEspecialidade;
import gestsaude.menu.MenuSecretaria;
import gestsaude.menu.MenuServico;
import gestsaude.menu.RelogioDialog;
import gestsaude.recurso.Consulta;
import gestsaude.recurso.Especialidade;
import gestsaude.recurso.GEstSaude;
import gestsaude.recurso.Servico;
import gestsaude.recurso.Utente;
import poo.util.RelogioSimulado;

/**
 * Classe responsavel pelo arranque do sistema.
 * Tem um metodo para criar a configuracao de teste.
 */
public class Arranque {

	/**
	 * Cria a configuracao inicial do sistema
	 * 
	 * @return um GEstSaude ja completamente configurado
	 */
	public static GEstSaude getGEstSaude() {
		// colocar o relogio simulado nas 8:00
		RelogioSimulado.getRelogioSimulado().setTempoAtual(LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 0)));

		GEstSaude g = new GEstSaude();

		// FEITO criar os utentes
		Utente u121 = new Utente("Raquel Marques Soares", 121);
		Utente u122 = new Utente("Daniel Mendes Rodrigues", 122);
		Utente u123 = new Utente("Zeferino Dias Torres", 123);
		Utente u124 = new Utente("Anabela Dias Santos", 124);
		Utente u125 = new Utente("Felizberto Degradado", 125);
		Utente u126 = new Utente("Antonina Martins Pires", 126);
		Utente u127 = new Utente("Camaleão das Neves Freitas", 127);
		Utente u128 = new Utente("João Pais Pereira", 128);
		Utente u129 = new Utente("Carlos Freitas Lobo", 129);
		Utente u130 = new Utente("Daniel Mendes Rodrigues", 130);
		Utente u120 = new Utente("Dália Ribeiro Sanches", 120);

		g.addUtente(u121);
		g.addUtente(u122);
		g.addUtente(u123);
		g.addUtente(u124);
		g.addUtente(u125);
		g.addUtente(u126);
		g.addUtente(u127);
		g.addUtente(u128);
		g.addUtente(u129);
		g.addUtente(u130);
		g.addUtente(u120);

		// FEITO criar as especialidades
		Especialidade orto1 = new Especialidade("Orto1", "Ortopedia - Dra Iris Tapada");
		Especialidade pneu = new Especialidade("Pneu", "Pneumologia - Dr Paul Mao");
		Especialidade ped1 = new Especialidade("Ped1", "Pediatria - P. Quana");
		Especialidade derm1 = new Especialidade("Derm1", "Dermatologia - Dra V. Rua");
		Especialidade orto2 = new Especialidade("Orto2", "Otorrino - Dra Odete Ote");

		g.addEspecialidade(orto1);
		g.addEspecialidade(pneu);
		g.addEspecialidade(ped1);
		g.addEspecialidade(derm1);
		g.addEspecialidade(orto2);

		// FEITO criar os servicos
		Servico scopia = new Servico("Scopia", "Lab. Endos", "Endos/Colonoscopia");
		Servico neuLab = new Servico("NeuLab", "Lab. Neurologia", "EEG + Dopler");
		Servico rad = new Servico("Rad", "Sala RX", "Radiologia");
		Servico audio = new Servico("Audio", "Lab. Som", "Audiometria");
		Servico enf = new Servico("Enf", "Enfermaria", "Enfermaria");

		g.addServico(scopia);
		g.addServico(neuLab);
		g.addServico(rad);
		g.addServico(audio);
		g.addServico(enf);

		// FEITO criar as consultas
		LocalDate hoje = LocalDate.now();
		LocalDate amanha = hoje.plusDays(1);
		g.addConsulta(new Consulta(LocalDateTime.of(hoje, LocalTime.of(8, 10)), u122, orto1));
		g.addConsulta(new Consulta(LocalDateTime.of(hoje, LocalTime.of(8, 10)), u120, ped1));
		g.addConsulta(new Consulta(LocalDateTime.of(hoje, LocalTime.of(8, 10)), u121, ped1));
		g.addConsulta(new Consulta(LocalDateTime.of(hoje, LocalTime.of(8, 20)), u125, derm1));
		g.addConsulta(new Consulta(LocalDateTime.of(hoje, LocalTime.of(8, 30)), u126, ped1));
		g.addConsulta(new Consulta(LocalDateTime.of(hoje, LocalTime.of(8, 40)), u127, ped1));
		g.addConsulta(new Consulta(LocalDateTime.of(amanha, LocalTime.of(8, 10)), u127, ped1));
		g.addConsulta(new Consulta(LocalDateTime.of(amanha, LocalTime.of(8, 20)), u123, ped1));

		return g;
	}

	/**
	 * arranque do sistema
	 */
	public static void main(String[] args) {
		// criar o GEstSaude
		GEstSaude gs = getGEstSaude();

		// Definir o tempo por segundo no relogio simulado
		RelogioSimulado relogio = RelogioSimulado.getRelogioSimulado();
		relogio.setTicksPorSegundo(1); // alterar este valor se quiserem que o tempo passe mais rapido

		// criar o menu da secretaria, neste caso ira ter apenas um
		MenuSecretaria secretaria = new MenuSecretaria(new Point(230, 10), "Secretaria", gs);
		secretaria.setVisible(true);

		// criar a janela do relogio
		RelogioDialog relogioDlg = new RelogioDialog(secretaria, new Point(10, 10));
		relogioDlg.setVisible(true);

		// criar uma maquina de entrada
		MaquinaEntrada me1 = new MaquinaEntrada(secretaria, new Point(10, 150), "Entrada 1", gs);
		me1.setVisible(true);

		// criar todos os menus de servico e de especialidades
		// FEITO ver as especialidades e os servicos do sistema
		Collection<Especialidade> especiais = gs.getEspecialidades();
		Collection<Servico> servicos = gs.getServicos();
		int idx = 0;
		int totalJanelas = 0;
		MenuEspecialidade menusEsp[] = new MenuEspecialidade[especiais.size()];
		for (Especialidade e : especiais) {
			menusEsp[idx] = new MenuEspecialidade(secretaria,
					new Point(10 + (totalJanelas % 5) * 200, 270 + (totalJanelas / 5) * 180), e, gs);
			menusEsp[idx].setVisible(true);
			idx++;
			totalJanelas++;
		}
		MenuServico menusServ[] = new MenuServico[servicos.size()];
		idx = 0;
		for (Servico s : servicos) {
			menusServ[idx] = new MenuServico(secretaria,
					new Point(10 + (totalJanelas % 5) * 200, 270 + (totalJanelas / 5) * 180), s);
			menusServ[idx].setVisible(true);
			idx++;
			totalJanelas++;
		}

		// criar um temporizador que vai atualizando as varias janelas
		// do menu de servicos, de 5 em 5 segundos (5000 milisegundos)
		Timer t = new Timer(5000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (MenuEspecialidade me : menusEsp)
					me.atualizarInfo();
				for (MenuServico ms : menusServ)
					ms.atualizarInfo();

				secretaria.atualizar();
			}
		});
		t.start();
	}
}
