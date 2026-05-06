package kr.hnu.ice.productmvc;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProductService {
    private Map<String, Product> products = new LinkedHashMap<String, Product>();

    public ProductService() {
        products.put("P001", new Product("P001", "노트북", "LG", 1200000, "2026-05-06"));
        products.put("P002", new Product("P002", "스마트폰", "Samsung", 850000, "2026-04-15"));
        products.put("P003", new Product("P003", "무선 이어폰", "Sony", 220000, "2026-03-20"));
    }

    public List<Product> findAll() {
        return new ArrayList<Product>(products.values());
    }

    public Product find(String id) {
        if (id == null) {
            return null;
        }
        return products.get(id);
    }
}
