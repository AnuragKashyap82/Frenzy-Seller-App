package kashyap.anurag.frenzyseller.Models;

public class ModelInCompleteDetails {

    String productId, shopId;

    public ModelInCompleteDetails() {
    }

    public ModelInCompleteDetails(String productId, String shopId) {
        this.productId = productId;
        this.shopId = shopId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
}
