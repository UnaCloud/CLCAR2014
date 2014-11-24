

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class GeneradorInformeCLCAR {
	static List<ResultadoPrueba> resultados=new ArrayList<ResultadoPrueba>();
	static Map<String,Long> baseTimes=new TreeMap<>();
	static Map<String,List<ResultadoPrueba>> baseResults=new TreeMap<>();
	static Map<String[],List<ResultadoPrueba>> grupos=new TreeMap<String[], List<ResultadoPrueba>>(new Comparator<String[]>() {
		@Override
		public int compare(String[] o1, String[] o2) {
			if(o1[0].compareTo(o2[0])!=0)return o1[0].compareTo(o2[0]);
			return o1[1].compareTo(o2[1]);
		}
	});
	public static void main(String[] args)throws Exception {
		DecimalFormat df=new DecimalFormat("0.000000");
		DecimalFormatSymbols dfs=new DecimalFormatSymbols();
		dfs.setDecimalSeparator(',');
		df.setDecimalFormatSymbols(dfs);
		cargarPruebas();
		String ant="";
		for(ResultadoPrueba p:resultados){
			List<ResultadoPrueba> g=grupos.get(p.getKey());
			if(g==null)grupos.put(p.getKey(),g=new ArrayList<>());
			String act=p.arc+p.hypervisor;
			if(!act.equals(ant))g.addAll(baseResults.get(p.arc));
			g.add(p);
			ant=act;
		}
		for(Entry<String[],List<ResultadoPrueba>> g:grupos.entrySet()){
			for(int e=1;e<=8;e++){
				String head=g.getKey()[0]+"\t"+g.getKey()[1]+"\t"+e;
				StringBuilder sb=new StringBuilder(head);
				//for(ResultadoPrueba rp:g.getValue())if(rp.threads==e)sb.append("\t"+df.format(rp.getImpact()));
				for(ResultadoPrueba rp:g.getValue())if(rp.threads==e)sb.append("\t"+rp.getMeanTime());
				if(sb.length()!=head.length())System.out.println(sb);
			}
		}
	}
	static void cargarPruebas()throws Exception{
		for(File f:new File("ResultadosCLCAR2014").listFiles())if(f.getName().endsWith(".txt")){
			try(BufferedReader br=new BufferedReader(new FileReader(f))){
				String cpu = br.readLine().split(" ")[0];
				int vms = Integer.parseInt(br.readLine().split(" ")[1]);
				for(String h,j[];(h=br.readLine())!=null;){
					if(h.startsWith("cores: ")){
						int cores=Integer.parseInt(h.split(" ")[1]);
						List<Long[]> times=new LinkedList<>();
						for(int e=0;e<cores;e++){
							j=br.readLine().split(" |:");
							Long[] ts=new Long[j.length-2];
							for(int i=2;i<j.length;i++)ts[i-2]=Long.parseLong(j[i]);
							times.add(ts);
						}
						resultados.add(new ResultadoPrueba(f.getName().split("-")[0], cpu, vms, cores, times.toArray(new Long[0][])));
					}
				}
			} 
		}
		ListIterator<ResultadoPrueba> it=resultados.listIterator();
		while(it.hasNext()){
			ResultadoPrueba p=it.next();
			p.limpiarResultados();
			if(p.vms==0){
				if(p.threads==1)baseTimes.put(p.arc,p.getMeanTime());
				List<ResultadoPrueba> l=baseResults.get(p.arc);
				if(l==null)baseResults.put(p.arc,l=new ArrayList<>());
				l.add(p);
				it.remove();
			}
		}
		Collections.sort(resultados);
	}
	static class ResultadoPrueba implements Comparable<ResultadoPrueba>{
		String hypervisor,arc;
		int vms,threads;
		/**
		 * Listado de tiempos de ejecución por core
		 */
		Long[][] times;
		public ResultadoPrueba(String hypervisor, String arc, int vms,int threads, Long[][] times) {
			this.hypervisor = hypervisor;
			this.arc = arc;
			this.vms = vms;
			this.threads = threads;
			this.times=times;
		}
		public String toString(){
			return hypervisor+"\t"+arc+"\t"+vms+"\t"+threads+"\t"+getImpact();
		}
		double getImpact(){
			double baseTime=baseTimes.get(arc);
			return ((double)getMeanTime()-baseTime)/baseTime;
		}
		long getMeanTime(){
			long sum=0,tot=0;
			for(Long[] ts:times){
				for(long t:ts)sum+=t;
				tot+=ts.length;
			}
			return sum/tot;
		}
		static int TO_CLEAR=2;
		void limpiarResultados(){
			Long[][] newTimes=new Long[times.length][];
			for(int e=0;e<times.length;e++){
				long mean=0;
				for(long t:times[e])mean+=t;
				mean/=times[e].length;
				Arrays.sort(times[e],new MeanComparator(mean));
				newTimes[e]=Arrays.copyOfRange(times[e],TO_CLEAR,times[e].length);
				mean=0;
				for(long t:newTimes[e])mean+=t;
				mean/=newTimes[e].length;
			}
			times=newTimes;
		}
		String[] getKey(){
			return new String[]{arc,hypervisor};
		}
		@Override
		public int compareTo(ResultadoPrueba arg0) {
			int a;
			if((a=arc.compareTo(arg0.arc))!=0)return a;
			if((a=hypervisor.compareTo(arg0.hypervisor))!=0)return a;
			if((a=Integer.compare(vms,arg0.vms))!=0)return a;
			if((a=Integer.compare(threads,arg0.threads))!=0)return a;
			return 0;
		}
	}
	static class MeanComparator implements Comparator<Long>{
		long mean;
		public MeanComparator(long mean) {
			this.mean = mean;
		}
		@Override
		public int compare(Long o1, Long o2) {
			return Long.compare(Math.abs(mean-o2),Math.abs(mean-o1));
		}
		
	}
}
