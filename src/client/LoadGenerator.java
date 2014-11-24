package client;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import server.dbos.TestDescription;
import tasks.ReadDiskProcess;
import tasks.TaskProcess;
import tasks.TaskProcessingProcess;
import tasks.TestType;
import tasks.WriteDiskProcess;
import tasks.WriteReadDiskProcess;
public class LoadGenerator {
    static TaskProcess hilos[];
    public static void main(String[] args)throws Exception{
    	System.out.println("EXIT");
        try(ServerSocket ss=new ServerSocket(80)){
        	while(true){
        		try(Socket s=ss.accept();ObjectInputStream ois=new ObjectInputStream(s.getInputStream());PrintWriter pw=new PrintWriter(s.getOutputStream())){
        			attendQuery((TestDescription)ois.readObject(),pw);
        		}
        	}
        }
    }
    public static void attendQuery(final TestDescription test,final PrintWriter pw){
        new Thread(){
            @Override
            public void run(){
                processQuery(test, pw);
            }
        }.start();
    }
    public static void processQuery(TestDescription test,PrintWriter pw){
        if(test.type==TestType.EXIT)System.exit(0);
        else if(test.type==TestType.STOP){
            if(hilos!=null){
                for(int e=0;e<hilos.length;e++){
                	hilos[e].terminado=true;
                    hilos[e].interrupt();
                }
                System.out.println("Interrumpidos");
            }
            return;
        }
        else if(hilos!=null)return;
        hilos=new TaskProcess[test.threads];
        switch(test.type){
            case PROCESS:
                for(int e=0;e<hilos.length;e++)hilos[e]=new TaskProcessingProcess(test.times,test.size);
                break;
            case WRITE:
                for(int e=0;e<hilos.length;e++)hilos[e]=new WriteDiskProcess(test.times,test.size);
                break;
            case READ:
                for(int e=0;e<hilos.length;e++)hilos[e]=new ReadDiskProcess(test.times,test.size);
                break;
            case WRITEREAD:
                for(int e=0;e<hilos.length;e++)hilos[e]=new WriteReadDiskProcess(test.times,test.size);
                break;
            default:
            	break;
        }
        for(int e=0;e<hilos.length;e++)hilos[e].start();
        for(int e=0;e<hilos.length;e++)try {
            hilos[e].join();
        } catch (InterruptedException ex) {
        }
        for(int e=0;e<hilos.length;e++)pw.println("hilo "+e+" "+hilos[e].getOutput());
        hilos=null;
    }
}
