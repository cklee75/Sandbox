/**
 * 
 */
package toy;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.Result;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.core.plugins.RepositoryPluginType;
import org.pentaho.di.repository.RepositoriesMeta;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.RepositoryElementMetaInterface;
import org.pentaho.di.repository.RepositoryMeta;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;

/**
 * @author ck.lee
 *
 */
public class TestLoadTransFromRepoDb {
	
	private static final String repositoryName = "mysql";
	private static final String username = "admin";
	private static final String password = "admin";
	private static final String directory = "/";
	private static final String transName = "Data Validator - explore use-cases";

	@Test
	public void test() throws KettleException {
		KettleEnvironment.init();
		
		// read the repositories.xml file to determine available repositories
		RepositoriesMeta repositoriesMeta = new RepositoriesMeta();
		repositoriesMeta.readData();
		
		
		// find the repository definition using its name
		RepositoryMeta repositoryMeta = repositoriesMeta.findRepository(repositoryName);
		
		if (repositoryMeta == null){
			throw new KettleException("Cannot find repository \""+repositoryName+"\". Please make sure it is defined in your "+Const.getKettleUserRepositoriesFile()+" file"); 
		}
		
		// use the plug-in system to get the correct repository implementation
		// the actual implementation will vary depending on the type of given
		// repository (File-based, DB-based, EE, etc.)
		PluginRegistry registry = PluginRegistry.getInstance();
		Repository repository = registry.loadClass(
		       RepositoryPluginType.class,
		       repositoryMeta,
		       Repository.class
		  );
		
		// connect to the repository using given username and password
		repository.init(repositoryMeta);
		repository.connect(username , password);
		
		// find the directory we want to load from
		RepositoryDirectoryInterface tree = repository.loadRepositoryDirectoryTree();
		RepositoryDirectoryInterface dir = tree.findDirectory(directory);
		
		if (dir == null){
			throw new KettleException("Cannot find directory \""+directory+"\" in repository.");
		}
		
		// retrieve name of transformations
		String[] transStrings = repository.getTransformationNames(dir.getObjectId(), false);
		System.out.println("List of transformation:\n" + Arrays.toString(transStrings));
		
		// retrieve transformations
		List<RepositoryElementMetaInterface> transList = repository.getTransformationObjects(dir.getObjectId(), false);
		for (RepositoryElementMetaInterface trans : transList) {
			System.out.println(trans.getName());
			System.out.println(trans.getModifiedDate());
		}

		// load latest revision of the transformation
		// The TransMeta object is the programmatic representation of a transformation definition.
		TransMeta transMeta = repository.loadTransformation(transName, dir, null, true, null);
		
		Trans trans = new Trans(transMeta);
		
		// starting the transformation, which will execute asynchronously
		trans.execute(null);
		
		// waiting for the transformation to finish
		trans.waitUntilFinished();
		
		// retrieve the result object, which captures the success of the transformation
		Result result = trans.getResult();

		assertEquals(0, result.getNrErrors());
		assertEquals(0, trans.getErrors());

		// report on the outcome of the transformation
		String outcome = "\nTrans "+ directory+"/"+transName+" executed "+(result.getNrErrors() == 0?"successfully":"with "+result.getNrErrors()+" errors");
		System.out.println(outcome);

	}

}
