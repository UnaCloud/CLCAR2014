import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;


public class Temp {

	static Map<String,Integer> map=new TreeMap<>();
	public static void main(String[] args) throws Exception{
		try(BufferedReader br=new BufferedReader(new FileReader("C:\\Users\\G\\Desktop\\T1.txt"))){
			for(String h;(h=br.readLine())!=null;){
				Integer n=map.get(h);
				if(n==null)n=0;
				n=n+1;
				map.put(h,n);
			}
		}
		try(BufferedReader br=new BufferedReader(new FileReader("C:\\Users\\G\\Desktop\\T2.txt"))){
			for(String h;(h=br.readLine())!=null;){
				Integer n=map.get(h);
				if(n==null)n=0;
				n=n-1;
				map.put(h,n);
			}
		}
		for(Entry<String, Integer> e:map.entrySet())if(e.getValue()!=0){
			System.out.println(e.getKey());
		}
	}
}
