package my.lubava.curswork3.controllers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping
public class InfoController {

    @GetMapping("/")

    public String hello() {

        return "Application is started!";
    }

    @GetMapping("/info")
    public String info() {
        return "Lubov Martyanova, 08.04.2023, accounting for socks! ";
    }


}
