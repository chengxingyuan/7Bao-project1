package com.wzitech.Z7Bao.frontend.service;

import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.entity.IUser;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.io.IOException;

/**
 * Created by 340082 on 2017/8/7.
 */
@Service("StartService")
@Path("/startService")
public class StartService {
    @Path("/start")
    public void start(@Context HttpServletRequest request, @Context HttpServletResponse response) throws IOException {
            IUser userForAction = CurrentUserContext.getUserForAction();
            System.out.println(userForAction);
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.println(userForAction.toString());
            outputStream.flush();
    }
}
