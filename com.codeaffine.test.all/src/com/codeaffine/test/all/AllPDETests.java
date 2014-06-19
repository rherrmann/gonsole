package com.codeaffine.test.all;

import org.junit.runner.RunWith;

import com.codeaffine.test.util.osgi.testsuite.BundleTestSuite;
import com.codeaffine.test.util.osgi.testsuite.BundleTestSuite.TestBundles;

@RunWith(BundleTestSuite.class)
@TestBundles({ 
	"com.codeaffine.console.core",
	"com.codeaffine.gonsole",
	"com.codeaffine.gonsole.egit" })
public class AllPDETests {
}