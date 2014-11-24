package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import server.dbos.TestDescription;
import tasks.TaskProcess;
import tasks.TaskProcessingProcess;
import tasks.TestType;
import unacloud.MyUserInfo;
import unacloud.Starter;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

public class Server {
	static PrintWriter pw;
    static String architecture;
    static JSch jsch=new JSch();
    static final int CORES=Runtime.getRuntime().availableProcessors();
    public static void main(String...args)throws Exception{
    	pw=new PrintWriter("results"+System.currentTimeMillis()+".txt");
    	architecture = args[0];
    	pw.println(architecture+" "+CORES);
    	if(args.length>1){
    		int vms=Integer.parseInt(args[1]);
    		doTest(vms);
    	}else{
    		for(int vms=0;vms<=CORES;vms++){
        		doTest(vms);
        		try {
					Thread.sleep(60000*2);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
    	}
    	pw.close();
    }
    static void doTest(int vms){
    	String[] ips=new String[0];
    	if(vms>0){
    		ips=Starter.startVMs(vms);
        	for(int e=0;e<vms;e++){
        		try {
    				Session session=jsch.getSession("root", ips[e], 22);
    				UserInfo ui=new MyUserInfo();
    				session.setUserInfo(ui);
    				session.connect();
    				Channel channel=session.openChannel("exec");
    				((ChannelExec)channel).setCommand("rm clcar2014client.jar;/usr/bin/wget http://157.253.204.10/clcar2014client.jar;/usr/bin/nohup java -jar clcar2014client.jar");
    				channel.setInputStream(null);
    				channel.connect();
    				try(BufferedReader br=new BufferedReader(new InputStreamReader(channel.getExtInputStream()))){
    					for(String h;(h=br.readLine())!=null;){
    						if(h.contains("saved"))break;
    					}
    				}
    				channel.getExitStatus();
    				channel.disconnect();
    				session.disconnect();
    			} catch (Exception e1) {
    				e1.printStackTrace();
    			}
        	}
    	}
    	doTests(ips);
    	if(vms>0)Starter.stopVMs(vms);
    }
    static void doTests(String[] ips){
    	pw.println("VMs: "+ips.length);
    	System.out.println("doTests");
    	try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	Socket[] ss=new Socket[ips.length];
    	for(int e=0;e<ips.length;e++){
    		TestDescription test=new TestDescription();
    		test.type=TestType.PROCESS;
    		test.threads=1;
    		test.times=2000;
    		test.size=test.getVmSize();
    		try {
				ss[e]=new Socket(ips[e],80);
				try(ObjectOutputStream oos=new ObjectOutputStream(ss[e].getOutputStream());){
	    			oos.writeObject(test);
	    			oos.flush();
	    		}
			} catch (Exception e2) {
				System.out.println("Error conentandose a "+ips[e]+": "+e2.getMessage());
			}
    	}
    	System.out.println("Ejecutando");
    	for(int pmcores=1;pmcores<=CORES;pmcores++){
    		pw.println();
    		pw.println("cores: "+pmcores);
    		TaskProcess[] hilos=new TaskProcess[pmcores];
        	for(int e=0;e<pmcores;e++)hilos[e]=new TaskProcessingProcess(24,110000l);
        	for(int e=0;e<pmcores;e++)hilos[e].start();
        	for(int e=0;e<pmcores;e++)
    			try {
    				hilos[e].join();
    			} catch (InterruptedException e1) {
    				e1.printStackTrace();
    			}
        	for(int e=0;e<pmcores;e++)pw.println("Hilo "+e+":"+hilos[e].getOutput());
        	try {
    			Thread.sleep(10000);
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    	}
    	System.out.println("Terminando");
    	for(int e=0;e<ips.length;e++){
    		TestDescription test=new TestDescription();
    		test.type=TestType.STOP;
    		try(Socket s=new Socket(ips[e],80);ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());){
    			oos.writeObject(test);
    			oos.flush();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
    	}
    	System.out.println("Leyendo resultados");
    	pw.println();pw.println("VM results:");
    	for(int e=0;e<ips.length;e++){
    		try(Socket s=ss[e];BufferedReader br=new BufferedReader(new InputStreamReader(s.getInputStream()))){
    			pw.println(br.readLine());
    		}catch (Exception e2) {
				e2.printStackTrace();
			}
    	}
    	System.out.println("Pruebas terminadas");
    }
    
}