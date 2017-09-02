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

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CatService {

    @Autowired
    private CatRepository catRepository;

    private static final int MIN_STARS = 1;
    private static final int MAX_STARS = 5;

    final Logger log = LoggerFactory.getLogger(CatService.class);

    public Cat saveCat(Cat cat) {
        return catRepository.save(cat);
    }

    public Cat rateCat(int stars, String comment, Cat cat) {
        log.debug("Rating cat with {} stars and comment: \"{}\"", stars,
                comment);

        if (stars < MIN_STARS) {
            throw new CatException(
                    "The minimum number of possible stars is " + MIN_STARS);
        }
        if (stars > MAX_STARS) {
            throw new CatException(
                    "The maximum number of possible stars is " + MAX_STARS);
        }

        cat.rate(stars, comment);
        log.debug("Cat info after rating: {}", cat);

        return cat;
    }

    public long getCatCount() {
        return catRepository.findAll().spliterator().getExactSizeIfKnown();
    }

    public Iterable<Cat> getAllCats() {
        Iterable<Cat> allCats = catRepository.findAll();
        allCats.forEach(cat -> log.debug("{}", cat));
        return allCats;
    }

    public List<Opinion> getOpinions(Cat cat) {
        Optional<Cat> optionalCat = catRepository.findById(cat.getId());
        if (!optionalCat.isPresent()) {
            throw new CatException("Cat not available: " + cat);
        }
        return optionalCat.get().getOpinions();
    }

}
