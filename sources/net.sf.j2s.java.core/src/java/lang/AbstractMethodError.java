/* Copyright 1998, 2002 The Apache Software Foundation or its licensors, as applicable
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package java.lang;


/**
 * This error is thrown when the VM notices that a an attempt is being made to
 * invoke an abstract method.
 * <p>
 * Note that this can only occur when inconsistant class files are being loaded,
 * since invoking an abstract method is a compile time error.
 */
public class AbstractMethodError extends IncompatibleClassChangeError {

    private static final long serialVersionUID = -1654391082989018462L;

    /**
	 * Constructs a new instance of this class with its walkback filled in.
	 */
	public AbstractMethodError() {
		super();
	}

	/**
	 * Constructs a new instance of this class with its walkback and message
	 * filled in.
	 * 
	 * @param detailMessage
	 *            String The detail message for the exception.
	 */
	public AbstractMethodError(String detailMessage) {
		super(detailMessage);
	}
}
