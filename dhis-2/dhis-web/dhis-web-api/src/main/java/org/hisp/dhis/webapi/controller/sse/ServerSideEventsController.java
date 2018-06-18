package org.hisp.dhis.webapi.controller.sse;

import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import static java.lang.Math.random;

/**
 * Created by henninghakonsen on 15/06/2018.
 * Project: dhis-2.
 */
@EnableScheduling
@RestController
@EnableAsync
@RequestMapping( value = "/" )
public class ServerSideEventsController
{
    private SseEmitter sse = new SseEmitter( 60 * 1000L );

    @Scheduled( fixedRate = 2000 )
    public void generateRandomNumber()
    {
        try
        {
            SseEmitter.SseEventBuilder eventBuilder = SseEmitter.event()
                .id( UUID.randomUUID().toString() )
                .name( "random" )
                .comment( "Stream of random integers" )
                .data( Math.round( random() * 1000 ) );

            sse.send( eventBuilder );
        }
        catch ( IOException ignored )
        {
        }
    }

    @RequestMapping( value = "/sse", method = RequestMethod.GET )
    public SseEmitter sseEmitter(HttpServletRequest request, HttpServletResponse response)
    {
        System.out.println("sseEmitter");
        return sse;
    }

    @RequestMapping("/sse/getMessage")
    public SseEmitter getRealTimeMessageAction(
        HttpServletRequest request)
        throws IOException
    {
        SseEmitter sseEmitter = new SseEmitter();
        // You can send message here
        sseEmitter.send("Message #1");
        return sseEmitter;
    }

    @RequestMapping( value = "/tt", method = RequestMethod.GET )
    public String getTT(HttpServletRequest request, HttpServletResponse response)
    {
        return "22";
    }
}
