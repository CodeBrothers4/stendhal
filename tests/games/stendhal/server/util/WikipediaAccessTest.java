/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2010 - Stendhal                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server.util;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.hamcrest.Matcher;
import org.junit.Test;

/**
 * Tests the WikipediaAccess class.
 */
public class WikipediaAccessTest {

	/**
	 * Test
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void test() {
		final WikipediaAccess access = new WikipediaAccess("Stendhal");

		access.run();

		if (access.getError() != null) {
			fail("Wikipedia access was not successful: " + access.getError());
		} else if (access.isFinished()) {
			if ((access.getText() != null) && (access.getText().length() > 0)) {
				final String result = access.getProcessedText();

				 System.out.println(result);

				final Matcher<String> henrimariebeyle = allOf(containsString("Marie"), containsString("Henri"), containsString("Beyle"));
				assertThat("There should be named the french novelist for the topic Stendhal.", result, henrimariebeyle);
			} else {
				fail("Sorry, could not find information on this topic in Wikipedia.");
			}
		}
	}

}
