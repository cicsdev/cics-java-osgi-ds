# cics-java-osgi-ds
Demonstration of Declarative Services (DS) in OSGi JVM servers

## Supporting Files
* [com.ibm.cicsdev.osgi.ds_1.0.0](projects/com.ibm.cicsdev.osgi.ds_1.0.0) - OSGi interfaces
* [com.ibm.cicsdev.osgi.ds.bundle_1.0.0](projects/com.ibm.cicsdev.osgi.ds.bundle_1.0.0) - CICS Bundle for the OSGi interfaces
* [com.ibm.cicsdev.osgi.ds.app_1.0.0](projects/com.ibm.cicsdev.osgi.ds.app_1.0.0) - CICS application entry point
* [com.ibm.cicsdev.osgi.ds.app.bundle_1.0.0](projects/com.ibm.cicsdev.osgi.ds.app.bundle_1.0.0) - CICS Bundle containing the application, program and transaction.
* [com.ibm.cicsdev.osgi.ds.tsq_1.0.0](projects/com.ibm.cicsdev.osgi.ds.tsq_1.0.0) - In-memory implementation
* [com.ibm.cicsdev.osgi.ds.tsq.bundle_1.0.0](projects/com.ibm.cicsdev.osgi.ds.tsq.bundle_1.0.0) - CICS Bundle for the base in-memory implementation
* [com.ibm.cicsdev.osgi.ds.tsq_1.0.1](projects/com.ibm.cicsdev.osgi.ds.tsq_1.0.1) - TSQ implementation
* [com.ibm.cicsdev.osgi.ds.tsq.bundle_1.0.1](projects/com.ibm.cicsdev.osgi.ds.tsq.bundle_1.0.1) - CICS Bundle for the TSQ implementation

## Usage
1. Deploy all found bundle projects to z/FS
2. Create an OSGi JVM server with the name `DFHOSGI`.
3. Define CICS bundles for these bundle projects. [Table 1](#table-1) shows the mapping of bundle to bundle name used.
4. Install `DS      `, `DS.APP  ` and `DS.TSQ10`.
5. Run the transaction `DSTS PUT INMEM`, this should display the message `Created entry 1`
6. Run the transaction `DSTS GET 1`, this should display the message `INMEM`, which confirms the data was stored correctly in-memory
7. Install the bundle `DS.TSQ11`.
8. Disable the bundle `DS.TSQ10`.
9. Run the transaction `DSTS GET 1`, this should ABEND because the entry was not found in the TSQ.
10. Run the transaction `DSTS PUT TSQ`, this should display the message `Created entry 1`
10. Run the transaction `DSTS GET 1`, this should display the message `TSQ`, which confirms the data was stored correctly  in the TSQ (confirm by browsing on the TSQ `TSQS`, e.g. `CEBR TSQS`).
11. The standard out (STDOUT) file for the JVM server should display messages which indicate that the in-memory service (version 1.0.0) was first bound. Then later unbound when the bundle was disabled. Then that the TSQ service (1.0.1) was bound in it's place.


### Table 1

| CICS Bundle Name | Bundle Project                           |
| ---------------- | ---------------------------------------- |
| `DS      `       | com.ibm.cicsdev.osgi.ds.bundle_1.0.0     |
| `DS.APP  `       | com.ibm.cicsdev.osgi.ds.app.bundle_1.0.0 |
| `DS.TSQ10`       | com.ibm.cicsdev.osgi.ds.tsq.bundle_1.0.0 |
| `DS.TSQ11`       | com.ibm.cicsdev.osgi.ds.tsq.bundle_1.0.1 |
 
