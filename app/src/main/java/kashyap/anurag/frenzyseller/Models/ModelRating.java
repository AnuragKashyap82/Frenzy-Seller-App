package kashyap.anurag.frenzyseller.Models;

public class ModelRating {

    private String ratings, reviews, productId, uid, reviewTime, reviewDate;

    public ModelRating() {
    }

    public ModelRating(String ratings, String reviews, String productId, String uid, String reviewTime, String reviewDate) {
        this.ratings = ratings;
        this.reviews = reviews;
        this.productId = productId;
        this.uid = uid;
        this.reviewTime = reviewTime;
        this.reviewDate = reviewDate;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(String reviewTime) {
        this.reviewTime = reviewTime;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }
}

