package com.epam.cloudx.aws.services;

import com.epam.cloudx.aws.utils.BookApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;
import software.amazon.awssdk.services.sns.model.SubscribeResponse;
import software.amazon.awssdk.services.sns.model.UnsubscribeRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookNotificationsService {

    private static final String EMAIL_PROTOCOL = "email";

    @Setter(onMethod_ = {@Value("${infrastructure.book-notifications-topic.arn}")})
    private String bookNotificationsTopicArn;

    private final SnsClient snsClient;

    public String subscribeEmailToBookNotifications(String email) {
        try {
            SubscribeRequest subscribeRequest = SubscribeRequest.builder()
                    .protocol(EMAIL_PROTOCOL)
                    .endpoint(email)
                    .returnSubscriptionArn(true)
                    .topicArn(bookNotificationsTopicArn)
                    .build();
            SubscribeResponse subscribeResponse = snsClient.subscribe(subscribeRequest);
            log.debug("Email '{}' is subscribed to book notifications topic! subscription ARN: {}", email, subscribeResponse.subscriptionArn());
            return subscribeResponse.subscriptionArn();
        } catch (SdkException e) {
            BookApiUtils.logAndThrowInternalError("Failed to subscribe email to notifications topic", e);
            return null;
        }
    }

    public void unsubscribeEmailToBookNotifications(String subscriptionArn) {
        try {
            UnsubscribeRequest unsubscribeRequest = UnsubscribeRequest.builder()
                    .subscriptionArn(subscriptionArn)
                    .build();
            snsClient.unsubscribe(unsubscribeRequest);
            log.debug("Subscription ARN '{}' was unsubscribed from book notifications topic", subscriptionArn);
        } catch (SdkException e) {
            BookApiUtils.logAndThrowInternalError("Failed to unsubscribe email from notifications topic", e);
        }
    }

    public void sendBookNotification(String message) {
        try {
            PublishRequest publishRequest = PublishRequest.builder()
                    .topicArn(bookNotificationsTopicArn)
                    .message(message)
                    .build();
            snsClient.publish(publishRequest);
        } catch (SdkException e) {
            BookApiUtils.logAndThrowInternalError("Failed to publish SNS mesage", e);
        }
    }
}
