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

package net.sf.j2s.ajax;

import net.sf.j2s.ajax.IXHRCallback;
import org.eclipse.swt.widgets.Display;


/**
 * This adapter class provides a default implementation of IXHRCallback.
 * This adapter class wraps current thread scope for those swtOnXXXX method. 
 * 
 * @author josson smith
 *
 * 2006-2-11
 */
public class XHRCallbackSWTAdapter implements IXHRCallback {
	
	/**
	 * Method will be called when XMLHttpRequest receives all reponses.
	 */
	public void swtOnComplete() {
	}

	/**
	 * Method will be called when XMLHttpRequest is transforming request and 
	 * receiving response.
	 */
	public void swtOnInteractive() {
	}

	/**
	 * Method will be called when XMLHttpRequest already setup HTTP connection.
	 */
	public void swtOnLoaded() {
	}

	/**
	 * Method will be called when XMLHttpRequest is loading.
	 */
	public void swtOnLoading() {
	}

//	public void swtOnUninitialized() {
//	}

	/**
	 * Call <code>#swtOnComplete</code> when all responses are received.
	 * @j2sNative this.swtOnComplete();
	 */
	public void onComplete() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				swtOnComplete();
			}
		});
	}

	/**
	 * Call <code>#swtOnInteractive</code> when the request is sending and the reponse comes.
	 * @j2sNative this.swtOnInteractive();
	 */
	public void onInteractive() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				swtOnInteractive();
			}
		});
	}

	/**
	 * Call <code>#swtOnLoaded</code> when the HTTP connection is setup.
	 * @j2sNative this.swtOnLoaded();
	 */
	public void onLoaded() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				swtOnLoaded();
			}
		});
	}

	/**
	 * Call <code>#swtOnLoading</code> when <code>HttPRequest#open</code> is called.
	 * @j2sNative this.swtOnLoading();
	 */
	public void onLoading() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				swtOnLoading();
			}
		});
	}

//	/**
//	 * @j2sNative this.swtOnUninitialized();
//	 */
//	public void onUninitialized() {
//		Display.getDefault().syncExec(new Runnable() {
//			public void run() {
//				swtOnUninitialized();
//			}
//		});
//	}

}