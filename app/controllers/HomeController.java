package controllers;

import play.mvc.*;

public class HomeController extends BaseController {

    public Result index() {
        logger.debug("logger");
        return ok();
    }

}
