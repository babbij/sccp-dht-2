package com.goodforgoodbusiness.dhtjava.service;

import java.io.IOException;

import com.goodforgoodbusiness.dhtjava.crypto.Crypto;
import com.goodforgoodbusiness.dhtjava.dht.DHTStore;
import com.goodforgoodbusiness.dhtjava.keys.KeyStore;
import com.goodforgoodbusiness.dhtjava.service.route.ClaimsRoute;
import com.goodforgoodbusiness.dhtjava.service.route.MatchesRoute;
import com.goodforgoodbusiness.shared.web.cors.CorsFilter;
import com.goodforgoodbusiness.shared.web.cors.CorsRoute;
import com.goodforgoodbusiness.shared.web.error.BadRequestException;
import com.goodforgoodbusiness.shared.web.error.BadRequestExceptionHandler;
import com.goodforgoodbusiness.shared.web.error.IOExceptionHandler;

import spark.Service;

public class DHTService {
	private final DHTStore dht;
	private final KeyStore keyStore;
	private final Crypto crypto;
	private final int port;
	
	protected Service service = null;
	
	public DHTService(int port, DHTStore dht, KeyStore keyStore, Crypto crypto) {
		this.port = port;
		this.dht = dht;
		this.keyStore = keyStore;
		this.crypto = crypto;
	}
	
	protected void configure() {
		service.options("/*", new CorsRoute());
		service.before(new CorsFilter());
		
		service.get("/matches", new MatchesRoute(dht, keyStore, crypto));
		service.post("/claims", new ClaimsRoute(dht, keyStore, crypto));
		
		service.exception(BadRequestException.class, new BadRequestExceptionHandler());
		service.exception(IOException.class, new IOExceptionHandler());
	}
	
	public final void start() {
		service = Service.ignite();
		service.port(port);
		
		configure();
		
		service.awaitInitialization();
	}
}
