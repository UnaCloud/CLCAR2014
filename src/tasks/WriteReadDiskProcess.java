package tasks;
import java.io.File;

import tasks.utils.DiskUtils;
public class WriteReadDiskProcess extends TaskProcess  {
    public WriteReadDiskProcess(int times,long size){
        super(size, times,true);
    }
    @Override
    public long doIOExecute(File file, File fileComp) {
        DiskUtils.writeComplexFile(file,size);
        DiskUtils.compressFile(file,fileComp);
        return 2;
    }
    @Override
    public long doExecute() {
        return 0;
    }
    @Override
    public void doOutput(long r, long time) {
        System.out.println("Lines compress: "+r+" in "+time+" ms");
    }
}