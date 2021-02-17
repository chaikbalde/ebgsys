package net.hub4u.ebgsys.web.controllers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/settings")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SettingsController {

    @GetMapping
    public String settingsHome() {
        return "settings";
    }

}
