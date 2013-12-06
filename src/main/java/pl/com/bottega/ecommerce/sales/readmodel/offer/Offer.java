package pl.com.bottega.ecommerce.sales.readmodel.offer;

import java.util.List;

import pl.com.bottega.ddd.annotations.appication.Finder;

@Finder
public interface Offer {

	public List<OfferedProductDto> find(OfferQuery query);
}
