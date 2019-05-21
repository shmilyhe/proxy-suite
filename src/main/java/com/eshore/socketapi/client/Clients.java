package com.eshore.socketapi.client;

import com.eshore.khala.utils.LRUCache;
import com.eshore.socketapi.server.ClientWorker;

public class Clients {
	static LRUCache<String,ClientWorker> clients= new LRUCache<String,ClientWorker> ();
}
