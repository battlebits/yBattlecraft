package br.com.battlebits.ybattlecraft.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import br.com.battlebits.ybattlecraft.Battlecraft;

public class Config {
	private static Battlecraft m;

	public Config(Battlecraft Battlecraft) {
		m = Battlecraft;
	}

	public void loadConfig() {
		for (ConfigEnum c : ConfigEnum.values()) {
			File config = new File(m.getDataFolder(), c.getFile());
			File dataFolder = m.getDataFolder();
			if (!dataFolder.exists()) {
				dataFolder.mkdir();
			}
			if (!config.exists()) {
				config.getParentFile().mkdirs();
				copy(m.getResource(c.getFile()), config);
			}
		}
	}

	public FileConfiguration getConfig(ConfigEnum enume) {
		return YamlConfiguration.loadConfiguration(new File(m.getDataFolder(),
				enume.getFile()));
	}

	private void copy(InputStream in, File file) {
		try {
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
