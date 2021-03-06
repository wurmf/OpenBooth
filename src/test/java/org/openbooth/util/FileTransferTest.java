package org.openbooth.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.Before;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Testclass for the class FileTransfer
 */
public class FileTransferTest {
    private final String realSourcePath;
    private final String alternativeSourcePath;
    private String realDestPath;
    public FileTransferTest(){
        this.realSourcePath="/images/test_logo_img.jpg";
        this.alternativeSourcePath="/images/test_imageprocessor_img.jpg";
    }

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Before
    public void setUp(){
        this.realDestPath = testFolder.getRoot().getPath() + "test_img_for_FileTransferTest.jpg";
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSource() throws IOException{
        FileTransfer.transfer(null,realDestPath);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testNullDest() throws IOException{
        FileTransfer.transfer(realDestPath, null);
    }
    @Test
    public void testActualCopy() throws IOException{
        File file= new File(realDestPath);
        if(file.exists()){
            file.delete();
        }
        FileTransfer.transfer(realSourcePath, realDestPath);
        assertTrue(file.exists());
        if(file.exists()){
            file.delete();
        }
    }
    @Test
    public void testOverwriteTrue() throws IOException{
        File file= new File(realDestPath);
        FileTransfer.transfer(realSourcePath, realDestPath);
        assertFalse(!file.exists());
        //TODO: load file
        FileTransfer.transfer(alternativeSourcePath, realDestPath);
        //TODO: load new file, compare
    }

    @Test
    public void testOverwriteFalse() throws IOException{
        File file= new File(realDestPath);
        FileTransfer.transfer(realSourcePath, realDestPath);
        assertFalse(!file.exists());
        //TODO: load file
        FileTransfer.transfer(alternativeSourcePath, realDestPath, false);
        //TODO: load new file, compare
    }
}
