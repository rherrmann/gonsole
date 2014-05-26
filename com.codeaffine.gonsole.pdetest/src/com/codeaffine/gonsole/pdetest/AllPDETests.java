package com.codeaffine.gonsole.pdetest;

import org.junit.runner.RunWith;

import com.codeaffine.osgi.testuite.BundleTestSuite;
import com.codeaffine.osgi.testuite.BundleTestSuite.TestBundles;

@RunWith(BundleTestSuite.class)
@TestBundles({"com.codeaffine.gonsole"})
public class AllPDETests {
}
