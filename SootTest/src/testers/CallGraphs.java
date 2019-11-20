package testers;

public class CallGraphs
{

	static void m1() {}
	static void m2() {}
	static {
		// System.out.println("clinit");
		m1();
	}
	public static void C() {
		// System.out.println("init");
		m2();
	}
       public static void main(String[] args) {
//              doStuff();
    	   p();
//    	   C();
//    	   q();
//    	   java.util.Map arg = new java.util.TreeMap();
//    	   java.util.Map obj = m(arg);
//    	   test1();
       }
       
     static java.util.Map m(java.util.Map m) {
   		return m;
   		// WHAT IS THE TYpe of "m"? -- NOT Map, HashMap, LinkedHashMap, TreeMap... BUT only TreeMap
   	}
   	static void p() {
   		java.util.Map p = m(new java.util.TreeMap());
   		p = m(new java.util.HashMap());
           p.entrySet().iterator();
   		// WHAT IS THE TYPE of "p"?
   	}
//   	static void q() {
//   		java.util.Map q = m(new java.util.TreeMap());
//   		// WHAT IS THE TYPE of "q"?
//   	}
////       public static void test1() {
////    	      foobar();
////    	  }
////    	   void test2() {}
////    	public static void foobar() {
////    	       new java.util.HashSet().iterator();
////    	  } 
////       public static void doStuff() {
////              new A().foo();
////       }
//}
//
// 
////class A
////{
////       public void foo() {
////              bar();
////       }
////       
////       public void bar() {
////       }
}