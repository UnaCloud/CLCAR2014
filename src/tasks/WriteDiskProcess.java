package tasks;

import java.io.File;

import tasks.utils.DiskUtils;

public class WriteDiskProcess extends TaskProcess {
    public WriteDiskProcess(int times,long size){
        super(size,times,true);
    }
    @Override
    public long doExecute() {
        return -1;
    }
    @Override
    public long doIOExecute(File file,File comp) {
        return DiskUtils.writeSimpleFile(file,size);
    }
    @Override
    public void doOutput(long r, long time) {
        System.out.println("Lines write: "+r+" in "+time+" ms");
    }
    
}