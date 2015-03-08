package com.peircean.glusterfs.examples.steps;

import org.jbehave.core.annotations.*;

import javax.print.attribute.URISyntax;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Properties;
import java.util.Set;


public class CreateNewFileInGlusterfsSteps {

    Properties properties = new Properties();
    String vagrantBox;
    String volname;
    String mountUri;
    String fileUri;
    Path glusterPath;
    FileSystem fileSystem;
    FileStore store;
    Set<PosixFilePermission> posixFilePermissions;
    FileAttribute<Set<PosixFilePermission>> attrs;


    @Given("a machine with a running Glustefs volume")
    public void givenAGlusterfsVolume()  {
        setupMachineRunningGlusterfs();
    }

    @When("an URI is used to create a new file")
    public void whenAnUriIsUsedToCreateANewFile(){
        // First set permissions file will have
        posixFilePermissions = PosixFilePermissions.fromString("rw-rw-rw-");
        attrs = PosixFilePermissions.asFileAttribute(posixFilePermissions);
        fileUri = "gluster://" + vagrantBox + ":" + volname + "/aFile.txt";
        try {
            glusterPath = Paths.get(new URI(fileUri));
            System.out.println("When the URI " + glusterPath.toString() + " is used to create a new file.");
        } catch (URISyntaxException e) {
            System.out.println("Error in whenAnUriIsUsedToCreateANewFile() while creating a path.");
            e.printStackTrace();
        }
    }

    @Then("a new file is created if it did not exist")
    public void thenANewFileIsCreatedIfItDidNotExist() {
        try {
            Files.createFile(glusterPath, attrs);
            System.out.println("File system created: " + glusterPath.toString());
        } catch (IOException e) {
            System.out.println("File system already exists at " + glusterPath.toString());
        }
    }

    public void setupMachineRunningGlusterfs() {
        try {
            properties.load(CreateNewFileInGlusterfsSteps.class.getClassLoader().getResourceAsStream("examples.properties"));
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
}
