package com.example.personalizedlearningexperienceapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PaymentsUtil {

    public static JSONObject getPaymentDataRequest(String price) {
        try {
            JSONObject transactionInfo = new JSONObject()
                    .put("totalPrice", price)
                    .put("totalPriceStatus", "FINAL")
                    .put("currencyCode", "USD");

            JSONObject cardPaymentMethod = new JSONObject()
                    .put("type", "CARD")
                    .put("parameters", new JSONObject()
                            .put("allowedAuthMethods", new JSONArray()
                                    .put("PAN_ONLY").put("CRYPTOGRAM_3DS"))
                            .put("allowedCardNetworks", new JSONArray()
                                    .put("AMEX").put("VISA").put("MASTERCARD")))
                    .put("tokenizationSpecification", new JSONObject()
                            .put("type", "PAYMENT_GATEWAY")
                            .put("parameters", new JSONObject()
                                    .put("gateway", "example")
                                    .put("gatewayMerchantId", "exampleMerchantId")));

            JSONObject paymentDataRequest = new JSONObject()
                    .put("apiVersion", 2)
                    .put("apiVersionMinor", 0)
                    .put("allowedPaymentMethods", new JSONArray().put(cardPaymentMethod))
                    .put("transactionInfo", transactionInfo)
                    .put("merchantInfo", new JSONObject().put("merchantName", "CampusConnect"));

            return paymentDataRequest;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}

