# Contributing Guidelines
You have to follow two rules only when contributing:

* Produce readable code
* **No Tests -> No Merge!**

# Development Environment Setup
If you plan to contribute a feature or bugfix to this project or just the want to have a closer look at the sources,
the following steps describe the setup of the development environment.
 
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


# Project License:  Eclipse Public License
By contributing code you automatically agree with the following points regarding licensing:

* You will only Submit Contributions where You have authored 100% of the content.
* You will only Submit Contributions to which You have the necessary rights. This means that if You are employed You have received the necessary permissions from Your employer to make the Contributions.
* Whatever content You Contribute will be provided under the Project License. 