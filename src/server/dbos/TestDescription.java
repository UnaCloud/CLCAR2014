package server.dbos;

import java.io.Serializable;

import tasks.TestType;

public class TestDescription implements Serializable{
	private static final long serialVersionUID = 5021512753320172822L;
	public TestType type;
	public long size;
	public int threads,times;
	public long getVmSize(){
        if(type==TestType.PROCESS)return 130000l;
        if(type==TestType.WRITE)return 1000l*1000l*1000l;
        return 1000l*1000l*1000l;
    }
    public long getPmSize(){
    	if(type==TestType.PROCESS)return 110000l;
    	if(type==TestType.WRITE)return 8l*1000l*1000l*1000l;
        return 2l*1000l*1000l*1000l;
    }
}
