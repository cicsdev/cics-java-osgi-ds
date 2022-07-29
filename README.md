# cics-java-osgi-ds
Demonstration of Declarative Services (DS) in OSGi JVM servers

## Preface
This example shows how OSGi Declarative Services (DS) can be used in an OSGi JVM server to perform application component refresh (change of, or fix to, implementations) without loss of service. Declarative Services removes the need for boilerplate tracking code such as ServiceTrackers, and by moving to a service component architecture from OSGi bundle-wiring (Import-Package) you gain all the dynamic refresh and look-up benefits of services. DS handles all the injection of components into your application, and ensures that if a service (or implementation) is removed it can seamlessly switch to a replacement (or updated) service.

There are 8 projects in this simple sample (4 OSGi projects, and each has a corresponding CICS bundle project). This same architecture can be used for complex applications.

Below is a simple diagram which explains the relationships between these projects:

```
                                                      Implementation 1 +-----------+
                                                   /-------------------| In memory |
+----------+             +-------------------+    /                    +-----------+
|          |  Injection  |                   |<--/
| CICS App |<------------| Storage Service   |
|          |             |                   |<--\
+----------+             +-------------------+    \                    +-----------+
                                                   \-------------------|    TSQ    |
                                                      Implementation 2 +-----------+
```

The *CICS App* is the OSGi bundle which contains the CICS main class. It uses the interface, or service, provided by the *Storage Service* to put data into storage and retrieve data from storage.

In this example, we have two separate implementations of the storage service. Version 1.0.0 is a simple in-memory storage system, which uses Java objects to store data.

The second implementation, version 1.1.0, uses a CICS TSQ to store data, using the JCICS APIs.

In our example we follow the scenario that we have the base application running using the in-memory storage service and we now want to upgrade to the new TSQ storage service.

When we first install the new CICS bundle, the TSQ storage service will activate, but the CICS App will remain bound to the in-memory implementation. We can then disable the in-memory implementation. At which point OSGi declarative services will bind the new TSQ service and unbind the in-memory service (in that order). This means the CICS App is never left without a working implementation and therefore avoiding loss of service.

We have chosen to separate each component of the application into distinct CICS bundles to give ultimate flexibility.

## Prerequisites
* CICS TS for z/OS 5.1 or above
* Java SE 1.8 or later on the workstation

## Supporting Files
* [com.ibm.cicsdev.osgi.ds.cicsapp_1.0.0](projects/com.ibm.cicsdev.osgi.ds.cicsapp_1.0.0) - OSGi bundle project containing the CICS application entry point
* [com.ibm.cicsdev.osgi.ds.cicsapp.bundle_1.0.0](projects/com.ibm.cicsdev.osgi.ds.cicsapp.bundle_1.0.0) - CICS bundle containing the application, program and transaction definitions.
* [com.ibm.cicsdev.osgi.ds.storage_1.0.0](projects/com.ibm.cicsdev.osgi.ds.storage_1.0.0) - OSGi bundle project containing interfaces for the storage service
* [com.ibm.cicsdev.osgi.ds.storage.bundle_1.0.0](projects/com.ibm.cicsdev.osgi.ds.storage.bundle_1.0.0) - CICS bundle for the storage service interface
* [com.ibm.cicsdev.osgi.ds.storage.impl_1.0.0](projects/com.ibm.cicsdev.osgi.ds.storage.impl_1.0.0) - OSGi bundle project containing the in-memory implementation of the storage service
* [com.ibm.cicsdev.osgi.ds.storage.impl.bundle_1.0.0](projects/com.ibm.cicsdev.osgi.ds.storage.impl.bundle_1.0.0) - CICS bundle for the base in-memory implementation
* [com.ibm.cicsdev.osgi.ds.storage.impl_1.1.0](projects/com.ibm.cicsdev.osgi.ds.storage.impl_1.1.0) - OSGi bundle project containing the TSQ implementation of the storage service
* [com.ibm.cicsdev.osgi.ds.storage.impl.bundle_1.1.0](projects/com.ibm.cicsdev.osgi.ds.storage.impl.bundle_1.1.0) - CICS bundle for the TSQ implementation

## Usage
1. Clone or download this repository onto a local workstation.
2. Import the projects into CICS Explorer.
3. Deploy all the CICS bundle projects to zFS.
4. Create an OSGi JVM server with the name `DFHOSGI`.
5. Create CICS bundle definitions for these OSGi bundle projects. [Table 1](#table-1) shows the mapping of bundle to bundle name used.
6. Install the CICS bundle definitions: `DS      `, `DS-APP  ` and `DS-IMP10`.
7. Run the transaction `DSTS PUT INMEM`, this should display the message `Created entry 1`.
8. Run the transaction `DSTS GET 1`, this should display the message `INMEM`, which confirms the data was stored correctly in-memory.
9. Install the CICS bundle `DS-IMP11`.
10. Disable the CICS bundle `DS-IMP10`.
11. Run the transaction `DSTS GET 1`, this should abend because the entry was not found in the TSQ.
12. Run the transaction `DSTS PUT TSQ`, this should display the message `Created entry 1`.
13. Run the transaction `DSTS GET 1`, this should display the message `TSQ`, which confirms the data was stored correctly  in the TSQ (confirm by browsing on the TSQ `TSQS`, using the `CEBR` transaction for example).
14. The standard out (STDOUT) file for the JVM server should display messages that indicate the in-memory service (version 1.0.0) was first bound. Then later the service will be unbound when the OSGi bundle is disabled. You should also see that the TSQ service (1.1.0) is bound in it's place.

### Table 1

| CICS Bundle Name | OSGi Bundle Project                      |
| ---------------- | ---------------------------------------- |
| `DS      `       | com.ibm.cicsdev.osgi.ds.bundle_1.0.0     |
| `DS-APP  `       | com.ibm.cicsdev.osgi.ds.app.bundle_1.0.0 |
| `DS-IMP10`       | com.ibm.cicsdev.osgi.ds.tsq.bundle_1.0.0 |
| `DS-IMP11`       | com.ibm.cicsdev.osgi.ds.tsq.bundle_1.0.1 |
 
## License
This project is licensed under [Apache License Version 2.0](LICENSE).
