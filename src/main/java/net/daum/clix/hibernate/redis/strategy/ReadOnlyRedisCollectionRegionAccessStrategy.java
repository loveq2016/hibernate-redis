package net.daum.clix.hibernate.redis.strategy;

import net.daum.clix.hibernate.redis.region.RedisCollectionRegion;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.CollectionRegion;
import org.hibernate.cache.access.CollectionRegionAccessStrategy;
import org.hibernate.cache.access.SoftLock;
import org.hibernate.cfg.Settings;

/**
 * @author jtlee
 * @author 84june
 */
public class ReadOnlyRedisCollectionRegionAccessStrategy extends AbstractRedisAccessStrategy<RedisCollectionRegion>
		implements CollectionRegionAccessStrategy {

	public ReadOnlyRedisCollectionRegionAccessStrategy(RedisCollectionRegion region, Settings settings) {
		super(region, settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CollectionRegion getRegion() {
		throw new IllegalAccessError("ReadOnlyRedisCollectionRegionAccessStrategy#getRegion has not implemented yet!!");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object get(Object key, long txTimestamp) throws CacheException {
		return cache.get(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean putFromLoad(Object key, Object value, long txTimestamp, Object version, boolean minimalPutOverride)
			throws CacheException {
		if (minimalPutOverride && region.contains(key)) {
			return false;
		} else {
			cache.put(key, value);
			return true;
		}
	}

	/**
	 * Throws UnsupportedOperationException since this cache is read-only
	 *
	 * @throws UnsupportedOperationException always
	 */
	@Override
	public SoftLock lockItem(Object key, Object version) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Can't write to a readonly object");
	}

	/**
	 * A no-op since this cache is read-only
	 */
	@Override
	public void unlockItem(Object key, SoftLock lock) throws CacheException {
	}
}
