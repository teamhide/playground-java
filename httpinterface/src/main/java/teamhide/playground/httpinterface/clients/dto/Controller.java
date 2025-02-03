package teamhide.playground.httpinterface.clients.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teamhide.playground.httpinterface.clients.PaymentClient;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class Controller {
    private final PaymentClient paymentClient;

    @GetMapping("")
    public void test() {
        paymentClient.getDetail("transactionId");
    }

    @GetMapping("/{transactionId}")
    public String ok(@PathVariable("transactionId") final String transactionId) {
        return transactionId;
    }
}
