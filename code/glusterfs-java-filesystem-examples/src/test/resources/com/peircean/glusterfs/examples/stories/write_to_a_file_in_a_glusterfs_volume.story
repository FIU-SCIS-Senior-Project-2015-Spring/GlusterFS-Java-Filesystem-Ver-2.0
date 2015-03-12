Meta:

Narrative:
As a developer using GlusterFS
I want to be able to write to an existent file in a GlusterFS volume
So that the file is modified

Scenario: An existing file (with write permissions) in a GlusterFS volume es edited.

Given a machine with a running GlusterFS volume
Given an existing file in such volume with write permissions
When the write function is used with the proper parameters
Then the file is written to