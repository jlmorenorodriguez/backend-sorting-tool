package com.example.productsorting.domain.exception;


public class ProductSortingException extends RuntimeException {

    public ProductSortingException(String message) {
        super(message);
    }

    public ProductSortingException(String message, Throwable cause) {
        super(message, cause);
    }

    public static class InvalidCriteriaException extends ProductSortingException {
        public InvalidCriteriaException(String message) {
            super(message);
        }
    }

    public static class InvalidWeightException extends ProductSortingException {
        public InvalidWeightException(String message) {
            super(message);
        }
    }

    public static class InvalidStockFormatException extends ProductSortingException {
        public InvalidStockFormatException(String message) {
            super(message);
        }
    }
}