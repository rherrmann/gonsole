package com.codeaffine.gonsole.test.all;

import org.junit.runner.RunWith;

import com.codeaffine.osgi.testuite.BundleTestSuite;
import com.codeaffine.osgi.testuite.BundleTestSuite.ClassnameFilters;
import com.codeaffine.osgi.testuite.BundleTestSuite.TestBundles;

@RunWith(BundleTestSuite.class)
@TestBundles( {
	"com.codeaffine.console.core",
	"com.codeaffine.gonsole",
	"com.codeaffine.gonsole.egit",
	"com.codeaffine.console.calculator"
} )
@ClassnameFilters( {
  ".*PDETest"
} )
public class AllPDETests {
}