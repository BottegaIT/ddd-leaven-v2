package pl.com.bottega.ecommerce.sharedkernel.specification;

import java.util.List;

public class Conjunction<T> extends CompositeSpecification<T> {

	private List<Specification<T>> list;
	
	public Conjunction(List<Specification<T>> list) {
		this.list = list;
	}

	@Override
	public boolean isSatisfiedBy(T candidate) {
		for (Specification<T> spec : list) {
			if (! spec.isSatisfiedBy(candidate))
				return false;
		}
		
		return true;
	}

}
