package com.peircean.glusterfs.examples.steps;

import java.nio.file.*;
import java.nio.file.spi.FileSystemProvider;
import java.util.Properties;
import java.io.IOException;
import java.net.URI;

import org.jbehave.core.annotations.*;


public class ConnectToGlusterfsVolumeSteps {

    Properties properties = new Properties();
    String vagrantBox;
    String volname;
    String mountUriString;
    URI mountUri;
    //Path mountPath;
    FileSystem fileSystem;
    FileStore store;


    @Given("a GlusterFS server name and and volume name")
    public void givenAGlusterfsVolumeAndName() {

        // server and name are already stored in a properties file (src/test/resources/examples.properties
        try {
            properties.load(ConnectToGlusterfsVolumeSteps.class.getClassLoader().getResourceAsStream("examples.properties"));
        } catch (IOException e) {
            System.out.printf("\n\nError in givenAGlusterfsVolume()\n\n");
            e.printStackTrace();
        }
        System.out.println("Server name: " + properties.getProperty("glusterfs.server"));
        System.out.println("Volume name: " + properties.getProperty("glusterfs.volume"));
    }

    @When("a new Gluster URI is created")
    public void whenANewGlusterURIisCreated() {

        // vagrant brings up a virtual machine previously set up with the following properties
        vagrantBox = properties.getProperty("glusterfs.server");
        volname = properties.getProperty("glusterfs.volume");

        // URI will be mounted in the virtual machine
        mountUriString = "gluster://" + vagrantBox + ":" + volname + "/";

        try {
            mountUri = new URI(mountUriString);
            System.out.println("" + mountUri.toString() + " URI is created");
        } catch (Exception e) {
            System.out.printf("\n\nError in whenANewGlusterURIisCreated\n\n");
            e.printStackTrace();
        }
    }

    @Then("a new GlusterFileSystem object can be created")
    public void aNewGlusterFileSystemIsCreated() {

        try {
            fileSystem = FileSystems.newFileSystem(mountUri, null);
            store = fileSystem.getFileStores().iterator().next();

            System.out.println(getProvider("gluster").toString());
            System.out.println(fileSystem.toString());
        } catch (Exception e) {
            System.out.printf("\n\nError in aNewGlusterFileSystemIsCreated\n\n");
            e.printStackTrace();
        }
    }

    public static FileSystemProvider getProvider(String scheme) {
        for (FileSystemProvider fsp : FileSystemProvider.installedProviders()) {
            if (fsp.getScheme().equals(scheme)) {
                return fsp;
            }
        }
        throw new IllegalArgumentException("No provider found for scheme: " + scheme);
    }
}
