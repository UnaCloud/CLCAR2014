package tasks;

import java.io.File;

public class TaskProcessingProcess extends TaskProcess {
    public TaskProcessingProcess(int times,long size) {
        super(size, times,false);
    }
    @Override
    public long doExecute() {
        long result=0;
        for(long i = 2,c=0; i < size&&!terminado; i++,c=0){
            for (long j = 2; j < i; j++)if(i%j==0)c++;
            if(c==0)result++;
        }
        return result;
    }
    @Override
    public long doIOExecute(File file,File comp) {
        return 0;
    }

    @Override
    public void doOutput(long r, long time) {
        System.out.println("Primes found: "+r+" in "+time+" ms");
    }
    
}
