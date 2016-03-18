package br.com.battlebits.ybattlecraft.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import br.com.battlebits.ybattlecraft.yBattleCraft;

public class Connect {
	private yBattleCraft m;

	public Connect(yBattleCraft m) {
		this.m = m;
	}

	public synchronized Connection trySQLConnection() {
		if (!m.sql) {
			m.getLogger().info("MySQL Desativado!");
			return null;
		}
		try {
			m.getLogger().info("Conectando ao MySQL");
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String conn = "jdbc:mysql://" + m.host + ":" + m.port + "/status";
			m.mainConnection = DriverManager.getConnection(conn, m.user, m.password);
			return m.mainConnection;
		} catch (ClassNotFoundException ex) {
			m.getLogger().warning("MySQL Driver nao encontrado!");
			m.sql = false;
		} catch (SQLException ex) {
			m.getLogger().warning("Erro enquanto tentava conectar ao Mysql!");
			m.sql = false;
		} catch (Exception ex) {
			m.getLogger().warning("Erro desconhecido enquanto tentava conectar ao MySQL.");
			m.sql = false;
		}
		return null;
	}

	public void prepareSQL(Connection con) {
		if (m.sql) {
			SQLQuery("CREATE TABLE IF NOT EXISTS `Status` (`ID` int(10) unsigned NOT NULL AUTO_INCREMENT, `Uuid` varchar(255) NOT NULL, `Kills` int(10), `Deaths` int(10), `Killstreak` int(10), `Money` int(10), PRIMARY KEY (`ID`)) ENGINE=InnoDB DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1 ;", con);
			SQLQuery("CREATE TABLE IF NOT EXISTS `Kits` (`ID` int(10) unsigned NOT NULL AUTO_INCREMENT, `PlayerName` varchar(255) NOT NULL, `KitName` varchar(255) NOT NULL, PRIMARY KEY (`ID`)) ENGINE=InnoDB DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1 ;", con);
			SQLQuery("CREATE TABLE IF NOT EXISTS `KitFavorito` (`ID` int(10) unsigned NOT NULL AUTO_INCREMENT, `Uuid` varchar(255) NOT NULL, `Kits` text NOT NULL, PRIMARY KEY (`ID`)) ENGINE=InnoDB DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1 ;", con);
			m.getLogger().info("Criando Tabelas no SQL");
		}
	}

	public static void SQLdisconnect(Connection con) {
		try {
			if (con != null && !con.isClosed()) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public synchronized void SQLQuery(String sql, Connection con) {
		if (!m.sql)
			return;
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			m.getLogger().info("Erro ao tentar executar Query");
			m.getLogger().info(e.getMessage());
		}
	}
}
