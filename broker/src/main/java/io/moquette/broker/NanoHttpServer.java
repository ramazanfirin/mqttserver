package io.moquette.broker;

import java.io.IOException;
import java.util.Collection;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;

public class NanoHttpServer extends NanoHTTPD{

	SessionRegistry sessions;
	ObjectMapper objectMapper = new ObjectMapper();
	
	public NanoHttpServer(SessionRegistry sessions) throws IOException {
        super(9090);
        this.sessions = sessions;
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    }
	
	@Override
    public Response serve(IHTTPSession session) {
		try {
			Collection<ClientDescriptor> client = sessions.listConnectedClients();
			String result = objectMapper.writeValueAsString(client);
			return newFixedLengthResponse(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return newFixedLengthResponse(Status.INTERNAL_ERROR, NanoHTTPD.MIME_HTML, e.getMessage());
		}
    }
}
