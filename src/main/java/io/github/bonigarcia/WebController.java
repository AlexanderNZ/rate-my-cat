/*
 * (C) Copyright 2017 Boni Garcia (http://bonigarcia.github.io/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package io.github.bonigarcia;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WebController {

    @Autowired
    private CatService catService;

    final Logger log = LoggerFactory.getLogger(WebController.class);

    @RequestMapping(value = "/", method = GET)
    public ModelAndView index() {
        ModelAndView model = new ModelAndView("index");
        model.addObject("cats", catService.getAllCats());
        return model;
    }

    @RequestMapping(value = "/", method = POST)
    public ModelAndView rate(@RequestParam long catId,
            @ModelAttribute Opinion opinion) {
        log.info("Received vote for cat {}: stars={} comment={}", catId,
                opinion.getStars(), opinion.getComment());

        Cat ratedCat = catService.rateCat(opinion.getStars(),
                opinion.getComment(), catId);

        ModelAndView model = new ModelAndView("index");
        model.addObject("cats", catService.getAllCats());

        String message = String.format(Locale.US,
                "Your vote for %s with %.1f stars and '%s' has been stored",
                ratedCat.getName(), opinion.getStars(), opinion.getComment());
        model.addObject("message", message);
        return model;
    }

}
