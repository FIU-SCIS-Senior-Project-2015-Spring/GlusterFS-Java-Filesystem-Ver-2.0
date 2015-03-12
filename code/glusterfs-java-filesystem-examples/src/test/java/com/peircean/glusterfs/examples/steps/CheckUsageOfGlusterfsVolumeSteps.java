package com.peircean.glusterfs.examples.steps;


import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.Properties;

public class CheckUsageOfGlusterfsVolumeSteps {
    Properties properties = new Properties();
    String vagrantBox;
    String volname;
    String mountUri;
    FileSystem fileSystem;
    FileStore store;

    long totalSpace;
    long usableSpace;
    long unallocatedSpace;


    @Given("a running GlusterFS volume")
    public void givenARunningGlusterfsVolume() {
        setupMachineRunningGlusterfs();
    }


    @When("the usage values are requested")
    public void whenTheUsageValuesAreRequested() {
        try {
            totalSpace =  store.getTotalSpace();
            usableSpace = store.getUsableSpace();
            unallocatedSpace = store.getUnallocatedSpace();
        } catch (IOException e) {
            System.out.printf("Error in whenTheUsageValuesAreRequested()");
            e.printStackTrace();
        }
    }


    @Then("the volume usage should be returned")
    public void thenTheVolumeUsagesAreReturned() {
        System.out.println("TOTAL SPACE: " + totalSpace);
        System.out.println("USABLE SPACE: " + usableSpace);
        System.out.println("UNALLOCATED SPACE: " + unallocatedSpace);
    }


    public void setupMachineRunningGlusterfs() {
        try {
            properties.load(CheckUsageOfGlusterfsVolumeSteps.class.getClassLoader().getResourceAsStream("examples.properties"));
        } catch (IOException e) {
            System.out.printf("\n\nError in CHANGEME()\n\n");
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

