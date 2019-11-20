import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soot.MethodOrMethodContext;
import soot.PackManager;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootMethod;
import soot.Transform;
import soot.jimple.spark.SparkTransformer;
//import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.Targets;
import soot.options.Options;
import soot.util.dot.DotGraph;
import soot.util.queue.QueueReader;
public class Main {

public static void serializeCallGraph(CallGraph graph, String fileName) {
		
//		String className = "testers.CallGraphs";
		
		if (fileName == null) {
			fileName = soot.SourceLocator.v().getOutputDir();
			if (fileName.length() > 0) {
				fileName = fileName + java.io.File.separator;
			}
			fileName = fileName + "call-graph" + DotGraph.DOT_EXTENSION;
		}
		DotGraph canvas = new DotGraph("call-graph");
		QueueReader<Edge> listener = graph.listener();
		while (listener.hasNext()) {
			Edge next = listener.next();
			MethodOrMethodContext src = next.getSrc();
			MethodOrMethodContext tgt = next.getTgt();
			String srcString = src.toString();
			String tgtString = tgt.toString();
			
			if((!srcString.startsWith("<java.")&& !srcString.startsWith("<sun.") && !srcString.startsWith("<org.")&& !srcString.startsWith("<com.")&& !srcString.startsWith("<jdk.")&& !srcString.startsWith("<javax."))||(!tgtString.startsWith("<java.")&& !tgtString.startsWith("<sun.")&& !tgtString.startsWith("<org.")&&!tgtString.startsWith("<com.")&& !tgtString.startsWith("<jdk.")&&!tgtString.startsWith("<javax.")))
			{
				canvas.drawNode(src.toString());
				canvas.drawNode(tgt.toString());
				canvas.drawEdge(src.toString(), tgt.toString());
			}
		}
		canvas.plot(fileName);
		return ;
	}
	
	// copied from https://www.programcreek.com/java-api-examples/?api=soot.jimple.toolkits.callgraph.CallGraph
	public static void main(String[] args) {
		List<String> argsList = new ArrayList<String>();
		for (String s : args) {
			System.out.println(s);
		}
		System.out.println(args.length);
		
		   argsList.addAll(Arrays.asList(new String[]{

				   "-w", // this is called "dash double u"
				   "-pp",
				   /*"-main-class",
				   "testers.CallGraphs",//main-class*/
				   "-cp",
				   args[0],
				   args[1]
		   }));

		   String[] args2 = new String[argsList.size()];
		   args2= argsList.toArray(args2);
		   PackManager.v().getPack("wjtp").add(new Transform("wjtp.myTrans", new SceneTransformer() {

			@Override

			protected void internalTransform(String phaseName, Map options) {

			       //CHATransformer.v().transform();
				   HashMap<String, String> opt = new HashMap<>(options);
				   
				   opt.put("enabled","true");
				   opt.put("verbose","true");
				   opt.put("ignore-types","false");
				   opt.put("force-gc","false");
				   opt.put("pre-jimplify","false");
				   opt.put("vta","false");
				   opt.put("rta","false");
				   opt.put("field-based","false");
     			   opt.put("types-for-sites","false");
				   opt.put("merge-stringbuffer","true");
				   opt.put("string-constants","false");
				   opt.put("simulate-natives","true");
				   opt.put("simple-edges-bidirectional","false");
				   opt.put("on-fly-cg","true");
				   opt.put("simplify-offline","false");
				   opt.put("simplify-sccs","false");
				   opt.put("ignore-types-for-sccs","false");
				   opt.put("propagator","worklist");
				   opt.put("set-impl","double");
				   opt.put("double-set-old","hybrid");
				   opt.put("double-set-new","hybrid");
				   opt.put("dump-html","false");
				   opt.put("dump-pag","false");
				   opt.put("dump-solution","false");
				   opt.put("topo-sort","false");
				   opt.put("dump-types","true");
				   opt.put("class-method-var","true");
				   opt.put("dump-answer","false");
				   opt.put("add-tags","false");
				   opt.put("set-mass","false");
				   
				 
				   //test generating call graph for the whole application
				   Options.v().set_src_prec(Options.src_prec_java);
				   Options.v().set_whole_program(true);
				   Options.v().set_time(true);
				   Options.v().set_verbose(true);
				   Options.v().set_debug(true);
				   Options.v().set_debug_resolver(true);
				   Scene.v().loadBasicClasses();

				   /*String[] classnames = new String[1];
				   classnames[0] = "testers.CallGraphs";
				   for (String classname : classnames) {
				        SootClass cl = Scene.v().loadClassAndSupport(classname);
				        cl.setApplicationClass();
				   }*/

				   SparkTransformer.v().transform("", opt);

//	                       SootClass a = Scene.v().getSootClass("testers.A");
	                       //SootClass a = Scene.v().getSootClass("testers.CallGraphs");

			       //SootMethod src = Scene.v().getMainClass().getMethodByName("p");//doStuff
			       SootMethod src = Scene.v().getMainClass().getMethodByName("p");

			       CallGraph cg = Scene.v().getCallGraph();
			       serializeCallGraph(cg, "output" + DotGraph.DOT_EXTENSION);
			       Iterator<MethodOrMethodContext> targets = new Targets(cg.edgesOutOf(src));

			       while (targets.hasNext()) {

			           SootMethod tgt = (SootMethod)targets.next();

			           System.out.println(src + " may call " + tgt);

			       }

			}
		   }));
	           String[] newargs = argsList.toArray(new String[0]);
	           Options.v().parse(newargs);
	           SootClass c = Scene.v().forceResolve(args[1], SootClass.BODIES);
	           c.setApplicationClass();
	           Scene.v().loadNecessaryClasses();
	           SootMethod method = c.getMethodByName("p");
	           List<SootMethod> entryPoints = new ArrayList<>();
	           entryPoints.add(method);
	           Scene.v().setEntryPoints(entryPoints);
	           PackManager.v().runPacks();
               //soot.Main.main(args);
	           //PackManager.v().runPacks();
	           //System.out.println("Finished packing");

		}


	

}
