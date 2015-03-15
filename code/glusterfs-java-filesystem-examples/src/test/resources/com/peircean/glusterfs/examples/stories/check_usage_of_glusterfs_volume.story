Meta:

Narrative:
As a developer using a Glustefs volume
I want to find out the usage values
So that I can use those values

Scenario:
A developer successfully retrieves the usage values for a GlusterFS volume

Given a running GlusterFS volume
When the usage values are requested
Then the volume usage should be returned