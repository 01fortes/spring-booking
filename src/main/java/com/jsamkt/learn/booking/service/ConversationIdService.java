package com.jsamkt.learn.booking.service;

import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.AdvisedRequest;
import org.springframework.ai.chat.client.advisor.api.AdvisedResponse;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisorChain;
import org.springframework.stereotype.Service;

@Service
public class ConversationIdService {

    public CallAroundAdvisor getAdviser(String conversationId) {
        return new CallAroundAdvisor() {
            @Override
            public int getOrder() {
                return 0;
            }

            @Override
            public String getName() {
                return "custom";
            }

            @Override
            public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
                advisedRequest.toolContext().put(
                        AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY,
                        conversationId
                );

                return chain.nextAroundCall(advisedRequest);
            }
        };
    }
}
