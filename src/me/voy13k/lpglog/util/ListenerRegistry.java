package me.voy13k.lpglog.util;

import android.content.Context;
import android.support.v4.util.SimpleArrayMap;

public class ListenerRegistry {

	public interface ListenerRegistryHost {
		ListenerRegistry getListenerRegistry();
	}

	private SimpleArrayMap<String, Object> map;

	public ListenerRegistry(int initialCapacity) {
		map = new SimpleArrayMap<String, Object>(initialCapacity);
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(Context context, String key) {
		ListenerRegistry registry = getListenerRegistry(context);
		return (T) registry.map.get(key);
	}

	public static void register(Context context, String key, Object object) {
		ListenerRegistry registry = getListenerRegistry(context);
		registry.map.put(key, object);
	}

	public static void deregister(Context context, String key) {
		ListenerRegistry registry = getListenerRegistry(context);
		registry.map.put(key, null);
	}
	
	private static ListenerRegistry getListenerRegistry(Context context) {
		return ((ListenerRegistryHost) context).getListenerRegistry();
	}

}
