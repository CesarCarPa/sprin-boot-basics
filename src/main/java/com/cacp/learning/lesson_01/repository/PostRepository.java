package com.cacp.learning.lesson_01.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cacp.learning.lesson_01.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>{

}
