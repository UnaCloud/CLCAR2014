package temp;

public enum Arquitectura {
	e6300("e6300"),e7600("e7600"),i5_660("i5-660"),i7_4770("i7-4770"),AMD5000plus("amd"),i7_2600("i7-2600"),
	i7_4770_NTB("i7-4770-ntb"),i7_4770_NADA("i7-4770-nada");
	String filename;
	private Arquitectura(String filename) {
		this.filename = filename;
	}
}
