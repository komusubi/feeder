package org.komusubi.feeder.web;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class StandAloneTest {

	@Rule
	public TemporaryFolder tmpDir = new TemporaryFolder();

	@Test
	public void buildCacheDirectory() throws Throwable {
		// setup
		StandAlone standAlone = new StandAlone();
		System.setProperty("feeder.home", tmpDir.getRoot().getAbsolutePath());
		File expected = new File(tmpDir.getRoot(), "temp");
		// exercise 
		File actual = standAlone.cacheDirectory();
		// verify
		assertTrue(actual.equals(expected));
	}

}
