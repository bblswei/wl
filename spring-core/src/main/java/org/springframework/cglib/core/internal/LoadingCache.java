package org.springframework.cglib.core.internal;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import org.springframework.cglib.core.internal.Function;
import org.springframework.cglib.core.internal.LoadingCache.1;
import org.springframework.cglib.core.internal.LoadingCache.2;

public class LoadingCache<K, KK, V> {
	protected final ConcurrentMap<KK, Object> map;
	protected final Function<K, V> loader;
	protected final Function<K, KK> keyMapper;
	public static final Function IDENTITY = new 1();

	public LoadingCache(Function<K, KK> keyMapper, Function<K, V> loader) {
		this.keyMapper = keyMapper;
		this.loader = loader;
		this.map = new ConcurrentHashMap();
	}

	public static <K> Function<K, K> identity() {
		return IDENTITY;
	}

	public V get(K key) {
		Object cacheKey = this.keyMapper.apply(key);
		Object v = this.map.get(cacheKey);
		return v != null && !(v instanceof FutureTask) ? v : this.createEntry(key, cacheKey, v);
	}

	protected V createEntry(K key, KK cacheKey, Object v) {
      boolean creator = false;
      FutureTask task;
      Object result;
      if(v != null) {
         task = (FutureTask)v;
      } else {
         task = new FutureTask(new 2(this, key));
         result = this.map.putIfAbsent(cacheKey, task);
         if(result == null) {
            creator = true;
            task.run();
         } else {
            if(!(result instanceof FutureTask)) {
               return result;
            }

            task = (FutureTask)result;
         }
      }

      try {
         result = task.get();
      } catch (InterruptedException arg8) {
         throw new IllegalStateException("Interrupted while loading cache item", arg8);
      } catch (ExecutionException arg9) {
         Throwable cause = arg9.getCause();
         if(cause instanceof RuntimeException) {
            throw (RuntimeException)cause;
         }

         throw new IllegalStateException("Unable to load cache item", cause);
      }

      if(creator) {
         this.map.put(cacheKey, result);
      }

      return result;
   }
}