package uam.grupoalemanha;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Scanner;

/*
 * UAM - Grupo Alemanha - Projeto iClothe - 3 Semestre - 2014
 * @author Paulo Ribeiro
 * 
 * @version 1.0
 * 
 * Classe Cliente - Clientes do iClothe
 */

public class Cliente {
    /*
     * Definição dos Atributos
     */
    private int 	id;
    private String 	nome;
    private String 	cpf;
    private String 	endereco;
    private String	bairro;
    private String	cidade;
    private String 	estado;

    /*
    * Gera GETS e SETS
    */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    /*
     * Atributos de uso nas Operações do Banco
     */
    boolean resultadoOperacao = false;
    String stringDeConexao = "jdbc:sqlite:db/iclothe.db";
	
    /*
     * Método menuCliente
     */
    public void menuCliente() throws SQLException {
        int opc = 1;
	Scanner scannerMenuCliente = new Scanner(System.in);
	String valorTela;
		
	while (opc != 0) {

            System.out.println("================================================");
            System.out.println(" iClothe - Cadastro de Clientes                 ");
            System.out.println("================================================");
            System.out.println("1 - Listar Cliente");
            System.out.println("2 - Cadastrar Cliente");
            System.out.println("3 - Editar Cliente");
            System.out.println("0 - Voltar");
            System.out.print("Qual a sua opção: ");

            valorTela = scannerMenuCliente.nextLine();
            opc = Integer.parseInt(valorTela);

            if (opc == 1) {
                /*
                Chama o método listarClientes()
                */
                listarClientes();
            }

            if (opc == 2) {
                /*
                Chama o método inserirCliente()
                */
                inserirCliente();
            }
			
            if (opc == 3) {
                /*
                Chama o método editarCliente()
                */
                editarCliente();
            }
			
	}

    }
	
    /*
     * Metodo inserirCliente() - Insere os Clientes no Banco de Dados
     */
    public void inserirCliente() throws SQLException {
		
        Scanner scannerInserirCliente = new Scanner(System.in);
        Cliente cliente = new Cliente();
		
        System.out.println("==========================================");
        System.out.println("Cadastrar Novo Cliente                    ");
        System.out.println("------------------------------------------");
        System.out.print("Digite o Nome: ");
        cliente.setNome(scannerInserirCliente.nextLine());
        System.out.print("Digite o CPF: ");
        cliente.setCpf(scannerInserirCliente.nextLine());
        System.out.print("Digite o Endereco com o numero: ");
        cliente.setEndereco(scannerInserirCliente.nextLine());
        System.out.print("Digite o Bairro: ");
        cliente.setBairro(scannerInserirCliente.nextLine());
        System.out.print("Digite a Cidade: ");
        cliente.setCidade(scannerInserirCliente.nextLine());
        System.out.print("Digite o Estado: ");
        cliente.setEstado(scannerInserirCliente.nextLine());
	
        /*
        * Prepara para inserir na Base de Dados
        */
        Connection conexao = DriverManager.getConnection(stringDeConexao);
        try {
            PreparedStatement comando = conexao.prepareStatement("INSERT INTO CLIENTES (NOME, CPF, ENDERECO, "
                + "BAIRRO, CIDADE, ESTADO) VALUES (?, ?, ?, ?, ?, ?)");
            comando.setString(1, cliente.getNome());
            comando.setString(2, cliente.getCpf());
            comando.setString(3, cliente.getEndereco());
            comando.setString(4, cliente.getBairro());
            comando.setString(5, cliente.getCidade());
            comando.setString(6, cliente.getEstado().toUpperCase()); //UF sempre em letras maiusculas
            comando.execute();
        } catch (Exception e) {
           e.printStackTrace();
        } finally {
            conexao.close();
        }
	
    }
	
    /*
     * Metodo listarCliente() - Lista os Clientes no Banco de Dados
     */
    public void listarClientes() throws SQLException {
        Connection conexao = DriverManager.getConnection(stringDeConexao);
        try {
            PreparedStatement comando = conexao.prepareStatement("SELECT ID, NOME, CPF, ENDERECO, "
                            + "BAIRRO, CIDADE, ESTADO FROM CLIENTES ORDER BY NOME");
            ResultSet resultado = comando.executeQuery();
            System.out.println("===============================================================================================");
            System.out.println("Relação de Clientes                                                                            ");
            System.out.println("-----------------------------------------------------------------------------------------------");
            System.out.println("ID  NOME                ENDERECO                           BAIRRO         CIDADE         ESTADO");
            System.out.println("-----------------------------------------------------------------------------------------------");

            while (resultado.next()) {
                /*
                 * Caracteres de Formatação - Ex: %-15s - 15 posições, alinhado a esquerda
                 */
                System.out.printf("%-4s",  resultado.getInt("ID"));
                System.out.printf("%-20s", resultado.getString("NOME"));
                System.out.printf("%-35s", resultado.getString("ENDERECO"));
                System.out.printf("%-15s", resultado.getString("BAIRRO"));
                System.out.printf("%-15s", resultado.getString("CIDADE"));
                System.out.printf("%-15s", resultado.getString("ESTADO"));		
                System.out.println("");
            }
            System.out.println("-----------------------------------------------------------------------------------------------");

        } catch (Exception e) {
           e.printStackTrace();
        } finally {
            conexao.close();
        }
    }
	
    /*
    Metodo editarCliente() - Edita o cliente
     */
	

    public void editarCliente() throws SQLException {
		
        /*
	Atributos do cliente selecionado
	 */
	String 	nomeAntigo 		= null;
	String 	cpfAntigo 		= null;
	String 	enderecoAntigo   	= null;
	String	bairroAntigo 		= null;
	String	cidadeAntiga 		= null;
	String 	estadoAntigo 		= null;
		
	boolean clienteEncontrado 	= false;
                
        Cliente cliente = new Cliente();

	Scanner scannerEditarCliente = new Scanner(System.in);
		
	System.out.println("==========================================");
	System.out.println("Editar Cliente                            ");
	System.out.println("------------------------------------------");
	System.out.print("Qual é o número do cliente? (ID): ");
	cliente.setId(Integer.parseInt(scannerEditarCliente.nextLine()));
		
	/*
	Primeiro pesquisa o cliente pelo ID
	*/
	Connection conexao = DriverManager.getConnection(stringDeConexao);
	try {
            PreparedStatement comando = conexao.prepareStatement("SELECT NOME, CPF, ENDERECO, "
		+ "BAIRRO, CIDADE, ESTADO FROM CLIENTES WHERE ID = ?");
            comando.setInt(1, cliente.getId());
			
            ResultSet resultado = comando.executeQuery();

            if (resultado.next()) {
                nomeAntigo 		= resultado.getString("NOME");
		cpfAntigo 		= resultado.getString("CPF");
		enderecoAntigo  	= resultado.getString("ENDERECO");
		bairroAntigo 		= resultado.getString("BAIRRO");
		cidadeAntiga 		= resultado.getString("CIDADE");
		estadoAntigo 		= resultado.getString("ESTADO");
		clienteEncontrado   = true;
            }else{
		System.out.println("Cliente não encontrado");
            }
		
	} catch (Exception e) {
            e.printStackTrace();
	} 
		
	if(clienteEncontrado) {
	
            /*
            Se encontrou o cliente, parte pra edição
            */
            System.out.println("Nome: " + nomeAntigo);
            System.out.print("NOVO nome (ENTER - mantem o mesmo):");
            /*
            Se a digitacao for em branco, a nova variavel mantem o valor da antiga
             */
            cliente.setNome(scannerEditarCliente.nextLine());
			
            if(cliente.getNome().isEmpty()) {
                cliente.setNome(nomeAntigo);
            }

            System.out.println("CPF: " + cpfAntigo);
            System.out.print("NOVO CPF (ENTER - mantem o mesmo):");
			
            cliente.setCpf(scannerEditarCliente.nextLine());
            if(cliente.getCpf().isEmpty()) {
                cliente.setCpf(cpfAntigo);
            }
			
            System.out.println("Endereco: " + enderecoAntigo);
            System.out.print("NOVO endereco (ENTER - mantem o mesmo):");
			
            cliente.setEndereco(scannerEditarCliente.nextLine());
            if(cliente.getEndereco().isEmpty()) {
                cliente.setEndereco(enderecoAntigo);
            }
		
            System.out.println("Bairro: " + bairroAntigo);
            System.out.print("NOVO bairro (ENTER - mantem o mesmo):");
			
            cliente.setBairro(scannerEditarCliente.nextLine());
            if(cliente.getBairro().isEmpty()) {
                cliente.setBairro(bairroAntigo);
            }
			
            System.out.println("Cidade: " + cidadeAntiga);
            System.out.print("NOVA cidade (ENTER - mantem a mesma):");
			
            cliente.setCidade(scannerEditarCliente.nextLine());
            if(cliente.getCidade().isEmpty()) {
                cliente.setCidade(cidadeAntiga);
            }
			
            System.out.println("Estado: " + estadoAntigo);
            System.out.print("NOVO estado (ENTER - mantem o mesmo):");
			
            cliente.setEstado(scannerEditarCliente.nextLine());
            if(cliente.getEstado().isEmpty()) {
                cliente.setEstado(estadoAntigo);
            }
			
            /*
            Atualiza o registro
             */
            try {
                PreparedStatement comando = conexao.prepareStatement("UPDATE CLIENTES SET NOME = ?,"
                    + "CPF = ?, ENDERECO = ?, BAIRRO = ?, CIDADE = ?, ESTADO = ? WHERE ID = ?");
		comando.setString(1, cliente.getNome());
		comando.setString(2, cliente.getCpf());
		comando.setString(3, cliente.getEndereco());
		comando.setString(4, cliente.getBairro());
		comando.setString(5, cliente.getCidade());
		comando.setString(6, cliente.getEstado().toUpperCase()); //UF sempre em letras maiusculas
		comando.setInt(7, cliente.getId());
		comando.execute();
            } catch (Exception e) {
                e.printStackTrace();
            } 

	}
            conexao.close();
    }
    
    /*
    método getIdCliente - Retorna o ID do cliente baseado no nome
    */
    public int getIdCliente(String nome) throws SQLException {
        int id = 0;
        
        this.setNome(nome);
         try (Connection conexao = DriverManager.getConnection(stringDeConexao)) {
             try {
                 PreparedStatement comando = conexao.prepareStatement("SELECT ID FROM CLIENTES WHERE NOME = ?");
                 comando.setString(1, this.getNome());
                 ResultSet linhas = comando.executeQuery();
                 
                 if (linhas.next()) {
                     id    = linhas.getInt("ID");
                 }
             }catch (Exception e) {
                 e.printStackTrace();
             }
        }        return id;
        
    }
    
    /*
    método getNomeCliente - Retorna o nome do cliente baseado no id
    */
    public String getNomeCliente(int id) throws SQLException {
        
        this.setId(id);
         try (Connection conexao = DriverManager.getConnection(stringDeConexao)) {
             try {
                 PreparedStatement comando = conexao.prepareStatement("SELECT NOME FROM CLIENTES WHERE ID = ?");
                 comando.setInt(1, this.getId());
                 ResultSet linhas = comando.executeQuery();
                 
                 if (linhas.next()) {
                     nome    = linhas.getString("NOME");
                 }
             }catch (Exception e) {
                 e.printStackTrace();
             }
        }        return nome;
        
    }
}
