package io.shmilyhe.socketapi.client;

import io.shmilyhe.khala.utils.LRUCache;
import io.shmilyhe.socketapi.server.ClientWorker;

public class Clients {
	static LRUCache<String,ClientWorker> clients= new LRUCache<String,ClientWorker> ();
}
