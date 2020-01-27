package com.backbase.games.kalah.db.nosql.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.backbase.games.kalah.db.nosql.entity.GameDTO;

/**
 * Spring data JPA repository for mongoDB
 * 
 * @author didel
 *
 */
/**
 * {@inheritDoc}
 */
public interface GameRepository extends MongoRepository<GameDTO, Integer> {

}
