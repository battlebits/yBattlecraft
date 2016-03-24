package br.com.battlebits.ybattlecraft.util;

public class NovaDirection {

	private double pitch;
	private double yaw;

	public NovaDirection(double pitch, double yaw) {
		this.pitch = pitch;
		this.yaw = yaw;
	}

	public double getPitch() {
		return pitch;
	}

	public double getYaw() {
		return yaw;
	}

}
