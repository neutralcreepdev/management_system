/*
 * Copyright 2007 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package QR;

public abstract class ReaderException extends Exception {

  // disable stack traces when not running inside test units
  protected static final boolean isStackTrace =
      System.getProperty("surefire.test.class.path") != null;
  protected static final StackTraceElement[] NO_TRACE = new StackTraceElement[0];

  ReaderException() {
    // do nothing
  }

  ReaderException(Throwable cause) {
    super(cause);
  }

  // Prevent stack traces from being taken
  @Override
  public final synchronized Throwable fillInStackTrace() {
    return null;
  }

}
