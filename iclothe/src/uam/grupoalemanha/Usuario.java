package uam.grupoalemanha;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Scanner;

/*
 * UAM - Grupo Alemanha - Projeto iClothe - 3 Semestre 2014
 * @author Paulo Ribeiro
 * 
 * @version 1.0
 * 
 * Classe Usuario - Usuários do iClothe
 */

public class Usuario {
    /*
    Definição dos atributos
     */
    private String login;
    private String senha;
    private String tipo;

    /*
    Atributos de uso nas Operações do Banco
     */
    boolean resultadoOperacao = false;
    String stringDeConexao = "jdbc:sqlite:db/iclothe.db";

    /*
    Gets e Sets de cada atributo
     */
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
	this.senha = senha;
    }

    public String getTipo() {
	return tipo;
    }

    public void setTipo(String tipo) {
	this.tipo = tipo;
    }

    /*
    Método menuUsuario
    */
    public void menuUsuario() throws SQLException {
        int opc = 1;
		
	while (opc != 0) {

            System.out.println("================================================");
            System.out.println(" iClothe - Cadastro de Usuarios                 ");
            System.out.println("================================================");
            System.out.println("1 - Listar Usuarios");
            System.out.println("2 - Cadastrar Usuario");
            System.out.println("0 - Voltar");
            System.out.print("Qual a sua opção: ");
	
            Scanner scanner = new Scanner(System.in);

            String valorTela = scanner.nextLine();
            opc = Integer.parseInt(valorTela);

            if (opc == 1) {
                listarUsuarios();
            }

            if (opc == 2) {
		inserirUsuario();
            }
	}
    }

    /*
    Método loginUsuario - verifica a combinação usuário / senha
    */
    public boolean loginUsuario(String login, String senha) throws SQLException {
        Connection conexao = DriverManager.getConnection(stringDeConexao);
	try {
            PreparedStatement comando = conexao
            .prepareStatement("SELECT LOGIN FROM USUARIOS WHERE LOGIN = ? AND SENHA = ?");
            comando.setString(1, login);
            comando.setString(2, senha);
            ResultSet resultado = comando.executeQuery();

            if (resultado.next()) {
            	resultadoOperacao = true;
            } else {
		resultadoOperacao = false;
            }

	} catch (Exception e) {
            e.printStackTrace();
	} finally {
            conexao.close();
	}

	return resultadoOperacao;
    }

    /*
    Método inserirUsuario - Insere um novo usuario na tabela Usuarios
    */
    public void inserirUsuario() throws SQLException {
		
        int tipoFuncionario     = 0;

        Usuario usuario = new Usuario();
        Scanner scanner = new Scanner(System.in);

        System.out.println("==========================================");
        System.out.println("Cadastrar Novo Usuario                    ");
        System.out.println("------------------------------------------");
        System.out.print("Digite o Login: ");
        usuario.setLogin(scanner.nextLine());
        System.out.print("Digite a Senha: ");
        usuario.setSenha(scanner.nextLine());
        System.out.print("Tipo - 1-VENDEDOR, 2-ESTOQUE, 3-GERENCIA - numero: ");
        tipoFuncionario = Integer.parseInt(scanner.nextLine());

        /*
        Faz a conversão da seleção numérica (tipoFuncionario) para String (tipo)
        */
        switch(tipoFuncionario) {
            case 1:
                usuario.setTipo("VENDEDOR");
            break;
            case 2:
                usuario.setTipo("ESTOQUE");
            break;
            case 3:
                usuario.setTipo("GERENCIA");
            break;
            default:
                usuario.setTipo("VENDEDOR");
        }

        Connection conexao = DriverManager.getConnection(stringDeConexao);
        try {

            PreparedStatement comando = conexao.prepareStatement("INSERT INTO USUARIOS (LOGIN, SENHA, TIPO) VALUES (?, ?, ?)");
            comando.setString(1, usuario.getLogin());
            comando.setString(2, usuario.getSenha());
            comando.setString(3, usuario.getTipo());

            comando.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conexao.close();
        }

 
    }

    /*
    Método getTipoUsuario - Retorna qual o tipo do Usuário
    */
    public String getTipoUsuario(String login) throws SQLException {
	String tipoUsuario = null;
	Connection conexao = DriverManager.getConnection(stringDeConexao);
	try {
            PreparedStatement comando = conexao.prepareStatement("SELECT SENHA, TIPO FROM USUARIOS WHERE LOGIN = ?");
            comando.setString(1, login);
            ResultSet resultado = comando.executeQuery();

            while (resultado.next()) {
                tipoUsuario = (resultado.getString("TIPO"));
            }
	} catch (Exception e) {
            e.printStackTrace();
	} finally {
            conexao.close();
	}
	return tipoUsuario;
    }

    /*
    Método listarUsuarios() - Lista todos os usuários cadastrados
    */
    public void listarUsuarios() throws SQLException {
        Connection conexao = DriverManager.getConnection(stringDeConexao);
	try {
            PreparedStatement comando = conexao.prepareStatement("SELECT LOGIN, SENHA, TIPO FROM USUARIOS ORDER BY LOGIN");
            ResultSet resultado = comando.executeQuery();
            System.out.println("==================================================");
            System.out.println("Relação de Usuários Cadastrados no Sistema        ");
            System.out.println("--------------------------------------------------");
            System.out.println("LOGIN               SENHA          TIPO           ");
            System.out.println("--------------------------------------------------");

            while (resultado.next()) {
                /*
                Caracteres de Formatação - Ex: %-15s - 15 posições, alinhado a esquerda
                */
                System.out.printf("%-20s", resultado.getString("LOGIN"));
                System.out.printf("%-15s", resultado.getString("SENHA"));
                System.out.printf("%-15s", resultado.getString("TIPO"));
                System.out.println("");
            }
            System.out.println("--------------------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
	} finally {
            conexao.close();
        }
    }

}
