package com.enigma.shorten_link.scheduler;

import com.enigma.shorten_link.service.LinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
public class InvokeLinkScheduler {
    @Autowired
    private LinkService service;

    @Scheduled(fixedRateString = "${sholin.schduler.time}")
    public void checkUnHitLink(){
        service.checkUnHitLinkAndInvoke();
    }
}
