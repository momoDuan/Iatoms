package com.cybersoft4u.xian.iatoms.common.utils.mail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

/**
 * Purpose: mail 资源信息加载器
 * @author candicechen
 * @since  JDK 1.6
 * @date   2015年8月13日
 * @MaintenancePersonnel candicechen
 */
public class StringResourceLoader extends ResourceLoader{
	 /** Key to look up the repository char encoding. */
    public static final String REPOSITORY_ENCODING = "repository.encoding";

    /** The default repository encoding. */
    public static final String REPOSITORY_ENCODING_DEFAULT = "UTF-8";
    /**定义字符集*/
    private static String charset = null;
    /**
     * (non-Javadoc)
     * @see org.apache.velocity.runtime.resource.loader.ResourceLoader#init(org.apache.commons.collections.ExtendedProperties)
     */
	public void init(ExtendedProperties configuration) {
		log.info("StringResourceLoader : initialization starting.");

        String encoding = configuration.getString(REPOSITORY_ENCODING, REPOSITORY_ENCODING_DEFAULT);
        if (encoding == null) {
        	charset = REPOSITORY_ENCODING_DEFAULT;
        } else {
        	charset = encoding;
        }

        log.info("StringResourceLoader : initialization complete.");
	}

	/**
	 * (non-Javadoc)
	 * @see org.apache.velocity.runtime.resource.loader.ResourceLoader#getResourceStream(java.lang.String)
	 */
	public synchronized InputStream getResourceStream(String templateString)
			throws ResourceNotFoundException {
		InputStream result = null;

		if (templateString == null || templateString.length() == 0) {
			throw new ResourceNotFoundException(
					"No   template   string   provided");
		}
		try {
			result = new ByteArrayInputStream(templateString.getBytes(charset));
			return result;
		} catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see org.apache.velocity.runtime.resource.loader.ResourceLoader#isSourceModified(org.apache.velocity.runtime.resource.Resource)
	 */
	public boolean isSourceModified(Resource resource) {
		return false;
	}
	/**
	 * (non-Javadoc)
	 * @see org.apache.velocity.runtime.resource.loader.ResourceLoader#getLastModified(org.apache.velocity.runtime.resource.Resource)
	 */
	public long getLastModified(Resource resource) {
		return 0;
	}
}
