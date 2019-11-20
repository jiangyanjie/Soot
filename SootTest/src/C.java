
public class C {
	static void m1() {}
	static void m2() {}
	static {
		// System.out.println("clinit");
		m1();
	}
	public C() {
		// System.out.println("init");
		m2();
	}
	public static void main(String[] a) {
		
	}
}
