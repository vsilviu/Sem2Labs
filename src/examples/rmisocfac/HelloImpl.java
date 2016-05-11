/*
 * Copyright (c) 1998, 1999 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */

package examples.rmisocfac;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class HelloImpl extends UnicastRemoteObject implements Hello {
  
    /* 
     * Constructor calls constructor of superclass with
     * client and server socket factory  parameters.
     */
    public HelloImpl(String protocol, byte [] pattern) throws RemoteException {
	super(0, new MultiClientSocketFactory(protocol, pattern),
	      new MultiServerSocketFactory(protocol, pattern));
    }

    /* 
     * Remote method returns String "Hello World!"
     * when invoked.
     */
    public String sayHello() throws RemoteException {
        return  "Hello World!";
    }
  
    public static void main(String args[]) {

		System.setProperty("java.security.policy","/Users/Silviu/TCLab1/src/examples/rmisocfac/policy");

	//Create and install a security manager
//	if (System.getSecurityManager() == null)
//	    System.setSecurityManager(new RMISecurityManager());
//        byte [] aPattern = { (byte)1011 };
	try {
		byte [] aPattern = { (byte)1011 };
		HelloImpl obj = new HelloImpl("compression", aPattern);
	    //HelloImpl obj = new HelloImpl("compression", null);
		LocateRegistry.createRegistry(1099);
 	    Naming.rebind("/HelloServer", obj);
	    System.out.println("HelloServer bound in registry");
	} catch (Exception e) {
	    System.out.println("HelloImpl err: " + e.getMessage());
	    e.printStackTrace();
	}
    }
} 
