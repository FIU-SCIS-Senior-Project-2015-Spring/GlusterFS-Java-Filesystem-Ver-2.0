package com.peircean.glusterfs.examples.steps;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Properties;
import java.util.Set;

public class WriteToAFileInAGlusterfsVolumeSteps {
    Properties properties = new Properties();
    String vagrantBox;
    String volname;
    String mountUri;
    FileSystem fileSystem;
    FileStore store;
    String fileUri;
    Path glusterPath;
    Set<PosixFilePermission> posixFilePermissions;
    FileAttribute<Set<PosixFilePermission>> attrs;
    long fileSize;


    @Given("a machine with a running GlusterFS volume")
    public void givenARunningGlusterfsVolume() {
        setupMachineRunningGlusterfs();
    }


    @Given("an existing file in such volume with write permissions")
    public void givenAnExistingFileInAGlusterfsVolume() {
        createWritableFileInGlusterfs();
    }


    @When("the write function is used with the proper parameters")
    public void whenTheWriteFunctionIsUsed() {
        try {
            fileSize = Files.size(glusterPath); // file size before writing
            System.out.println("File size before write: " + fileSize);
            String hello = "Hello, world!";
            Files.write(glusterPath, hello.getBytes(), StandardOpenOption.WRITE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Error in whenTheWriteFunctionIsUsed(), file could not be writen to.");
            e.printStackTrace();
        }
    }


    @Then("the file is written to")
    public void thenTheFileIsWritten() throws IOException {
        long newFileSize = Files.size(glusterPath);
        if (fileSize != newFileSize)
            System.out.println("File size after write: " + newFileSize);
        else
            System.out.println("Error: file was not changed");
    }


    public void setupMachineRunningGlusterfs() {
        try {
            properties.load(WriteToAFileInAGlusterfsVolumeSteps.class.getClassLoader().getResourceAsStream("examples.properties"));
        } catch (IOException e) {
            System.out.printf("\n\nError in setupMachineRunningGlusterfs()\n\n");
            e.printStackTrace();
        }

        vagrantBox = properties.getProperty("glusterfs.server");
        volname = properties.getProperty("glusterfs.volume");
        mountUri = "gluster://" + vagrantBox + ":" + volname + "/";

        try {
            fileSystem = FileSystems.newFileSystem(new URI(mountUri), null);
            store = fileSystem.getFileStores().iterator().next();
            System.out.println("Gluster file sytem ready: " + fileSystem.toString());
        } catch (Exception e) {
            System.out.printf("\n\nError in setupMachineRunningGlusterfs()\n\n");
            e.printStackTrace();
        }
    }


    public void createWritableFileInGlusterfs() {
        // First set permissions file will have
        posixFilePermissions = PosixFilePermissions.fromString("rw-rw-rw-");
        attrs = PosixFilePermissions.asFileAttribute(posixFilePermissions);
        fileUri = "gluster://" + vagrantBox + ":" + volname + "/writeToAFileTest.txt";

        try {
            glusterPath = Paths.get(new URI(fileUri));

            Files.createFile(glusterPath, attrs);
            System.out.println("File created: " + glusterPath.toString());

        } catch (URISyntaxException e) {
            System.out.println("Error in createWritableFileInGlusterfs() while creating a path.");
            e.printStackTrace();

        } catch (IOException e) {
            System.out.println("File already exists at " + glusterPath.toString());
        }
    }
}