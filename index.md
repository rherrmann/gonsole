---
layout: default
title: Git Console Integration for the Eclipse IDE 
---
![](gonsole.png)
Git Console Integration for the Eclipse IDE [![Build Status](https://travis-ci.org/rherrmann/gonsole.png)](https://travis-ci.org/rherrmann/gonsole)
======

[Git](http://http://git-scm.com/) is a popular version control system.  This project integrates a Git console into
the Eclipse IDE. It lets you enter common Git commands directly into the [Eclipse Console View](http://help.eclipse.org/kepler/index.jsp?topic=%2Forg.eclipse.jdt.doc.user%2Freference%2Fviews%2Fconsole%2Fref-console_view.htm) and displays their output within this view.

![](readme-screenshot.png)

The project is currently in its proof of concept stage and of almost no practical use. We will soon provide early access versions that can be consumed from a software repository.

<!--- 
??? TBD:
* Screenshot 
* Link to project page: Please see the [project page](http://.../) for details on features and usage. 
* ([Published on Eclipse Marketplace](<???TBD marketplace url>))
--->


Requirements
------------

Eclipse 3.8 (Juno) or newer.


Git included
---------------

It is not necessary to have Git installed on your computer for the the plug-in to work. The plug-in uses [JGit](https://eclipse.org/jgit/), a pure Java implementation of Git, to interact with the repository. 


Installation
------------

Install from this Eclipse software repository: http://rherrmann.github.io/gonsole/repository/


License
-------

The code is published under the terms of the [Eclipse Public License, version 1.0](http://www.eclipse.org/legal/epl-v10.html).

Includes code from [JGit](https://eclipse.org/jgit/), which is published under the terms of the [Eclipse Distribution License - v 1.0](http://www.eclipse.org/org/documents/edl-v10.php)

