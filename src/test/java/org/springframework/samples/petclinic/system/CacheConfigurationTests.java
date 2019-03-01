package org.springframework.samples.petclinic.system;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;
import org.springframework.samples.petclinic.system.CacheConfiguration;


@RunWith(SpringRunner.class)
@WebMvcTest(CacheConfiguration.class)

public class CacheConfigurationTests {
	
	private CacheConfiguration cC;
	private CacheManager cManager;
	
	@Mock
	private MutableConfiguration<Object, Object> mC;
	
	@Test
	public void testPetclinicCacheConfigurationCustomizer() {
		
		/**CachingProvider provider = Caching.getCachingProvider();
		CacheManager manager = provider.getCacheManager();

		MutableConfiguration<Object, Object> mC = new MutableConfiguration<>().setStatisticsEnabled(true);
		
		Cache<Object, Object> cache = manager.createCache("vets", mC);
		
		CacheConfiguration cC = cache.getCacheManager().getCache(cache);
		
		assertEquals(mC.isStatisticsEnabled() , cC);
		**/
	
	}
	
	@Test
	public void testCacheConfiguration() {

		cC = new CacheConfiguration();
		
		mC = new MutableConfiguration<Object, Object>();
		
		System.out.println(cC);
		
	}
	
}
