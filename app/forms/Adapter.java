package forms;

public interface Adapter<F, E> {

	public abstract F toForm(E entity);

	public abstract E toNewEntity(F form) throws Exception;

	public abstract E toEntity(F form, E entity) throws Exception;

}