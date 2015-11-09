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
 * Classe Imposto - NCM e Aliquotas de Impostos
 */

public class Imposto {
    /*
     * Definição dos Atributos
     */
    private int 	id;
    private String 	ncm;
    private String 	descricao;
    private Double 	aliquota;

    /*
    Gets e Sets
    */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNcm() {
        return ncm;
    }

    public void setNcm(String ncm) {
        this.ncm = ncm;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getAliquota() {
        return aliquota;
    }

    public void setAliquota(Double aliquota) {
        this.aliquota = aliquota;
    }

    /*
     * Atributos de uso nas Operações do Banco
     */
    boolean resultadoOperacao = false;
    String stringDeConexao = "jdbc:sqlite:db/iclothe.db";
	
    /*
     * Método menuImposto
     */
    public void menuImposto() throws SQLException {
    int opc = 1;
    Scanner scanner = new Scanner(System.in);
    String valorTela;

    while (opc != 0) {

        System.out.println("================================================");
        System.out.println(" iClothe - Cadastro de NCMs                     ");
        System.out.println("================================================");
        System.out.println("1 - Listar NCM");
        System.out.println("2 - Cadastrar NCM");
        System.out.println("3 - Editar NCM");
        System.out.println("0 - Voltar");
        System.out.print("Qual a sua opção: ");

        valorTela = scanner.nextLine();
        opc = Integer.parseInt(valorTela);

        if (opc == 1) {
            /*
            Método listarNCM()
            */
            listarNCM();
        }

        if (opc == 2) {
            /*
            Método inserirNCM()
            */
            inserirNCM();
        }

        if (opc == 3) {
            /*
            Método editarNCM()
            */
            editarNCM();
        }

    }

}

    /*
     * Metodo inserirNCM() - Insere NCM e aliquota de cálculo de impostos
     */
    public void inserirNCM() throws SQLException {

        boolean jaexiste = false;

        Scanner scanner = new Scanner(System.in);
        Imposto imposto = new Imposto();

        System.out.println("==========================================");
        System.out.println("Cadastrar Novo NCM                        ");
        System.out.println("------------------------------------------");
        System.out.print("Digite o NCM: ");
        imposto.setNcm(scanner.nextLine());
        System.out.print("Descricao do NCM: ");
        imposto.setDescricao(scanner.nextLine());
        System.out.print("Aliquota de Impostos (formato 00.00): ");
        imposto.setAliquota(Double.parseDouble(scanner.nextLine()));

        /*
         * Prepara para inserir no Banco de Dados
         */

        /*
         * Verifica se o NCM já está cadastrado
         * 
         */
        Connection conexao = DriverManager.getConnection(stringDeConexao);
        try {

            PreparedStatement comando = conexao.prepareStatement("SELECT NCM FROM IMPOSTOS WHERE NCM = ?");
            comando.setString(1, imposto.getNcm());
            ResultSet linhas = comando.executeQuery();

            if (linhas.next()) {
                jaexiste = true;
                System.out.println("NCM já cadastrado!");
            }
        }catch (Exception e) {
           e.printStackTrace();
        }

        /*
         * Se não estiver, cadastra
         * 
         */

        if(!jaexiste) {
            try {
                PreparedStatement comando = conexao.prepareStatement("INSERT INTO IMPOSTOS (NCM, DESCRICAO, ALIQUOTA) "
                + "VALUES (?, ?, ?)");
                comando.setString(1, imposto.getNcm());
                comando.setString(2, imposto.getDescricao());
                comando.setDouble(3, imposto.getAliquota());
                comando.execute();
                
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexao.close();
            }

        }

    }

    /*
     * Metodo listarNCM() - Lista os NCMs cadastrados
     */
    public void listarNCM() throws SQLException {

        Connection conexao = DriverManager.getConnection(stringDeConexao);
        try {
            PreparedStatement comando = conexao.prepareStatement("SELECT ID, NCM, DESCRICAO, ALIQUOTA "
                + "FROM IMPOSTOS ORDER BY NCM");
            ResultSet resultado = comando.executeQuery();
            System.out.println("===========================================================================");
            System.out.println("Relação de NCMs                                                            ");
            System.out.println("---------------------------------------------------------------------------");
            System.out.println("ID  NCM      DESCRICAO                                             ALIQUOTA");

            System.out.println("---------------------------------------------------------------------------");

            while (resultado.next()) {
                /*
                 * Caracteres de Formatação - Ex: %-15s - 15 posições, alinhado a esquerda
                 */
                System.out.printf("%-4s"	, resultado.getInt("ID"));
                System.out.printf("%-9s"	, resultado.getString("NCM"));
                System.out.printf("%-55s"	, resultado.getString("DESCRICAO"));
                System.out.printf("%-10s"	, resultado.getDouble("ALIQUOTA"));
                System.out.println("");
            }
            System.out.println("---------------------------------------------------------------------------");

        } catch (Exception e) {
           e.printStackTrace();
        } finally {
            conexao.close();
        }

    }

    public void editarNCM() throws SQLException {
        /*
        * Atributos do NCM selecionado
        */
        String 	descricaoAntiga    = null;
        Double 	aliquotaAntiga     = null;

        boolean ncmEncontrado       = false;

        Scanner scanner = new Scanner(System.in);
        Imposto imposto = new Imposto();

        System.out.println("==========================================");
        System.out.println("Editar NCM                                ");
        System.out.println("------------------------------------------");
        System.out.print("Qual é o NCM?: ");
        imposto.setNcm(scanner.nextLine());

        /*
        Verifica se o NCM já está cadastrado
        */
        Connection conexao = DriverManager.getConnection(stringDeConexao);
        try {

            PreparedStatement comando = conexao.prepareStatement("SELECT ID, DESCRICAO, ALIQUOTA FROM IMPOSTOS WHERE NCM = ?");
            comando.setString(1, imposto.getNcm());
            ResultSet linhas = comando.executeQuery();

            if (linhas.next()) {
                imposto.setId(linhas.getInt("ID"));
                descricaoAntiga         = linhas.getString("DESCRICAO");
                aliquotaAntiga		= linhas.getDouble("ALIQUOTA");
                ncmEncontrado           = true;

            }else{
                System.out.println("NCM " + imposto.getNcm() + " nao foi encontrado!");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        /*
        Se o NCM foi encontrado ele pode ser editado
        */
        if(ncmEncontrado) {

            System.out.println("Descricao: " + descricaoAntiga);
            System.out.print("NOVA descricao (ENTER - mantem a mesma):");
            /*
            Se a digitacao for em branco, a nova variavel mantem o valor da antiga
            */
            imposto.setDescricao(scanner.nextLine());
            if(imposto.getDescricao().isEmpty()) {
                imposto.setDescricao(descricaoAntiga);
            }

            System.out.println("Aliquota: " + aliquotaAntiga);
            System.out.print("NOVA aliquota - Formato 00.00:");
            imposto.setAliquota(Double.parseDouble(scanner.nextLine()));

            try {
                PreparedStatement comando = conexao.prepareStatement("UPDATE IMPOSTOS SET DESCRICAO = ? "
                    + "ALIQUOTA = ? WHERE ID = ?)");
                comando.setString(1, imposto.getDescricao());
                comando.setDouble(2, imposto.getAliquota());
                comando.setInt(3, imposto.getId());

                comando.execute();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexao.close();
            }
        }
    }
    
    /*
    Método que verifica se o NCM existe, utilizado no cadastro de produtos
    */
    public boolean verificaNCM(String ncm) throws SQLException {
        boolean ncmEncontrado = false;
         Connection conexao = DriverManager.getConnection(stringDeConexao);
	try {
            PreparedStatement comando = conexao.prepareStatement("SELECT NCM FROM IMPOSTOS WHERE NCM =  ?");
            comando.setString(1, ncm);
            ResultSet resultado = comando.executeQuery();

            if (resultado.next()) {
            	ncmEncontrado = true;
            } else {
		ncmEncontrado = false;
            }

	} catch (Exception e) {
            e.printStackTrace();
	} finally {
            conexao.close();
	}
        return ncmEncontrado;
    }
    
    /*
    Método calculaImposto - Retorna o valor do imposto calculo 
    */
    public double calculaImposto(Double valor, String ncm) throws SQLException {
        double valorImposto = 0;
        double aliquota     = 0.1;
        
        Connection conexao = DriverManager.getConnection(stringDeConexao);
	try {
            PreparedStatement comando = conexao.prepareStatement("SELECT ALIQUOTA FROM IMPOSTOS WHERE NCM =  ?");
            comando.setString(1, ncm);
            ResultSet resultado = comando.executeQuery();

            if (resultado.next()) {
            	aliquota = resultado.getDouble("ALIQUOTA");
            } 

	} catch (Exception e) {
            e.printStackTrace();
	} 
        
        valorImposto = (valor/100) * aliquota;
        conexao.close();
        return valorImposto;
    }

}
