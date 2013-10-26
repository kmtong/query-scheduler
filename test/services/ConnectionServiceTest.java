package services;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class ConnectionServiceTest {

	@Test
	public void testNewConnection() throws Exception {
		ConnectionService cs = new ConnectionService();
		InputStream is = getClass().getResourceAsStream("/h2.jar");
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		IOUtils.copy(is, os);

		Connection conn = cs.newConnection(os.toByteArray(), "org.h2.Driver",
				"jdbc:h2:mem:play", "sa", "");
		assertNotNull(conn);
	}
}
