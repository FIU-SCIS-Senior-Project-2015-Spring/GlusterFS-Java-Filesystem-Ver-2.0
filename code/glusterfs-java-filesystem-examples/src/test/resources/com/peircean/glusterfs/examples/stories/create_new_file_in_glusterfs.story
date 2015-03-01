Narrative:
As a developer using a Glusterfs volume
I want to create a file

Scenario: a developer wants create a file in a running Glusterfs volume.

Given a machine with a running Glustefs volume
When an URI is used to create a new file
Then a new file is created if it did not exist