package com.example.myapplication.Payment;

public class ZaloPayment {
    private static final String VNPAY_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    private static final String VNP_TMNCODE = "WR09K0DJ";  // Thay bằng TMNCode của bạn
    private static final String VNP_HASHSECRET = "XZILC1VJZPA21J447BVAR60GYT0ADLEG";  // Thay bằng Secret Key của bạn
    private static final String RETURN_URL = "yourapp://vnpay_return";
}
