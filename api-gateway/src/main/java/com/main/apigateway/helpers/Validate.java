package com.main.apigateway.helpers;

import com.main.apigateway.exception.AuthServiceException;

public class Validate {

    private Validate() {}

    /**
     *
     * @param trueExpectedExpression
     * @param errorCode
     * @param errorMsg
     * @throws AuthServiceException
     * expression should be TRUE, otherwise throws {@link AuthServiceException}
     */
    public static void state(boolean trueExpectedExpression, Integer errorCode, String errorMsg)
        throws AuthServiceException {
        if (!trueExpectedExpression) {
            throw new AuthServiceException(errorMsg, errorCode);
        }
    }

    /**
     *
     * @param falseExpectedExpression
     * @param errorCode
     * @param errorMsg
     * @throws AuthServiceException
     * expression should be FALSE, otherwise throws {@link AuthServiceException}
     */
    public static void stateNot(boolean falseExpectedExpression, Integer errorCode, String errorMsg)
        throws AuthServiceException {
        if (falseExpectedExpression) {
            throw new AuthServiceException(errorMsg, errorCode);
        }
    }

}
