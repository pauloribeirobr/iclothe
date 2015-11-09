package uam.grupoalemanha;

import java.sql.SQLException;
import java.util.Scanner;


/*
 * UAM - Grupo Alemanha - Projeto iClothe - 3 Semestre 2014
 * @author Paulo Ribeiro
 * 
 * @version 1.0
 * 
 * Classe Main - Classe Inicial do Sistema
 */

public class Main {
    private static Scanner scanner;
	
    public static void main(String[] args) throws SQLException {
	
	boolean logado = false;
	scanner = new Scanner(System.in);
		
	/*
	 * Inicialização das Classes de Apoio
	 */
	Usuario usuario = new Usuario();
        MainGerencia mainGerencia = new MainGerencia();
        MainVendedor mainVendedor = new MainVendedor();
	MainEstoque mainEstoque = new MainEstoque();
        

	
	while(!logado) {
            System.out.println("================================================");
            System.out.println(" iClothe - Sistema de Gestão de Lojas de Roupas ");
            System.out.println("================================================");
            System.out.print("Login: ");
            usuario.setLogin(scanner.nextLine());
            System.out.print("Senha: ");
            usuario.setSenha(scanner.nextLine());
			
            if(usuario.loginUsuario(usuario.getLogin(), usuario.getSenha())) {
                //Associa o tipo de usuario ao objeto atual
		usuario.setTipo(usuario.getTipoUsuario(usuario.getLogin()));
		logado = true;
            }else{
                System.out.println("Usuário ou senha invalidos!");
            }

	}
		
	/*
	 * Abre os métodos conforme o tipo do usuário
	 */
	if(usuario.getTipo().equals("GERENCIA")) {
            /*
            Usuário Tipo Gerência
            */
            mainGerencia.TelaGerencia();
	}
        
	if(usuario.getTipo().equals("ESTOQUE")) {
            /*
            Usuário Tipo Estoque
            */
            mainEstoque.TelaEstoque();
	}
        
 	if(usuario.getTipo().equals("VENDEDOR")) {
            /*
            Usuário Tipo Vendedor
            */
            mainVendedor.TelaVendedor();
	}
        
        
	
    }
	


}
