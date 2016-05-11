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

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMISocketFactory;


public class MultiClientSocketFactory
        implements RMIClientSocketFactory, Serializable {
    /* 
     * Get the default RMISocketFactory 
     */
    private static RMISocketFactory defaultFactory =
            RMISocketFactory.getDefaultSocketFactory();

    private String protocol;
    private byte[] data;

    public MultiClientSocketFactory(String protocol, byte[] data) {
        this.protocol = protocol;
        this.data = data;
    }

    /* 
     * Override createSocket to call the default 
     * RMISocketFactory's createSocket method. This
     * way, you'll get a TCP connection if you don't
     * specify compression or xor.
     */
    public Socket createSocket(String host, int port)
            throws IOException {
        if (protocol.equals("compression")) {
            return new CompressionSocket(host, port);

        } else if (protocol.equals("xor")) {
            if (data == null || data.length != 1)
                throw new IOException("invalid argument for XOR protocol");
            return new XorSocket(host, port, data[0]);

        }

        return defaultFactory.createSocket(host, port);
    }
}
