package br.com.battlebits.ybattlecraft.constructors;

import java.util.ArrayList;
import java.util.UUID;

import br.com.battlebits.ybattlecraft.enums.ReportStatus;

public class Report {

	private UUID reported;
	private ArrayList<String> reasons;
	private ArrayList<String> reporters;
	private Long expire;
	private ReportStatus status;

	public Report(UUID reported) {
		this.reported = reported;
		this.reporters = new ArrayList<>();
		this.reasons = new ArrayList<>();
		this.expire = System.currentTimeMillis() + 3600000;
		this.status = ReportStatus.OPEN;
	}

	public UUID getReported() {
		return reported;
	}

	public ArrayList<String> getReasons() {
		return reasons;
	}

	public ArrayList<String> getReporters() {
		return reporters;
	}

	public Long getExpire() {
		return expire;
	}

	public void updateExpire() {
		this.expire = System.currentTimeMillis() + 3600000;
	}

	public ReportStatus getStatus() {
		return status;
	}

	public void setStatus(ReportStatus status) {
		this.status = status;
	}

}
