Adding a new program
====================

If you want to add a new program to the ScattPort client, you need to create a new class in the package _org.scattport.client.apps_.

You can use the classes _DummyApp_ and _Sscatt_ as a starting point.
Every program is inherited from _org.scattport.client.App_, which contains generic methods, e.g. setting up working directories, and so on.

Further you need to edit _org.scattport.client.JobFetcher_, to introduce your new application.
