package kashyap.anurag.frenzyseller.Models;

public class ModelAllOrders {

    private String shopId, orderId, orderBy, productId;

    public ModelAllOrders() {
    }

    public ModelAllOrders(String shopId, String orderId, String orderBy, String productId) {
        this.shopId = shopId;
        this.orderId = orderId;
        this.orderBy = orderBy;
        this.productId = productId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
