/*******************************************************************************
 * Copyright (c) 2007 java2script.org and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Zhou Renjian - initial API and implementation
 *******************************************************************************/
package net.sf.j2s.ajax;

import java.lang.reflect.Method;

import net.sf.j2s.ajax.SimpleRPCRunnable;
import net.sf.j2s.ajax.SimpleSerializable;

/**
 * 
 * @author zhou renjian
 */
public abstract class SimplePipeRunnable extends SimpleRPCRunnable {
	
	/**
	 * Pipe's id
	 */
	public String pipeKey;

	public boolean pipeAlive;
	
	SimplePipeHelper.IPipeThrough helper;
	
	/**
	 * 
	 * @param helper
	 * @j2sIgnore
	 */
	void setPipeHelper(SimplePipeHelper.IPipeThrough helper) {
		this.helper = helper;
	}
	
	public String getPipeURL() {
		return "simplepipe"; // url is relative to the servlet!
	}

	public String getPipeMethod() {
		return "GET";
	}

	@Override
	public void ajaxIn() {
		pipeInit();
	}
	
	@Override
	public void ajaxRun() {
		pipeAlive = pipeSetup();
		if (!pipeAlive) {
			return; // setup failed
		}
		pipeKey = SimplePipeHelper.registerPipe(this);
		keepPipeLive();
		pipeMonitoring();
	}

	@Override
	public void ajaxFail() {
		pipeFailed();
	}
	
	@Override
	public void ajaxOut() {
		if (pipeAlive) {
			pipeCreated();
		} else {
			pipeFailed();
		}
	}
	
	/**
	 * Listening on given events and pipe events from Simple RPC to client.
	 */
	public abstract boolean pipeSetup();
	
	/**
	 * Destroy the pipe and remove listeners.
	 * After pipe is destroyed, {@link #isPipeLive()} must be false
	 */
	public abstract void pipeDestroy();

	/**
	 * To initialize pipe with given parameters.
	 */
	public void pipeInit() {
		// to be override
	}
	
	/**
	 * Success to create a pipe.
	 */
	public void pipeCreated() {
		// to be override
		// notify pipe is created
	}
	
	/**
	 * Failed to setup a pipe.
	 */
	public void pipeFailed() {
		// to be override
		// notify that pipe is not created correctly.
	}
	
	/**
	 * The pipe is lost. Reasons may be server is down, physical connection
	 * is broken or client side failed to keep pipe alive.
	 */
	public void pipeLost() {
		// to be override
		// notify that pipe is lost. Maybe trying to reconnect the pipe
	}
	
	/**
	 * The pipe is closed by the server side. Pipe could only be closed from
	 * server side. If client want to close a pipe, s/he should send a 
	 * SimpleRPCRunnable request to break down the pipe.
	 */
	public void pipeClosed() {
		// to be override
		// notify that pipe is closed by server.
	}
	
	/**
	 * Return whether the pipe is still live or not.
	 * @return pipe is live or not.
	 */
	public boolean isPipeLive() {
		return pipeAlive;
	}
	
	/**
	 * Notify that the pipe is still alive.
	 */
	public void keepPipeLive() {
		// to be override
	}

	/**
	 * Start pipe monitor to monitor the pipe status. If pipe is non-active,
	 * try to destroy pipe by calling {@link #pipeDestroy()}.
	 * User may override this method to use its own monitoring method.
	 * @j2sIgnore
	 */
	protected void pipeMonitoring() {
		new Thread(new Runnable() {
			
			public void run() {
				while (true) {
					try {
						long interval = pipeMonitoringInterval();
						if (interval <= 0) {
							interval = 1000;
						}
						Thread.sleep(interval);
					} catch (InterruptedException e) {
						//e.printStackTrace();
					}
					if (!isPipeLive()) {
						pipeDestroy();
						break;
					}
				}
			}
		
		}, "Pipe Monitor").start();
	}
	
	/**
	 * Return interval time between two pipe status checking by monitor.
	 * If return interval is less than or equals to 0, the interval time will
	 * be set to 1000 in {@link #pipeMonitoring()}. 
	 * @return time interval in millisecond.
	 * @j2sIgnore
	 */
	protected long pipeMonitoringInterval() {
		return 1000;
	}
	
	/**
	 * Update pipe's live status.
	 * 
	 * @param live if live is true, just notify the pipe is still alive. if live is false
	 * and {@link #isPipeLive()} is true, {@link #pipeDestroy()} will be called.
	 */
	protected void updateStatus(boolean live) {
		if (live) {
			keepPipeLive();
			pipeAlive = true;
		} else if (isPipeLive()) {
			pipeDestroy();
			pipeAlive = false;
		}
	}

	/**
	 * Convert input objects into SimpleSerializable objects.
	 * 
	 * @param args
	 * @return SimpleSerializable objects to be sent through the pipe.
	 * If return null, it means that this pipe does not recognize the
	 * argument objects.
	 */
	public abstract SimpleSerializable[] through(Object ... args);

	/**
	 * Deal the object from pipe.
	 * @param ss
	 * @return boolean Whether the object is dealt
	 */
	public boolean deal(SimpleSerializable ss) {
		try {
			Class<? extends SimpleSerializable> clazz = ss.getClass();
			if ("net.sf.j2s.ajax.SimpleSerializable".equals(clazz.getName())) {
				return true; // seldom or never reach this branch, just ignore
			}
			Method method = null;
			
			Class<?> clzz = getClass();
			String clazzName = clzz.getName();
			int idx = -1;
			while ((idx = clazzName.lastIndexOf('$')) != -1) {
				if (clazzName.length() > idx + 1) {
					char ch = clazzName.charAt(idx + 1);
					if (ch < '0' || ch > '9') { // not a number
						break; // inner class
					}
				}
				clzz = clzz.getSuperclass();
				if (clzz == null) {
					break; // should never happen!
				}
				clazzName = clzz.getName();
			}
			if (clzz != null) {
				method = clzz.getMethod("deal", clazz);
				if (method != null) {
					Class<?> returnType = method.getReturnType();
					if (returnType == boolean.class) {
						Object result = method.invoke(this, ss);
						return ((Boolean) result).booleanValue();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false; // unknown object
	}

	/**
	 * A method used to pipe a bundle of instances through.
	 * 
	 * Attention: Only visible inside {@link #pipeSetup()}.
	 * @param args
	 * @j2sIgnore
	 */
	protected void pipeThrough(Object ... args) {
		SimplePipeRunnable pipe = SimplePipeHelper.getPipe(pipeKey);
		if (pipe == null) return;
		SimpleSerializable[] objs = pipe.through(args);
		
		if (objs == null || objs.length == 0) return;
		
		if (pipe instanceof SimplePipeRunnable) {
			SimplePipeRunnable pipeRunnable = (SimplePipeRunnable) pipe;
			if (pipeRunnable.helper != null) {
				pipeRunnable.helper.helpThrough(pipe, objs);
				return;
			}
		}
		for (int i = 0; i < objs.length; i++) {
			pipe.deal(objs[i]);
		}
	}
	
}