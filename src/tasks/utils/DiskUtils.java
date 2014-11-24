/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tasks.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author Asistente
 */
public class DiskUtils {
    private static volatile int file=0;
    public synchronized static File getTempWriteFile(){
        File f=new File("./testWriteFile"+file+++".in");
        f.deleteOnExit();
        return f;
    }
    /*
     * Read a file f to the disk
     * return the line number read
     */
    public static long readFile(File f,long size){
        long readLineNumber = 0,readedSize=0;
        byte[] buffer=new byte[10*1024];
        try(FileInputStream fis=new FileInputStream(f)){
            for(int n=0;readedSize<size&&(n=fis.read(buffer))>0;){
                readedSize+=n;
                readLineNumber++;
            }
        }catch (IOException ex) {
            Logger.getLogger(tasks.TaskProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return readLineNumber;
    }

    public static int writeSimpleFile(File f, long sizeBytes){
        int linesWritten=0;
        final String lineWritten = "abcdefghijklmnopqrstuvwxyz12345\n";
        try(Writer output = new BufferedWriter(new FileWriter(f))){
            for(long i=0,l=lineWritten.length();i<sizeBytes;i+=l,linesWritten++)output.write(lineWritten);
        }
        catch(IOException e){
            System.out.println("Error opening the file: "+e.toString());
        }
        return linesWritten;
    }

    public static void writeComplexFile(File f, long sizeBytes){
        long fileSize = 0;
        Random r=new Random();
        try(Writer output = new BufferedWriter(new FileWriter(f));){
            while(fileSize<sizeBytes){
                String lineWritten = ""+r.nextDouble();
                output.write(lineWritten);
                fileSize += lineWritten.length();
            }
        }catch (IOException ex) {
            Logger.getLogger(tasks.TaskProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
     * Compress a source file (sourceFile) to a destination file (targetFile)
     */
    public static void compressFile(File sourceFile, File targetFile){
        try(ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(targetFile));FileInputStream fis = new FileInputStream(sourceFile)){
            zos.putNextEntry(new ZipEntry(sourceFile.getAbsolutePath()));
            byte[] buffer = new byte[1024*1024];
            for(int size;(size = fis.read(buffer, 0, buffer.length)) > 0;) {
                zos.write(buffer, 0, size);
            }
            zos.closeEntry();
        } catch (IOException ex) {
            Logger.getLogger(tasks.TaskProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}