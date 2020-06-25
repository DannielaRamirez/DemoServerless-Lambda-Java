package com.sophos.demoserverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.sophos.demoserverless.beans.LambdaProxyRequest;
import com.sophos.demoserverless.beans.LambdaProxyResponse;
import com.sophos.demoserverless.controller.LambdaController;

public class LambdaHandler implements RequestHandler<LambdaProxyRequest, LambdaProxyResponse> {

	@Override
	public LambdaProxyResponse handleRequest(LambdaProxyRequest input, Context context) {
		return new LambdaController(context).procesar(input);
	}

}
