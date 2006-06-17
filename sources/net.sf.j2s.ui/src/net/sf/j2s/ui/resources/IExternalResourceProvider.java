/*******************************************************************************
 * Java2Script Pacemaker (http://j2s.sourceforge.net)
 *
 * Copyright (c) 2006 ognize.com and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     ognize.com - initial API and implementation
 *******************************************************************************/

package net.sf.j2s.ui.resources;

/**
 * @author josson smith
 *
 * 2006-5-13
 */
public interface IExternalResourceProvider {
	public String[] getKeys();
	public String[] getDescriptions();
	public String[][] getResources();
	public void copyResources(String key, String destPath);
}
