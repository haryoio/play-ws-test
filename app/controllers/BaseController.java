package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.logging.Log;
import play.api.data.package$;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import static org.apache.commons.logging.LogFactory.getFactory;

public class BaseController extends Controller {

    /**
     * デバッグ用ロガーを初期化
     */
    protected Log logger = getFactory().getInstance("application");

    protected Result error(String message, int status) {
        ObjectNode resp = Json.newObject();
        resp.put("error", message);
        return status(status, resp);
    }
}
