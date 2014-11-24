package tasks;
import java.io.File;

import tasks.utils.DiskUtils;
public abstract class TaskProcess extends Thread{
    
    long size;
    int times;
    long[] processRealDurations;
    public boolean isIO=false,terminado=false;
    public TaskProcess(long size, int times,boolean isIO) {
        this.size = size;
        this.times = times;
        this.processRealDurations = new long[times];
        this.isIO=isIO;
    }

    @Override
    public void run(){
        long r;
        if(!isIO){
            for(int e=0;e<times&&!terminado;e++){
                processRealDurations[e]=-System.currentTimeMillis();
                r=doExecute();
                processRealDurations[e] += System.currentTimeMillis();
                doOutput(r,processRealDurations[e]);
            }
        }else{
            for(int e=0;e<times&&!terminado;e++){
                File out=DiskUtils.getTempWriteFile(),comp=DiskUtils.getTempWriteFile();
                processRealDurations[e]=-System.currentTimeMillis();
                r=doIOExecute(out,comp);
                processRealDurations[e] += System.currentTimeMillis();
                out.delete();comp.delete();
                doOutput(r,processRealDurations[e]);
            }
        }
        
    }
    
    public abstract long doExecute();
    public abstract void doOutput(long r,long time);
    public abstract long doIOExecute(File file,File fileComp);
    public String getOutput(){
        String r=null;
        for(long l:processRealDurations)r=((r==null)?"":(r+" "))+l;
        return r==null?"":r;
    }
}
