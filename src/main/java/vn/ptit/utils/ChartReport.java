package vn.ptit.utils;

import java.math.BigDecimal;

public class ChartReport {
	private String label[];
	private double data[];

	public double[] getData() {
		return data;
	}

	public void setData(double[] data) {
		this.data = data;
	}

	public ChartReport() {
		// TODO Auto-generated constructor stub
	}

	public String[] getLabel() {
		return label;
	}

	public void setLabel(String[] label) {
		this.label = label;
	}

	public ChartReport(String[] label, double[] data) {
		super();
		this.label = label;
		this.data = data;
	}

}
