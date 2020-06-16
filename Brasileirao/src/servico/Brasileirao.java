package servico;

import java.util.Scanner;

public class Brasileirao {
	public static void menu() {
		System.out.println("Inicio do programa");
		System.out.println("1. Cadastrar Campeonato");
		System.out.println("2. Listar Campeonato");
		System.out.println("3. Limpar Campeonato");
		System.out.println("4. Cadastrar Time");
		System.out.println("5. Limpar Time");
		System.out.println("6. Sortear Times");
		System.out.println("7. Sortear Resultados");
		System.out.println("8. Listar Partidas");
		System.out.println("9. Ver tabela com Resultados");
		System.out.println("10. Ver vencedores");
		System.out.println("11. Fim");
	}

	public static void main(String[] Args) {

		BancoDeDados bancoDeDados = new BancoDeDados();
		int opcao;
		int a;
		String b;
		Scanner entrada = new Scanner(System.in);
		Scanner e1 = new Scanner(System.in);
		Scanner e2 = new Scanner(System.in);
		
		do {
			menu();
			opcao = entrada.nextInt();

			switch (opcao) {
			case 1:
				bancoDeDados.cadastrarCampeonato(2, 19, 10, 20);
				bancoDeDados.desconectar();
				break;
				
			case 2:
				bancoDeDados.listarCampeonatos();
				bancoDeDados.desconectar();
				break;

			case 3:
				bancoDeDados.limparCampeonato();
				bancoDeDados.desconectar();
				break;

			case 4:
				System.out.println("digite o nome do time a ser incluido");
				b = e2.nextLine();
				bancoDeDados.cadastrarTime(b);
				bancoDeDados.desconectar();
				break;

			case 5:
				System.out.println("digite o numero do time a ser excluido");
				a = e1.nextInt();
				bancoDeDados.limparTime(a);
				bancoDeDados.desconectar();
				break;

			case 6:
				bancoDeDados.sortearTimes();
				bancoDeDados.desconectar();
				break;

			case 7:
				bancoDeDados.sortearResultados();
				bancoDeDados.desconectar();
				break;
				
			case 8:
				bancoDeDados.listarPartidas();
				bancoDeDados.desconectar();
				break;

			case 9:
				bancoDeDados.mostrarClassificaçao();
				bancoDeDados.desconectar();
				break;
			
			case 10:
				bancoDeDados.mostrarVencedores();
				bancoDeDados.desconectar();
				break;
				
			case 11:
				System.out.println("Encerado");
				System.exit(0);

			default:
				System.out.println("Opção inválida.");
			}
		} while (opcao != 0);
		entrada.close();
		e1.close();
		e2.close();
	}
}
