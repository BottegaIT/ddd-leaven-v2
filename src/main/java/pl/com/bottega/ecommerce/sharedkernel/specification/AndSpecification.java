package pl.com.bottega.ecommerce.sharedkernel.specification;

/**
 * 
 * @author Slawek
 *
 * @param <T>
 */
public class AndSpecification<T> extends CompositeSpecification<T>{
    private Specification<T> a;
    private Specification<T> b;

    public AndSpecification(Specification<T> a, Specification<T> b){
        this.a = a;
        this.b = b;
    }

    public boolean isSatisfiedBy(T candidate){
        return a.isSatisfiedBy(candidate) && b.isSatisfiedBy(candidate);
    }
}
