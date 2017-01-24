package at.ac.tuwien.sepm.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Used to transfer files out of an executing JAR-File to a destination in the computers filesystem.
 */
public class FileTransfer{

    /**
     * Will copy the given specified file to the specified destination and will overwrite an existing file by default.
     * The source-filename must be the files position inside the JAR-file (e.g. "/sql/create.sql"), the destination must be an absolute path to the executing computers filesystem.
     * @param sourceName the path of the source file inside the JAR-file.
     * @param destinationName the path of the destination as absolute path.
     * @throws IOException if an error occurs while writing or reading the files.
     */
    public void transfer(String sourceName, String destinationName) throws IOException{
        transfer(sourceName, destinationName, true);
    }

    /**
     * Will copy the given specified file to the specified destination.
     * The source-filename must be the files position inside the JAR-file (e.g. "/sql/create.sql"), the destination must be an absolute path to the executing computers filesystem.
     * @param sourceName the path of the source file inside the JAR-file.
     * @param destinationName the path of the destination as absolute path.
     * @param replace boolean indicating, whether a file shall be overwritten if it already exists. True will overwrite, false will abort if the file already exists.
     * @throws IOException if an error occurs while writing or reading the files.
     */
    public void transfer(String sourceName, String destinationName, boolean replace) throws IOException{
        if(sourceName==null || sourceName.isEmpty() || destinationName==null || destinationName.isEmpty()){
            throw new IOException("At least one of the paths is empty or null!");
        }
        File destinationFile=new File(destinationName);
        if(!replace && destinationFile.exists()){
            return;
        }
        if(!destinationFile.getParentFile().exists()){
            Files.createDirectories(Paths.get(destinationFile.getParentFile().getCanonicalPath()));
        }
        InputStream sourceStream = this.getClass().getResourceAsStream(sourceName);
        if(sourceStream==null){
            throw new FileNotFoundException("File at path "+sourceName+" not found!");
        }
        FileOutputStream fos=new FileOutputStream(destinationFile);
        OutputStream destStream = new BufferedOutputStream(fos);
        byte[] buffer = new byte[1024*8];
        int length;
        while((length=sourceStream.read(buffer))>=0){
            destStream.write(buffer,0,length);
        }

        sourceStream.close();
        destStream.close();
        fos.close();
    }
}
