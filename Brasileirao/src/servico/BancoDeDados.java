package servico;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BancoDeDados {

	public Connection getConnection() {
		Connection conn = null;
		String servidor = "jdbc:mysql://localhost:3306/brasileiro";
		String usuario = "root";
		String senha = "Manci_gamer5";
		String driver = "com.mysql.jdbc.Driver";

		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(servidor, usuario, senha);
		} catch (Exception e) {
			System.out.println("Erro: " + e.getMessage());
		}
		return conn;
	}

	public void listarCampeonatos() {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			Connection conn = getConnection();
			if (conn != null) {
				String query = "SELECT * FROM campeonato";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(query);
				while (rs.next()) {
					System.out.println("turnos: " + rs.getInt("qtd_turnos") + "  rodadas: " + rs.getInt("qtd_rodadas")
							+ "  partidas: " + rs.getInt("qtd_partidas") + "  clubes: " + rs.getInt("qtd_clubes"));
				}
			}
		} catch (Exception e) {
			System.out.println("Erro: " + e.getMessage());
		}
	}

	public void listarTimes() {
		ResultSet rs = null;
		Statement stmt = null;
		try {
			Connection conn = getConnection();
			String query = "SELECT * FROM time";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				System.out.println("time " + rs.getInt("numero_time") + ": " + rs.getString("nome_time"));
			}
		} catch (Exception e) {
			System.out.println("Erro: " + e.getMessage());
		}
	}

	public void limparCampeonato() {
		Statement stmt = null;
		try {
			Connection conn = getConnection();
			stmt = conn.createStatement();
			String query;

			query = "delete from brasileiro.partida";
			stmt.executeUpdate(query);
			query = "delete from brasileiro.rodada";
			stmt.executeUpdate(query);
			query = "delete from brasileiro.turno";
			stmt.executeUpdate(query);
			query = "delete from brasileiro.campeonato";
			stmt.executeUpdate(query);
			query = "UPDATE `brasileiro`.`time` SET pontos = 0, vitorias = 0, derrotas = 0, empates = 0, gols_pro = 0, gols_contra = 0, saldo_gols = 0, ca = 0, cv = 0;";
			stmt.executeUpdate(query);
		} catch (Exception e) {
			System.out.println("Erro: " + e.getMessage());
		}
	}

	public void cadastrarCampeonato(int turno, int rodada, int partida, int clubes) {
		Statement stmt = null;
		try {
			Connection conn = getConnection();
			String query = "INSERT INTO campeonato (qtd_turnos, qtd_rodadas, qtd_partidas, qtd_clubes) VALUES ('"
					+ turno + "', '" + rodada + "', '" + partida + "', '" + clubes + "')";
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
		} catch (Exception e) {
			System.out.println("Erro: " + e.getMessage());
		}
	}

	public void cadastrarTime(String nome) {
		Statement stmt = null;
		try {
			Connection conn = getConnection();
			String query = "INSERT INTO time (nome_time) VALUES ('" + nome + "')";
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
		} catch (Exception e) {
			System.out.println("Erro: " + e.getMessage());
		}
	}

	public void limparTime(int numero_time) {
		Statement stmt = null;
		try {
			Connection conn = getConnection();
			String query = "DELETE FROM `brasileiro`.`time` WHERE (numero_time = '" + numero_time + "')";
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
		} catch (Exception e) {
			System.out.println("Erro: " + e.getMessage());
		}
	}

	public void sortearTimes() {
		ResultSet rs = null;
		Statement stmt = null;
		List<String> clubes = new ArrayList<String>();
		try {
			Connection conn = getConnection();
			String query = "SELECT * FROM time";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				System.out.println("time " + rs.getInt("numero_time") + ": " + rs.getString("nome_time"));
				clubes.add(rs.getString("numero_time"));
			}

			int partida = 1;
			int turno = 1;
			for (turno = 1; turno <= 2; turno++) {
				String s_truno = "INSERT INTO `brasileiro`.`turno` (`numero_turno`) VALUES ('" + turno + "')";
				stmt.executeUpdate(s_truno);

				int rodada;
				int t = clubes.size();
				int m = clubes.size() / 2;
				for (int i = 0; i < t - 1; i++) {
					rodada = i + 1;
					System.out.print((rodada) + "a rodada: ");
					String s_rodada = "INSERT INTO `brasileiro`.`rodada` (`numero_rodada`, `numero_turno`) VALUES ('"
							+ rodada + "', '" + turno + "');";
					stmt.executeUpdate(s_rodada);

					for (int j = 0; j < m; j++) {
						// Clube está de fora nessa rodada?
						if (clubes.get(j).isEmpty())
							continue;

						// Teste para ajustar o mando de campo
						if (j % 2 == 1 || i % 2 == 1 && j == 0) {
							System.out.println(clubes.get(t - j - 1) + " x " + clubes.get(j) + "   ");
							query = "INSERT INTO `brasileiro`.`partida` (numero_partida , numero_rodada, time_a, time_b) VALUES ("
									+ partida + ", " + rodada + ", " + clubes.get(t - j - 1) + ", " + clubes.get(j)
									+ ");";
							stmt.executeUpdate(query);

						} else {
							System.out.println(clubes.get(j) + " x " + clubes.get(t - j - 1) + "   ");
							query = "INSERT INTO `brasileiro`.`partida` (numero_partida , numero_rodada, time_a, time_b) VALUES ("
									+ partida + ", " + rodada + ", " + clubes.get(j) + ", " + clubes.get(t - j - 1)
									+ ");";
							stmt.executeUpdate(query);
						}
						partida++;
					}
					System.out.println();
					// Gira os clubes no sentido horário, mantendo o primeiro no lugar
					clubes.add(1, clubes.remove(clubes.size() - 1));
				}
			}
		} catch (Exception e) {
			System.out.println("Erro: " + e.getMessage());
		}
	}

	public void sortearResultados() {
		ResultSet rs = null;
		Statement stmt = null;
		Statement stmt2 = null;
		Statement stmt3 = null;
		Statement stmt4 = null;
		try {
			Connection conn = getConnection();
			Random r = new Random();
			int golsA;
			int golsB;
			int caA;
			int caB;
			int cvA;
			int cvB;
			int vitoriaA;
			int derrotaA;
			int empate;
			int vitoriaB;
			int derrotaB;
			int pontosA;
			int pontosB;

			String query = "SELECT * FROM partida";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				golsA = r.nextInt(5);
				golsB = r.nextInt(5);
				caA = r.nextInt(3);
				caB = r.nextInt(3);
				cvA = r.nextInt(2);
				cvB = r.nextInt(2);
				if (golsA > golsB) {
					vitoriaA = 1;
					derrotaA = 0;
					vitoriaB = 0;
					derrotaB = 1;
					empate = 0;
					pontosA = 3;
					pontosB = 0;
				} else if (golsB > golsA) {
					vitoriaA = 0;
					derrotaA = 1;
					vitoriaB = 1;
					derrotaB = 0;
					empate = 0;
					pontosA = 0;
					pontosB = 3;
				} else {
					vitoriaA = 0;
					derrotaA = 0;
					vitoriaB = 0;
					derrotaB = 0;
					empate = 1;
					pontosA = 1;
					pontosB = 1;
				}

				query = "UPDATE `brasileiro`.`partida` SET `gols_a` = '" + golsA + "', `gols_b` = '" + golsB
						+ "', `ca_a` = '" + caA + "', `ca_b` = '" + caB + "', `cv_a` = '" + cvA + "', `cv_b` = '" + cvB
						+ "' WHERE (numero_partida = " + rs.getInt("numero_partida") + ");";
				stmt2 = conn.createStatement();
				stmt2.executeUpdate(query);

				query = "UPDATE `brasileiro`.`time` SET `gols_pro` = `gols_pro` + " + golsA
						+ ", `gols_contra` = `gols_contra` + " + golsB
						+ ", `saldo_gols` = `gols_pro` - `gols_contra`, `ca` = `ca` + " + caA + ", `cv` = `cv` + " + cvA
						+ ", `vitorias` = `vitorias` + " + vitoriaA + ", `derrotas` = `derrotas` + " + derrotaA
						+ ", `empates` = `empates` + " + empate + ", `pontos` = `pontos` + " + pontosA
						+ "  WHERE (numero_time = " + rs.getInt("time_a") + ");";
				stmt3 = conn.createStatement();
				stmt3.executeUpdate(query);

				query = "UPDATE `brasileiro`.`time` SET `gols_pro` = `gols_pro` + " + golsB
						+ ", `gols_contra` = `gols_contra` + " + golsA
						+ ", `saldo_gols` = `gols_pro` - `gols_contra`, `ca` = `ca` + " + caB + ", `cv` = `cv` + " + cvB
						+ ", `vitorias` = `vitorias` + " + vitoriaB + ", `derrotas` = `derrotas` + " + derrotaB
						+ ", `empates` = `empates` + " + empate + ", `pontos` = `pontos` + " + pontosB
						+ "  WHERE (numero_time = " + rs.getInt("time_b") + ");";
				stmt4 = conn.createStatement();
				stmt4.executeUpdate(query);
			}
		} catch (Exception e) {
			System.out.println("Erro : " + e.getMessage());
		}
	}

	public void mostrarClassificaçao() {
		ResultSet rs = null;
		Statement stmt = null;
		try {
			Connection conn = getConnection();
			String query = "SELECT * FROM time ORDER BY pontos desc";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				System.out.println("time: " + rs.getString("nome_time") + "  pontos: " + rs.getInt("pontos")
						+ "  vitorias: " + rs.getInt("vitorias") + "  derrotas: " + rs.getInt("derrotas")
						+ "  empates: " + rs.getInt("empates") + "  gols pro: " + rs.getInt("gols_pro")
						+ "  gols contra: " + rs.getInt("gols_contra") + "  saldo de gols: " + rs.getInt("saldo_gols"));
			}
		} catch (Exception e) {
			System.out.println("Erro: " + e.getMessage());
		}
	}

	public void listarPartidas() {
		ResultSet rs = null;
		Statement stmt = null;
		try {
			Connection conn = getConnection();
			String query = "select numero_rodada, numero_partida, t1.nome_time as time1, t2.nome_time as time2 \r\n"
					+ "from  partida p \r\n" + "join `brasileiro`.`time` t1 on (p.time_a = t1.numero_time)\r\n"
					+ "join `brasileiro`.`time` t2 on (p.time_b = t2.numero_time)\r\n" + "order by numero_partida";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				System.out.println("numero rodada: " + rs.getInt("numero_rodada") + "  numero partida: "
						+ rs.getInt("numero_partida") + "  " + rs.getString("time1") + "  x  " + rs.getString("time2"));
			}
		} catch (Exception e) {
			System.out.println("Erro: " + e.getMessage());
		}
	}

	public void mostrarVencedores() {
		String query;
		ResultSet rs = null;
		ResultSet rs2 = null;
		Statement stmt = null;
		Statement stmt2 = null;
		Time timeAnterior = new Time();

		try {
			Connection conn = getConnection();
			stmt = conn.createStatement();
			stmt2 = conn.createStatement();

			query = "select * from time order by pontos desc, vitorias desc, saldo_gols desc ,gols_pro desc;";
			rs = stmt.executeQuery(query);
			rs.next();
			timeAnterior.setPontos(rs.getInt("pontos"));
			timeAnterior.setVitorias(rs.getInt("vitorias"));
			timeAnterior.setSaldo_gols(rs.getInt("saldo_gols"));
			timeAnterior.setGols_pro(rs.getInt("gols_pro"));

			query = "select count(*) as count from time where (pontos = " + timeAnterior.getPontos()
					+ " AND vitorias = " + timeAnterior.getVitorias() + " AND saldo_gols = "
					+ timeAnterior.getSaldo_gols() + " AND gols_pro = " + timeAnterior.getGols_pro() + ");";
			rs = stmt.executeQuery(query);
			rs.next();
			if (rs.getInt("count") == 1) {
				query = "select nome_time from time where (pontos = " + timeAnterior.getPontos() + " AND vitorias = "
						+ timeAnterior.getVitorias() + " AND saldo_gols = " + timeAnterior.getSaldo_gols()
						+ " AND gols_pro = " + timeAnterior.getGols_pro() + ");";
				rs2 = stmt2.executeQuery(query);
				if (rs2.next()) {
					System.out.println("time campeão: " + rs2.getString("nome_time"));
				} else {
					System.out.println("Erro no sistema");
					System.exit(0);
				}
			} else if (rs.getInt("count") == 2) {
				query = "select * from time where (pontos = " + timeAnterior.getPontos() + " AND vitorias = "
						+ timeAnterior.getVitorias() + " AND saldo_gols = " + timeAnterior.getSaldo_gols()
						+ " AND gols_pro = " + timeAnterior.getGols_pro() + ");";
				rs2 = stmt2.executeQuery(query);

				String s;
				rs2.next();
				s = "" + rs2.getInt("numero_time");
				rs2.next();
				s = s + "," + rs2.getInt("numero_time");

				query = "select * from partida where ( time_a in ( " + s + " ) and time_b in  ( " + s + " ));";
				rs2 = stmt2.executeQuery(query);
				int vitoriaA = 0;
				int empate = 0;
				int partida = 0;
				while (rs2.next()) {
					if (rs2.getInt("gols_a") > rs2.getInt("gols_b")) {
						vitoriaA = vitoriaA + 1;
						empate = empate + 0;
						partida = partida + 1;
					} else if (rs2.getInt("gols_a") < rs2.getInt("gols_b")) {
						vitoriaA = vitoriaA + 0;
						empate = empate + 0;
						partida = partida + 1;
					} else {
						vitoriaA = vitoriaA + 0;
						empate = empate + 1;
						partida = partida + 1;
					}
					query = "select * from time where (pontos = " + timeAnterior.getPontos() + " AND vitorias = "
							+ timeAnterior.getVitorias() + " AND saldo_gols = " + timeAnterior.getSaldo_gols()
							+ " AND gols_pro = " + timeAnterior.getGols_pro() + ");";
					rs = stmt.executeQuery(query);
					rs.next();
					String timeA = rs.getString("nome_time");
					rs.next();
					String timeB = rs.getString("nome_time");
					if (vitoriaA == 0 && empate == 0 && partida == 2) { // 2 vitorias B
						System.out.println("time campeão: " + timeB);
					} else if (vitoriaA == 2 && empate == 0 && partida == 2) { // 2 vitorias A
						System.out.println("time campeão: " + timeA);
					} else if (vitoriaA == 1 && empate == 1 && partida == 2) { // 1 vitoria A e 1 empate
						System.out.println("time campeão: " + timeA);
					} else if (vitoriaA == 0 && empate == 1 && partida == 2) { // 1 vitoria B e 1 empate
						System.out.println("time campeão: " + timeB);
					} else if (vitoriaA == 1 && empate == 0 && partida == 2) { // 1 vitoria A e 1 vitoria B
						query = "select * from time order by pontos desc, vitorias desc, saldo_gols desc, gols_pro desc, cv asc, ca asc;";
						rs2 = stmt2.executeQuery(query);
						rs2.next();
						System.out.println("time campeão: " + rs2.getString("nome_time"));
					} else if (vitoriaA == 0 && empate == 2 && partida == 2) { // 2 empates
						query = "select * from time order by pontos desc, vitorias desc, saldo_gols desc, gols_pro desc, cv asc, ca asc;";
						rs2 = stmt2.executeQuery(query);
						rs2.next();
						System.out.println("time campeão: " + rs2.getString("nome_time"));
					}
				}
			} else {
				query = "select * from time order by pontos desc, vitorias desc, saldo_gols desc, gols_pro desc, cv asc, ca asc;";
				rs2 = stmt2.executeQuery(query);
				rs2.next();
				System.out.println("time campeão: " + rs2.getString("nome_time"));
			}
		} catch (Exception e) {
			System.out.println("Erro: " + e.getMessage());
		}
	}

	public void desconectar() {
		Connection conn;
		try {
			conn = getConnection();
			conn.close();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

}
