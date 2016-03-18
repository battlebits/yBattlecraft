package br.com.battlebits.ybattlecraft.util;

public class TimeFormater {

	public String formatToMinutesAndSeconds(int i) {
		String str = "";
		if (i >= 60) {
			int m = i / 60;
			int s = i % 60;
			if (m > 0) {
				if (m == 1) {
					str = "1 minuto";
				} else {
					str = m + " minutos";
				}
			}
			if (s > 0) {
				if (s == 1) {
					if (str.isEmpty()) {
						str = "1 segundo";
					} else {
						str = str + " e 1 segundo";
					}
				} else if (str.isEmpty()) {
					str = s + " segundos";
				} else {
					str = str + " e " + s + " segundos";
				}
			}
		} else if (i <= 1) {
			str = "1 segundo";
		} else {
			str = i + " segundos";
		}
		return str;
	}

}
