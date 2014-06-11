![](gonsole.png)
Git Console Integration for the Eclipse IDE [![Build Status](https://travis-ci.org/rherrmann/gonsole.png)](https://travis-ci.org/rherrmann/gonsole)
======

Introduction
---
Please see [this page](http://rherrmann.github.io/gonsole/) for detailed information.


Contributing to Gonsole
---
If you plan to contribute a feature or bugfix to this project or just the want to have a closer look at the sources,
the following section describes the setup of the development environment
 
1. Start an Eclipse IDE that has the [Plug-in Development Environment (PDE)](https://www.eclipse.org/pde/) installed.
 If you want to use a fresh Eclipse installation, you can download [Eclipse Standard](https://www.eclipse.org/downloads/packages/eclipse-standard-432/keplersr2). 
 It contains everything you need.
 
2. Clone the git repository from git@github.com:rherrmann/gonsole.git
 
3. The necessary projects are located at the root of the repository. 
 Import all of them into your workspace.
 
4. In the com.codeaffine.gonsole.releng project there are [Target Platform Definitions](http://help.eclipse.org/juno/index.jsp?topic=%2Forg.eclipse.pde.doc.user%2Fconcepts%2Ftarget.htm) for Eclipse 3.8 and 4.4.
Open one of them and select the 'Set as Target Platform' link.
 


Now you are ready to hack the sources.
If you whish to verify the setup, you can run the test suites. There are two launch configurations to run them:
Gonsole - All Integration Tests and Gonsole - All Unit Tests.

Gonsole uses Maven/Tycho to build, the parent pom can be found in com.codeaffine.gonsole.releng.
