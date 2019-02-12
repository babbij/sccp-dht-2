package com.goodforgoodbusiness.engine;

import static org.apache.jena.graph.NodeFactory.createURI;
import static org.apache.jena.sparql.util.NodeFactoryExtra.createLiteralNode;

import java.util.stream.Collectors;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;

import com.goodforgoodbusiness.engine.crypto.KeyManager;
import com.goodforgoodbusiness.kpabe.local.KPABELocalInstance;
import com.goodforgoodbusiness.model.TriTuple;

public class PatternMakerTest {
	public static void main(String[] args) throws Exception {
		var kpabe = KPABELocalInstance.newKeys();
		var keyManager = new KeyManager(kpabe.getPublicKey(), kpabe.getSecretKey());
		
		System.out.println(
			PatternMaker.forPublish(
				keyManager,
				TriTuple.from(new Triple(
					createURI("https://twitter.com/ijmad8x"),
					createURI("http://xmlns.com/foaf/0.1/name"),
					createLiteralNode("Hello", null, "http://www.w3.org/2001/XMLSchema/string")
				))
			).collect(Collectors.toSet())
		);
		
		System.out.println(
			PatternMaker.forSearch(
				kpabe.getPublicKey(),
				TriTuple.from(
					new Triple(
						createURI("https://twitter.com/ijmad8x"),
						createURI("http://xmlns.com/foaf/0.1/name"),
						Node.ANY
					)
				)
			)
		);
		
		System.out.println(
			PatternMaker.forSearch(
				kpabe.getPublicKey(),
				TriTuple.from(
					new Triple(
						createURI("https://twitter.com/ijmad8x"),
						Node.ANY,
						Node.ANY
					)
				)
			)
		);
		
		System.out.println(
			PatternMaker.forSearch(
				kpabe.getPublicKey(),
				TriTuple.from(
					new Triple(
						Node.ANY,
						Node.ANY,
						createLiteralNode("Hello", null, "http://www.w3.org/2001/XMLSchema/string")
					)
				)
			)
		);
	}
}