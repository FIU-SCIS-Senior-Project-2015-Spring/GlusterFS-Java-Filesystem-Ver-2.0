package com.peircean.glusterfs.examples.steps;

import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.spi.FileSystemProvider;
import java.util.Properties;
import java.io.IOException;
import java.net.URI;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;


public class ConnectToGlusterfsVolumeSteps {

    Properties properties = new Properties();
    String vagrantBox;
    String volname;
    String mountUri;
    String testUri;
    Path mountPath;
    FileSystem fileSystem;
    FileStore store;

    @Given("a GlusterFS volume server and name")
    public void givenAGlusterfsVolume() {

        System.out.printf("\n\n--------------------------------------\nSTORY: Connect To Glusterfs Volume\n" +
            "--------------------------------------\n");

        // server and name are already stored in a properties file (src/test/resources/examples.properties
        try {
            properties.load(ConnectToGlusterfsVolumeSteps.class.getClassLoader().getResourceAsStream("examples.properties"));
        } catch (IOException e) {
            System.out.printf("\n\nError in givenAGlusterfsVolume()\n\n");
            e.printStackTrace();
        }
        System.out.println("\nGiven");
        System.out.println("the Server name: " + properties.getProperty("glusterfs.server"));
        System.out.println("and the volume name: " + properties.getProperty("glusterfs.volume"));
    }

    @When("a new Gluster URI is created")
    public void whenANewGlusterURIisCreated() {

        // vagrant brings up a virtual machine previously set up with the following properties
        this.vagrantBox = properties.getProperty("glusterfs.server");
        this.volname = properties.getProperty("glusterfs.volume");

        // URI will be mounted in the virtual machine
        this.mountUri = "gluster://" + this.vagrantBox + ":" + this.volname + "/";
        this.testUri = "gluster://" + this.vagrantBox + ":" + this.volname + "/baz";

        try {
            this.mountPath = Paths.get(new URI(this.mountUri));
            System.out.println("\nWhen the\n" + this.mountPath.toString() + " URI is created");
        } catch (Exception e) {
            System.out.printf("\n\nError in whenANewGlusterURIisCreated\n\n");
            e.printStackTrace();
        }
    }

    @Then("a new GlusterFileSystem object should be created")
    public void aNewGlusterFileSystemIsCreated() {

        try {
            this.fileSystem = FileSystems.newFileSystem(new URI(this.mountUri), null);
            this.store = fileSystem.getFileStores().iterator().next();

            System.out.println("\nThen\na Gluster file sytem can be created");
            System.out.println(getProvider("gluster").toString());
            System.out.println(this.fileSystem.toString());
            System.out.printf("FS total space: " + this.store.getTotalSpace());
            System.out.printf(", FS usable space: " + this.store.getUsableSpace());
            System.out.printf(", FS unallocated space: " + this.store.getUnallocatedSpace() + "\n");

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
