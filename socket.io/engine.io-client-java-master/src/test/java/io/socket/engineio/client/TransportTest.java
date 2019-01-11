package io.socket.engineio.client;

import io.socket.engineio.client.transports.PollingXHR;
import io.socket.engineio.client.transports.WebSocket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class TransportTest {

    // NOTE: tests for the rememberUpgrade option are on ServerConnectionTest.

    @Test
    public void uri() {
        Transport.Options opt = new Transport.Options();
        opt.path = "/engine.io";
        opt.hostname = "localhost";
        opt.secure = false;
        opt.query = new HashMap<String, String>() {{
            put("sid", "test");
        }};
        opt.timestampRequests = false;
        Polling polling = new Polling(opt);
        assertThat(polling.uri(), containsString("http://localhost/engine.io?sid=test"));
    }

    @Test
    public void uriWithDefaultPort() {
        Transport.Options opt = new Transport.Options();
        opt.path ="/engine.io";
        opt.hostname = "localhost";
        opt.secure = false;
        opt.query = new HashMap<String, String>() {{
            put("sid", "test");
        }};
        opt.port = 80;
        opt.timestampRequests = false;
        Polling polling = new Polling(opt);
        assertThat(polling.uri(), containsString("http://localhost/engine.io?sid=test"));
    }

    @Test
    public void uriWithPort() {
        Transport.Options opt = new Transport.Options();
        opt.path ="/engine.io";
        opt.hostname = "localhost";
        opt.secure = false;
        opt.query = new HashMap<String, String>() {{
            put("sid", "test");
        }};
        opt.port = 3000;
        opt.timestampRequests = false;
        Polling polling = new Polling(opt);
        assertThat(polling.uri(), containsString("http://localhost:3000/engine.io?sid=test"));
    }

    @Test
    public void httpsUriWithDefaultPort() {
        Transport.Options opt = new Transport.Options();
        opt.path ="/engine.io";
        opt.hostname = "localhost";
        opt.secure = true;
        opt.query = new HashMap<String, String>() {{
            put("sid", "test");
        }};
        opt.port = 443;
        opt.timestampRequests = false;
        Polling polling = new Polling(opt);
        assertThat(polling.uri(), containsString("https://localhost/engine.io?sid=test"));
    }

    @Test
    public void timestampedUri() {
        Transport.Options opt = new Transport.Options();
        opt.path ="/engine.io";
        opt.hostname = "localhost";
        opt.timestampParam = "t";
        opt.timestampRequests = true;
        Polling polling = new Polling(opt);
        assertThat(polling.uri().matches("http://localhost/engine.io\\?(j=[0-9]+&)?t=[0-9A-Za-z-_.]+"), is(true));
    }

    @Test
    public void ipv6Uri() {
        Transport.Options opt = new Transport.Options();
        opt.path ="/engine.io";
        opt.hostname = "::1";
        opt.secure = false;
        opt.port = 80;
        opt.timestampRequests = false;
        Polling polling = new Polling(opt);
        assertThat(polling.uri(), containsString("http://[::1]/engine.io"));
    }

    @Test
    public void ipv6UriWithPort() {
        Transport.Options opt = new Transport.Options();
        opt.path ="/engine.io";
        opt.hostname = "::1";
        opt.secure = false;
        opt.port = 8080;
        opt.timestampRequests = false;
        Polling polling = new Polling(opt);
        assertThat(polling.uri(), containsString("http://[::1]:8080/engine.io"));
    }

    @Test
    public void wsUri() {
        Transport.Options opt = new Transport.Options();
        opt.path ="/engine.io";
        opt.hostname = "test";
        opt.secure = false;
        opt.query = new HashMap<String, String>() {{
            put("transport", "websocket");
        }};
        opt.timestampRequests = false;
        WS ws = new WS(opt);
        assertThat(ws.uri(), is("ws://test/engine.io?transport=websocket"));
    }

    @Test
    public void wssUri() {
        Transport.Options opt = new Transport.Options();
        opt.path = "/engine.io";
        opt.hostname = "test";
        opt.secure = true;
        opt.timestampRequests = false;
        WS ws = new WS(opt);
        assertThat(ws.uri(), is("wss://test/engine.io"));
    }

    @Test
    public void wsTimestampedUri() {
        Transport.Options opt = new Transport.Options();
        opt.path = "/engine.io";
        opt.hostname = "localhost";
        opt.timestampParam = "woot";
        opt.timestampRequests = true;
        WS ws = new WS(opt);
        assertThat(ws.uri().matches("ws://localhost/engine.io\\?woot=[0-9A-Za-z-_.]+"), is(true));
    }

    @Test
    public void wsIPv6Uri() {
        Transport.Options opt = new Transport.Options();
        opt.path ="/engine.io";
        opt.hostname = "::1";
        opt.secure = false;
        opt.port = 80;
        opt.timestampRequests = false;
        WS ws = new WS(opt);
        assertThat(ws.uri(), containsString("ws://[::1]/engine.io"));
    }

    @Test
    public void wsIPv6UriWithPort() {
        Transport.Options opt = new Transport.Options();
        opt.path ="/engine.io";
        opt.hostname = "::1";
        opt.secure = false;
        opt.port = 8080;
        opt.timestampRequests = false;
        WS ws = new WS(opt);
        assertThat(ws.uri(), containsString("ws://[::1]:8080/engine.io"));
    }

    class Polling extends PollingXHR {

        public Polling(Options opts) {
            super(opts);
        }

        public String uri() {
            return super.uri();
        }
    }

    class WS extends WebSocket {

        public WS(Transport.Options opts) {
            super(opts);
        }

        public String uri() {
            return super.uri();
        }
    }
}
