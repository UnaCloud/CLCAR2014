package tasks;
import java.io.File;

import tasks.utils.DiskUtils;
public class ReadDiskProcess extends TaskProcess{
    protected File readFile=new File("./10G.in");
    public ReadDiskProcess(int times,long size){
        super(size, times,false);
    }
    @Override
    public long doExecute() {
        return DiskUtils.readFile(this.readFile,size);
    }
    @Override
    public long doIOExecute(File file,File comp) {
        return DiskUtils.readFile(this.readFile,size);
    }

    @Override
    public void doOutput(long r,long time) {
        System.out.println("Lines read: "+r+" in "+time+" ms");
    }
    
}