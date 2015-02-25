package com.peircean.glusterfs.examples.steps;

import com.peircean.glusterfs.GlusterFileSystem;
import com.peircean.glusterfs.GlusterPath;

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

//    Properties properties = new Properties();
//    String vagrantBox;
//    String volname;
//    String mountUri;
//    String testUri;
//    Path mountPath;
//    FileSystem fileSystem;
//    FileStore store;

    @Given("a GlusterFS volume server and name")
    public void givenAGlusterfsVolume() throws URISyntaxException, IOException, InterruptedException {
        //loadPreconfiguredVolumeProperties();
        System.out.printf("\n\n======================================\n" +
            "STORY: Connect To Glusterfs Volume\n" +
            "======================================\n");
//        System.out.println("Server name: " + properties.getProperty("glusterfs.server"));
//        System.out.println("Gluster volume name: " + properties.getProperty("glusterfs.volume"));

        Properties properties = new Properties();
        properties.load(ConnectToGlusterfsVolumeSteps.class.getClassLoader().getResourceAsStream("examples.properties"));

        String vagrantBox = properties.getProperty("glusterfs.server");
        String volname = properties.getProperty("glusterfs.volume");

        System.out.println(getProvider("gluster").toString());

        String mountUri = "gluster://" + vagrantBox + ":" + volname + "/";
        String testUri = "gluster://" + vagrantBox + ":" + volname + "/baz";
        Path mountPath = Paths.get(new URI(mountUri));

        FileSystem fileSystem = FileSystems.newFileSystem(new URI(mountUri), null);
        FileStore store = fileSystem.getFileStores().iterator().next();
        System.out.println("TOTAL SPACE: " + store.getTotalSpace());
        System.out.println("USABLE SPACE: " + store.getUsableSpace());
        System.out.println("UNALLOCATED SPACE: " + store.getUnallocatedSpace());
        System.out.println(fileSystem.toString());
    }

    @When("a new Gluster URI is created")
    public void whenANewGlusterURIisCreated() {
        System.out.println("I am in the when step");
//        this.mountUri = "gluster://" + this.vagrantBox + ":" + this.volname + "/";
//        this.testUri = "gluster://" + this.vagrantBox + ":" + this.volname + "/baz";
//        try {
//            System.out.println(new URI(this.mountUri));
//            this.mountPath = Paths.get(new URI(this.mountUri));
//        } catch (Exception e) {
//            System.out.printf("\n\nError in whenANewGlusterURIisCreated\n\n");
//            e.printStackTrace();
//        }

    }

    @Then("a new GlusterFileSystem object should be created")
    public void aNewGlusterFileSystemIsCreated() {
        System.out.println("I am in the then step");
//        try {
//            this.fileSystem = FileSystems.newFileSystem(new URI(this.mountUri), null);
//            this.store = fileSystem.getFileStores().iterator().next();
//            System.out.println("TOTAL SPACE: " + this.store.getTotalSpace());
//            System.out.println("USABLE SPACE: " + this.store.getUsableSpace());
//            System.out.println("UNALLOCATED SPACE: " + this.store.getUnallocatedSpace());
//            System.out.println(this.fileSystem.toString());
//        } catch (Exception e) {
//            System.out.printf("\n\nError in aNewGlusterFileSystemIsCreated\n\n");
//            e.printStackTrace();
//        }
    }

//    private void loadPreconfiguredVolumeProperties() {
//        try {
//            properties.load(ConnectToGlusterfsVolumeSteps.class.getClassLoader().getResourceAsStream("examples.properties"));
//
//
//            this.vagrantBox = properties.getProperty("glusterfs.server");
//
//            System.out.println(this.vagrantBox);
//            this.volname = properties.getProperty("glusterfs.volume");
//            System.out.println(this.volname);
//
//            //System.out.println(getProvider("gluster"));
//            this.properties.getProperty("glusterfs.volume");
//        } catch (IOException e) {
//            System.out.printf("\n\nError in load PreconfiguredVolumeProperties\n\n");
//            e.printStackTrace();
//        }
//    }

    public static FileSystemProvider getProvider(String scheme) {
        for (FileSystemProvider fsp : FileSystemProvider.installedProviders()) {
            if (fsp.getScheme().equals(scheme)) {
                return fsp;
            }
        }
        throw new IllegalArgumentException("No provider found for scheme: " + scheme);
    }
}
