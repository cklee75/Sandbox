/**
 * 
 */
package toy;

import static org.junit.Assert.*;

import org.junit.Test;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;

/**
 * @author ck.lee
 *
 */
public class TestLoadTransFromFile {

	@Test
	public void test() throws KettleException {

		KettleEnvironment.init();
		TransMeta metaData = new TransMeta("hello.ktr");
		Trans trans = new Trans(metaData);
		trans.execute(null);
		trans.waitUntilFinished();
		assertEquals(0, trans.getErrors());

	}

}
