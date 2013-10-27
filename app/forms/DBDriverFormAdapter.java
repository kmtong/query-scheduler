package forms;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

import models.DBDriver;

import org.apache.commons.io.IOUtils;

public class DBDriverFormAdapter implements Adapter<forms.DBDriver, DBDriver> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see forms.Adapter#toForm(models.DBDriver)
	 */
	@Override
	public forms.DBDriver toForm(DBDriver dbdriver) {
		forms.DBDriver form = new forms.DBDriver();
		form.name = dbdriver.getName();
		form.driverClass = dbdriver.getDriverClass();
		return form;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see forms.Adapter#toNewEntity(forms.DBDriver)
	 */
	@Override
	public DBDriver toNewEntity(forms.DBDriver form) throws Exception {
		DBDriver entity = new DBDriver();
		return toEntity(form, entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see forms.Adapter#toEntity(forms.DBDriver, models.DBDriver)
	 */
	@Override
	public DBDriver toEntity(forms.DBDriver form, DBDriver entity)
			throws Exception {
		entity.setName(form.name);
		entity.setDriverClass(form.driverClass);
		if (form.jarFile != null) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			IOUtils.copy(new FileInputStream(form.jarFile), out);
			entity.setDriver(out.toByteArray());
		}
		return entity;
	}

}
