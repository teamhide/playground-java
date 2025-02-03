package teamhide.playground.httpinterface.clients;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import teamhide.playground.httpinterface.clients.dto.ApproveRequest;
import teamhide.playground.httpinterface.clients.dto.ApproveResponse;
import teamhide.playground.httpinterface.clients.dto.GetDetailResponse;

@HttpExchange(url = "${clients.payment.base-url}")
public interface PaymentClient {
    @GetExchange("/{transactionId}")
    GetDetailResponse getDetail(@PathVariable("transactionId") final String transactionId);

    @PostExchange("/approve")
    ApproveResponse approve(@RequestBody final ApproveRequest request);
}
