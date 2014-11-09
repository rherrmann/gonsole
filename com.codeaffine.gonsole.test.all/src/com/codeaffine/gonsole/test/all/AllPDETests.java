package com.codeaffine.gonsole.test.all;

import org.junit.runner.RunWith;

import com.codeaffine.gonsole.test.util.osgi.testsuite.BundleTestSuite;
import com.codeaffine.gonsole.test.util.osgi.testsuite.BundleTestSuite.TestBundles;

@RunWith(BundleTestSuite.class)
@TestBundles({
	"com.codeaffine.console.core",
	"com.codeaffine.gonsole",
	"com.codeaffine.gonsole.egit",
	"com.codeaffine.console.calculator" })
public class AllPDETests {
}