package pl.com.bottega.ecommerce.sharedkernel.specification;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author Slawek
 * 
 * @param <T>
 */
public abstract class CompositeSpecification<T> implements Specification<T> {

	public Specification<T> and(Specification<T> other) {
		return new AndSpecification<T>(this, other);
	}

	public Specification<T> or(Specification<T> other) {
		return new OrSpecification<T>(this, other);
	}

	public Specification<T> not() {
		return new NotSpecification<T>(this);
	}

	@SuppressWarnings("unchecked")
	public Specification<T> conjunction(Specification<T>... others) {
		List<Specification<T>> list = Arrays.asList(others);
		list.add(this);
		return new Conjunction<T>(list);
	}
}
