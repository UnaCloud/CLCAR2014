package unacloud;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import communication.messages.vmo.VirtualMachineStartMessage;
import communication.messages.vmo.VirtualMachineStopMessage;

public class Starter {
	static String ips[],mask;
	static int imageId;
	private static void load(){
		List<String> arr=new LinkedList<>();
		try(BufferedReader br=new BufferedReader(new FileReader("ips.txt"))){
			imageId=Integer.parseInt(br.readLine());
			mask=br.readLine();
			for(String h;(h=br.readLine())!=null;)if(!h.isEmpty())arr.add(h);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ips=arr.toArray(new String[0]);
	}
	public static String[] startVMs(final int n){
		load();
		String[] usedIps=new String[n];
		for(int e=0;e<n;e++){
			VirtualMachineStartMessage vmsm=new VirtualMachineStartMessage();
			vmsm.setExecutionTime(24);
			vmsm.setHostname("vm"+n);
			vmsm.setVirtualMachineIP(usedIps[e]=ips[e]);
			vmsm.setVirtualMachineNetMask(mask);
			vmsm.setVmCores(1);
			vmsm.setVmMemory(1024);
			vmsm.setVirtualMachineExecutionId(1000+e);
 			vmsm.setVirtualMachineImageId(imageId);
			System.out.println("enviando "+e);
			try(Socket s=new Socket("localhost",81);ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());ObjectInputStream ois=new ObjectInputStream(s.getInputStream());){
				oos.writeObject(vmsm);
				oos.flush();
				ois.readObject();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			System.out.println("enviado "+e);
			try {
				Thread.sleep(45000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		try{
    		Thread.sleep(3*60000);
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
		
		for(String h:usedIps){
			for(int e=0;e<10;e++){
				try(Socket s=new Socket(h,22)){
					break;
				}catch (Exception ex) {
					try{
			    		Thread.sleep(60000);
			    	}catch(Exception x){
			    		ex.printStackTrace();
			    	}
				}
			}
			
		}
		System.out.println("Maquinas levantadas");
		return usedIps;
	}
	public static void stopVMs(final int n){
		for(int e=0;e<n;e++){
			VirtualMachineStopMessage vmsm=new VirtualMachineStopMessage();
			vmsm.setVirtualMachineExecutionId(1000+e);
			try(Socket s=new Socket("localhost",81);ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());ObjectInputStream ois=new ObjectInputStream(s.getInputStream());){
				oos.writeObject(vmsm);
				oos.flush();
				ois.readObject();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		startVMs(Integer.parseInt(args[0]));
	}
}
