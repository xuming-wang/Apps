package org.air.bigearth.apps.image.controller;


import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 公共 控制层
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-03-09
 */
@Controller
@RequestMapping
public class CommonController {

    @RequestMapping(value = "/image", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public byte[] getImage() throws IOException {
        File file = new File("/home/centos/workspace/idea/Apps/src/main/resources/image/1552555536292.png");
        FileInputStream fis = new FileInputStream(file);
        byte[] bytes = new byte[fis.available()];
        fis.read(bytes, 0, fis.available());
        return bytes;
    }

    /*@RequestMapping("/{page}")
    public ModelAndView forwardToPage(@PathVariable String page) {
        Map result = new HashMap();
        return new ModelAndView(page, result);
    }

    @RequestMapping("/{path}/{page}")
    public ModelAndView forwardToPage(@PathVariable String path, @PathVariable String page) {
        Map result = new HashMap();
        return new ModelAndView(path + File.separator + page, result);
    }*/


}
